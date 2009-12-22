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
// GenomeReadFormat.java
// Since: Apr 10, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.silk.read;

import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.xerial.core.XerialError;
import org.xerial.core.XerialErrorCode;
import org.xerial.lens.Lens;
import org.xerial.silk.SilkEnv;
import org.xerial.silk.SilkParser;
import org.xerial.silk.SilkParserConfig;
import org.xerial.util.log.Logger;

/**
 * Reader
 * 
 * @author leo
 * 
 */
public class ReadDBBuilder {

	private static Logger _logger = Logger.getLogger(ReadDBBuilder.class);

	public static class ReferenceReader extends Reference {
		private static int count = 1;
		private final int id;

		private StringBuilder sequenceBuilder = new StringBuilder();

		public ReferenceReader() {
			id = count++;
		}

		public void addRead(Read read) {
			// add read data
			if (_logger.isDebugEnabled())
				_logger.debug("add read: " + read);

			try {
				stat.execute(String.format("insert into read values(%d, '%s', %d, %d, %d, %d, '%s', '%s')", this.id, read.name, read.viewstart, read.viewend,
						read.start, read.end, read.strand, read.sequence));
			}
			catch (SQLException e) {
				_logger.error(e);
			}

		}

		public void appendSequence(String seq) {
			sequenceBuilder.append(seq);
		}

		@Override
		public String toString() {
			return String.format("reference: name=%s, start=%d, strand=%s\nsequence=%s", name, start, strand, sequenceBuilder.toString());
		}

	}

	private static Connection conn = null;
	private static Statement stat = null;

	public ReadDBBuilder() {

	}

	public ReadDBBuilder(String dbFilePath) throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);

			conn.setAutoCommit(true);
			stat = conn.createStatement();
			stat.execute("pragma synchronous=off");
			conn.setAutoCommit(false);

			// prepare table
			stat.execute("create table coordinate('group' text, species text, revision text)");
			stat.execute("create table reference(id integer primary key, name text, start integer, strand text, tag integer, score integer, sequence text)");
			stat
					.execute("create table read(reference_id integer, name text, view_start integer, view_end integer, start integer, end integer, strand text, sequence text)");
		}
		catch (ClassNotFoundException e) {
			throw new XerialError(XerialErrorCode.INVALID_STATE, "sqlite JDBC not found");
		}

	}

	public void build(Reader input) throws SQLException {
		try {
			SilkParserConfig config = new SilkParserConfig();
			config.bufferSize = 8 * 1024 * 1024;
			config.numWorkers = 2;
			SilkParser parser = new SilkParser(input, SilkEnv.newEnv(), config);
			Lens.load(this, parser);
		}
		catch (Exception e) {
			_logger.error(e);
		}
		finally {
			commit();
		}

	}

	public void build(URL silkFile) throws SQLException {
		// TODO remove duplicate code
		try {
			SilkParserConfig config = new SilkParserConfig();
			config.bufferSize = 8 * 1024 * 1024;
			config.numWorkers = 2;
			SilkParser parser = new SilkParser(silkFile, config);
			Lens.load(this, parser);
		}
		catch (Exception e) {
			_logger.error(e);
		}
		finally {
			commit();
		}
	}

	public void setCoordinate(Coordinate coordinate) throws SQLException {
		if (_logger.isDebugEnabled())
			_logger.debug("set coordinate: " + coordinate);

		stat.execute(String.format("insert into coordinate values('%s', '%s', '%s')", coordinate.group, coordinate.species, coordinate.revision));

	}

	public void addReference(ReferenceReader reference) throws SQLException {
		if (_logger.isDebugEnabled())
			_logger.debug("add reference: " + reference);

		stat.execute(String.format("insert into reference values(%d, '%s', %d, '%s', %d, %d, '%s')", reference.id, reference.name, reference.start,
				reference.strand, reference.tag, reference.score, reference.sequenceBuilder.toString()));

	}

	public void commit() throws SQLException {
		conn.commit();
		conn.close();
		_logger.info("commit done.");

		stat = null;
		conn = null;
	}

}
