/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// utgb-shell Project
//
// WIGDatabaseGenerator.java
// Since: Nov 20, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-shell/src/main/java/org/utgenome/shell/db/wig/WIGDatabaseGenerator.java $ 
// $Author: yoshimura $
//--------------------------------------
package org.utgenome.format.wig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPOutputStream;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.xerial.core.XerialException;
import org.xerial.db.DBException;
import org.xerial.lens.Lens;
import org.xerial.util.StopWatch;
import org.xerial.util.log.Logger;

/**
 * Generating SQLite database of WIG data
 * 
 * @author yoshimur
 * 
 */
public class WIGDatabaseGenerator {

	private static Logger _logger = Logger.getLogger(WIGDatabaseGenerator.class);
	private static StopWatch stopWatch = new StopWatch();

	private static CompressedBuffer chromStartBuffer;
	private static CompressedBuffer dataValueBuffer;

	private static int data_start = 0;
	private static int data_step = 0;

	private static boolean isVariableStep = true;
	private static boolean isAddTrackId = true;
	private static boolean isBufferEmpty = true;

	private static int buffer_count = 0;
	private static long buffer_start = -1;
	private static long buffer_end = -1;
	private static float buffer_maxValue = Float.MIN_VALUE;
	private static float buffer_minValue = Float.MAX_VALUE;

	private static int dataSplitUnit = 100000;
	private static int[] chromStarts;
	private static float[] dataValues;

