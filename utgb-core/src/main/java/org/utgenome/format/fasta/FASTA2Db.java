//--------------------------------------
// base-color Project
//
// FastaToDBGenerator.java
// Since: Jan 25, 2008
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-tracks/base-color/src/main/java/org/utgenome/track/basecolor/FastaToDBGenerator.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.format.fasta;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.zip.GZIPOutputStream;

import org.xerial.db.DBException;
import org.xerial.db.sql.PreparedStatementHandler;
import org.xerial.db.sql.SQLExpression;
import org.xerial.db.sql.SQLUtil;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.util.StopWatch;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;

/**
 * Fasta2DB creates a SQLite database file from a given FASTA format file
 * 
 * @author leo
 * 
 */
public class FASTA2Db {
	private static Logger _logger = Logger.getLogger(FASTA2Db.class);

	public static class Config {
		@Option(symbol = "h", longName = "help", description = "display help message")
		public boolean dispalyHelp = false;
		@Option(symbol = "o", longName = "out", varName = "DBNAME", description = "output SQLite database file name")
		public String outputDBName = null;

		@Argument(index = 0, required = false, name = "input FASTA file")
		public String inputFASTAFile = null;
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

	public void createDB(Reader fasta, SQLiteAccess db) throws Exception {
		FASTAPullParser pullParser = new FASTAPullParser(fasta);

		try {
			db.setAutoCommit(true);
			db.update("pragma synchronous=off");
			db.setAutoCommit(false); // begin a single transaction

			// prepare the database tables
			db.update("drop table if exists description");
			db.update("drop table if exists sequence");
			db.update("drop table if exists sequence_length");
			db.update("create table description (id integer primary key not null, description string)");
			db
					.update("create table sequence (description_id integer not null, start integer, end integer, sequence string, primary key (description_id, start))");
			db.update("create table sequence_length (description_id integer primary_key not null, length integer)");

			stopWatch.reset();
			// load the FASTA file
			int count = 1;
			String description = null;

			final int sequenceSplitUnit = 10000;

			// for each FASTA entry 
			while ((description = pullParser.nextDescriptionLine()) != null) {
				long start = 1;
				long end = 1;

				_logger.info("new entry: " + description);
				db.update(SQLExpression.fillTemplate("insert into description values($1, $2)", count, SQLUtil.singleQuote(description)));

				CompressedBuffer buffer = new CompressedBuffer();

				// for each sequence line
				String seq = null;
				while ((seq = pullParser.nextSequenceLine()) != null) {
					int storedLen = buffer.writtenSize();
					if (storedLen + seq.length() >= sequenceSplitUnit) {
						int remainingSize = sequenceSplitUnit - storedLen;
						buffer.write(seq.substring(0, remainingSize).getBytes());

						end = start + buffer.writtenSize() - 1;
						insertSequence(db, count, start, end, buffer.toByteArray());
						start = end + 1;

						buffer.reset();
						buffer.write(seq.substring(remainingSize).getBytes());
					}
					else {
						buffer.write(seq.getBytes());
					}
				}
				if (buffer.writtenSize() > 0) {
					end = start + buffer.writtenSize() - 1;
					insertSequence(db, count, start, end, buffer.toByteArray());
					start = end + 1;
				}

				// set sequence_length
				db.update(SQLExpression.fillTemplate("insert into sequence_length values($1, $2)", count, end));

				count++;
			}
		}
		catch (DBException e) {
			_logger.error(e);
		}
		finally {
			db.update("commit");
			db.dispose();
		}

		_logger.info("done.");

	}

	public static void main(String[] args) throws Exception {

		Config conf = new Config();
		OptionParser optionParser = new OptionParser(conf);

		optionParser.parse(args);

		if (conf.dispalyHelp) {
			optionParser.printUsage();
			return;
		}

		Reader input = null;
		String fastaName = null;
		if (conf.inputFASTAFile != null) {
			File fastaFile = new File(conf.inputFASTAFile);
			_logger.info("fasta file: " + fastaFile);
			if (!fastaFile.exists())
				throw new Exception(fastaFile.getName() + " does not exist");
			input = new BufferedReader(new FileReader(fastaFile));
			fastaName = fastaFile.getName();
		}
		else {
			input = new InputStreamReader(System.in);
			fastaName = "out";
		}

		assert (fastaName != null);

		String dbName = conf.outputDBName != null ? conf.outputDBName : fastaName + ".db";
		_logger.info("output sqlite db file: " + dbName);

		FASTA2Db p = new FASTA2Db();
		p.createDB(input, new SQLiteAccess(dbName));
	}

	private static int insertCount = 0;
	private static StopWatch stopWatch = new StopWatch();

	private static void insertSequence(SQLiteAccess db, int descriptionID, long start, long end, byte[] sequence) throws DBException {

		db.updateWithPreparedStatement(SQLExpression.fillTemplate("insert into sequence values($1, $2, $3, ?)", descriptionID, start, end), new SequenceSetter(
				sequence));

		insertCount++;
		if ((insertCount % 10000) == 0) {
			_logger.info("inserted " + insertCount + "\t" + stopWatch.getElapsedTime() + " sec.");
		}
	}

	static class SequenceSetter implements PreparedStatementHandler {
		private final byte[] sequence;

		public SequenceSetter(byte[] sequence) {
			this.sequence = sequence;
		}

		public void setup(PreparedStatement preparedStatement) throws SQLException {
			preparedStatement.setBytes(1, sequence);

		}

	}

}