	public static void toSQLiteDB(Reader wigInput, String dbName) throws IOException, XerialException {
		BufferedReader reader = new BufferedReader(wigInput);

		int track_id = -1;

		chromStartBuffer = new CompressedBuffer();
		dataValueBuffer = new CompressedBuffer();

		int nPoints = 0;
		chromStarts = new int[dataSplitUnit];
		dataValues = new float[dataSplitUnit];

		String line = null;
		int lineNum = 1;

		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			Statement stat = conn.createStatement();

			conn.setAutoCommit(true);
			stat.executeUpdate("pragma synchronous=off");
			conn.setAutoCommit(false); // begin a single transaction

			// prepare the database tables
			stat.executeUpdate("drop table if exists browser");
			stat.executeUpdate("drop table if exists track");
			stat.executeUpdate("drop table if exists data");

			stat.executeUpdate("create table browser (description text)");
			stat.executeUpdate("create table track (track_id integer, name text, value text)");
			stat.executeUpdate("create table data (track_id integer, start integer, end integer, min_value real, "
					+ "max_value real, data_num integer, chrom_starts blob, " + "data_values blob)");

			PreparedStatement browserInfoInsertQuery = conn.prepareStatement("insert into browser values(?)");
			PreparedStatement trackInsertQuery = conn.prepareStatement("insert into track values(?, ?, ?)");
			PreparedStatement dataBlockInsertQuery = conn.prepareStatement("insert into data values(?, ?, ?, ?, ?, ?, ?, ?)");

			stopWatch.reset();

			// for each WIG File 
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#") || line.trim().length() == 0) {
				}
				else if (line.startsWith("browser")) {
					// flush buffer
					if (!isBufferEmpty) {
						insertData(track_id, dataBlockInsertQuery);
						nPoints = 0;
					}

					// insert browser line
					readBrowserLine(browserInfoInsertQuery, line);
				}
				else if (line.startsWith("track") || line.startsWith("variableStep") || line.startsWith("fixedStep")) {
					// flush buffer
					if (!isBufferEmpty) {
						insertData(track_id, dataBlockInsertQuery);
						nPoints = 0;
					}

					if (isAddTrackId) {
						track_id++;
						isAddTrackId = false;
					}

					// insert track line
					readHeaderLine(track_id, trackInsertQuery, line);
				}
				else {
					// insert data lines					
					isBufferEmpty = false;

					if (isVariableStep) {
						String[] lineValues = readDataLine(line, lineNum);
						int currentPoint = Integer.parseInt(lineValues[0]);
						if (buffer_count == 0) {
							buffer_start = currentPoint;
						}
						else {
							buffer_end = currentPoint;
						}
						chromStarts[buffer_count] = currentPoint;
						dataValues[buffer_count] = Float.parseFloat(lineValues[1]);
					}
					else {
						String[] lineValues = readDataLine(line, lineNum);
						int currentPoint = data_start + (nPoints * data_step);
						if (buffer_count == 0) {
							buffer_start = currentPoint;
						}
						else {
							buffer_end = currentPoint;
						}
						dataValues[buffer_count] = Float.parseFloat(lineValues[0]);
					}

					buffer_maxValue = Math.max(dataValues[buffer_count], buffer_maxValue);
					buffer_minValue = Math.min(dataValues[buffer_count], buffer_minValue);

					nPoints++;
					buffer_count++;

					if (buffer_count >= dataSplitUnit) {
						insertData(track_id, dataBlockInsertQuery);
					}
				}
				lineNum++;
			}

			if (!isBufferEmpty) {
				insertData(track_id, dataBlockInsertQuery);
			}

			stat.executeUpdate("create index track_index on track (name, value)");
			stat.executeUpdate("create index data_index on data (track_id, start)");

			conn.commit();

			browserInfoInsertQuery.close();
			trackInsertQuery.close();
			dataBlockInsertQuery.close();
			stat.close();
			conn.close();

		}
		catch (Exception e) {
			_logger.error(String.format("line %d: %s", lineNum, e));
		}
	}

	private static void insertData(int track_id, PreparedStatement p3) throws SQLException, IOException {

		int[] tempChromStarts = new int[buffer_count];
		float[] tempDataValues = new float[buffer_count];

		System.arraycopy(chromStarts, 0, tempChromStarts, 0, buffer_count);
		System.arraycopy(dataValues, 0, tempDataValues, 0, buffer_count);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(buf);
		if (isVariableStep) {
			out.writeObject(tempChromStarts);
			out.flush();
			chromStartBuffer.write(buf.toByteArray());
		}
		buf = new ByteArrayOutputStream();
		out = new ObjectOutputStream(buf);
		out.writeObject(tempDataValues);
		out.flush();
		dataValueBuffer.write(buf.toByteArray());

		// insert data line
		p3.setInt(1, track_id);
		p3.setLong(2, buffer_start);
		p3.setLong(3, buffer_end);
		p3.setFloat(4, buffer_minValue);
		p3.setFloat(5, buffer_maxValue);
		p3.setLong(6, buffer_count);
		p3.setBytes(7, chromStartBuffer.toByteArray());
		p3.setBytes(8, dataValueBuffer.toByteArray());
		p3.execute();

		_logger.info(String.format("insert data %d:%d-%d", track_id, buffer_start, buffer_end));

		// init variables
		chromStarts = new int[dataSplitUnit];
		dataValues = new float[dataSplitUnit];
		isAddTrackId = true;
		isBufferEmpty = true;
		chromStartBuffer.reset();
		dataValueBuffer.reset();
		buffer_count = 0;
		buffer_start = -1;
		buffer_end = -1;
		buffer_maxValue = Float.MIN_VALUE;
		buffer_minValue = Float.MAX_VALUE;
	}

	private static String[] readDataLine(String line, int lineNum) throws DataFormatException {
		String[] temp = line.replace(" ", "\t").trim().split("\t+");
		// split by tab or space
		if (temp.length > 2) {
			throw new DataFormatException("Number of line parameters > 2");
		}
		return temp;
	}

	private static void readBrowserLine(PreparedStatement p1, String line) throws SQLException {
		p1.setString(1, line);
		p1.execute();
	}

	private static void readHeaderLine(int track_id, PreparedStatement p2, String line) throws IOException, XerialException, RecognitionException,
			NumberFormatException, DBException, SQLException {

		WIGLexer lexer = new WIGLexer(new ANTLRReaderStream(new StringReader(line)));
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		WIGParser parser = new WIGParser(tokens);
		WIGParser.description_return ret = parser.description();

		if (line.startsWith("variableStep")) {
			isVariableStep = true;
			p2.setInt(1, track_id);
			p2.setString(2, "stepType");
			p2.setString(3, "variableStep");
			p2.execute();
		}
		else if (line.startsWith("fixedStep")) {
			isVariableStep = false;
			p2.setInt(1, track_id);
			p2.setString(2, "stepType");
			p2.setString(3, "fixedStep");
			p2.execute();
		}

		for (WIGHeaderAttribute a : Lens.loadANTLRParseTree(WIGHeaderDescription.class, (Tree) ret.getTree(), WIGParser.tokenNames).attributes) {
			if (a.name.equals("start")) {
				data_start = Integer.parseInt(a.value);
			}
			else if (a.name.equals("step")) {
				data_step = Integer.parseInt(a.value);
			}

			p2.setInt(1, track_id);
			p2.setString(2, a.name);
			p2.setString(3, a.value);
			p2.execute();
		}
	}

	public static class WIGHeaderDescription {
		String name;
		ArrayList<WIGHeaderAttribute> attributes = new ArrayList<WIGHeaderAttribute>();

		public void setName(String name) {
			this.name = name;
		}

		public void addAttribute(WIGHeaderAttribute attribute) {
			attributes.add(attribute);
		}

		@Override
		public String toString() {
			return String.format("name=%s, attributes=%s", name, attributes.toString());
		}
	}

	public static class WIGHeaderAttribute {
		String name;
		String value;

		public void setName(String name) {
			this.name = name;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.format("{name=%s, value=%s}", name, value);
		}
	}

	static class CompressedBuffer {
		private ByteArrayOutputStream buf;
		private GZIPOutputStream compressor;
		private int writtenSize;

		public CompressedBuffer() throws IOException {
			reset();
		}

		public int writtenSize() {
			return writtenSize;
		}

		public void write(byte[] data) throws IOException {
			compressor.write(data);
			writtenSize += data.length;
		}

		public byte[] toByteArray() throws IOException {
			compressor.finish();
			byte[] ret = buf.toByteArray();
			return ret;
		}

		public void reset() throws IOException {
			buf = new ByteArrayOutputStream();
			compressor = new GZIPOutputStream(buf);
			writtenSize = 0;
		}
	}
}
