/*--------------------------------------------------------------------------
 *  Copyright 2012 utgenome.org
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
// utgb-core Project
//
// CSEGDatabase.java
// Since: 2012/01/16
//
//--------------------------------------
package org.utgenome.format.cseg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.xerial.util.StopWatch;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;

public class CSEGDatabase {
	private static Logger _logger = Logger.getLogger(CSEGDatabase.class);

	private static StopWatch stopWatch = new StopWatch();

	public static class Config {
		@Option(symbol = "h", longName = "help", description = "display help message")
		public boolean dispalyHelp = false;
		@Option(symbol = "o", longName = "out", varName = "DBNAME", description = "output SQLite database file name")
		public String outputDBName = null;

		@Argument(index = 0, required = false, name = "input CSEG file")
		public String inputCSEGFile = null;
	}

	private static final int SEGDATA_COMMIT_LENGTH = 1000;

	public void createDB(Reader cseg, Connection db) throws Exception {
		try {
	        Statement stmt = db.createStatement();

			db.setAutoCommit(true);
			stmt.execute("pragma synchronous=off");

			// prepare the database tables
			stmt.execute("drop table if exists segdata");
			stmt.execute("create table segdata (chr string, start integer, end integer, id string, data string)");

			db.setAutoCommit(false); // begin a single transaction
			_logger.debug("end create db: " + "\t" + stopWatch.getElapsedTime() + " sec.");
		} catch (Exception ex) {
			_logger.error(ex.getMessage());
        } 
		
		// load the CSEG file

		final BufferedReader reader = new BufferedReader(cseg);
        final String insertSQL = "insert into segdata values(?,?,?,?,?)";
        final PreparedStatement ps = db.prepareStatement(insertSQL);
          
		// for each CSEG entry            
    	_logger.debug("begin insert data: " + "\t" + stopWatch.getElapsedTime() + " sec.");
    	int lineCount = 1;
    	int columnSize = -1; // -1 indicates that the column size is not yet determined (i.e., have not yet read the first line of data).
		try {
			String segDataLine;
            while ((segDataLine = reader.readLine()) != null) {
				lineCount++;
				if(lineCount <= 1) continue; // because of header
				final String[] segdataColumns = segDataLine.split("	");
				if(segdataColumns.length < 5) {
					_logger.error(String.format("Number of columns is less than 5 at line %d", lineCount));
					break;
				}
				if(columnSize == -1) {
					columnSize = segdataColumns.length - 4;
				} else {
					_logger.error(String.format("Number of columns must be equal at all lines. Error found at line %d", lineCount));
					break;
				}
				final String id = segdataColumns[0];
				final String scaffoldName = segdataColumns[1];
				final int startPos = Integer.parseInt(segdataColumns[2]);
				final int endPos = Integer.parseInt(segdataColumns[3]);
				final StringBuilder data = new StringBuilder(2048/*initial size*/);
				data.append(segdataColumns[4]);
				for (int i = 5; i < segdataColumns.length; i++){
					data.append(',');
					data.append(segdataColumns[i]);
				}					
                ps.setString(1, scaffoldName);
                ps.setInt(2, startPos);
                ps.setInt(3, endPos);
                ps.setString(4, id);
                ps.setString(5, data.toString());
                ps.executeUpdate();
			}
		} catch (NumberFormatException ex) {
			_logger.error(String.format("Failed in parsing integer at line %d", lineCount));
		} catch (Exception ex) {
			_logger.error(ex.getMessage());
        } finally {
			db.commit();
		}
	}
	
	private void createIndex(Reader input, Connection db) throws Exception {
		try{
			Statement stmt = db.createStatement();
			_logger.info("create index");
			stmt.execute("create index segdata_pos_start on segdata(chr, start);");
			stmt.execute("create index segdata_pos_end on segdata(chr, end);");
			db.commit();
		}
		catch (Exception ex) {
			_logger.error(ex.getMessage());
        } 
		
	}
	public static void main(String[] args) throws Exception {

		Config conf = new Config();
		OptionParser optionParser = new OptionParser(conf);

		optionParser.parse(args);
		stopWatch.reset();

		if (conf.dispalyHelp) {
			optionParser.printUsage();
			return;
		}

		Reader input = null;
		String CSEGName = null;
		if (conf.inputCSEGFile != null) {
			File CSEGFile = new File(conf.inputCSEGFile);
			_logger.info("cseg file: " + CSEGFile);
			if (!CSEGFile.exists())
				throw new Exception(CSEGFile.getName() + " does not exist");
			input = new BufferedReader(new FileReader(CSEGFile));
			CSEGName = CSEGFile.getName();
		}
		else {
			input = new InputStreamReader(System.in);
			CSEGName = "out";
		}

		assert (CSEGName != null);

		String dbName = conf.outputDBName != null ? conf.outputDBName : CSEGName + ".db";
		_logger.info("output sqlite db file: " + dbName);

        Connection db = null;
        String url = "jdbc:sqlite:"+dbName;
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection(url);
            db.setAutoCommit(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        
		CSEGDatabase p = new CSEGDatabase();
		
		_logger.debug("begin create db" + "\t" + stopWatch.getElapsedTime() + " sec.");
		p.createDB(input, db);
		_logger.debug("end insert data" + "\t" + stopWatch.getElapsedTime() + " sec.");
		_logger.debug("begin create index" + "\t" + stopWatch.getElapsedTime() + " sec.");
		p.createIndex(input, db);
		db.close();
		_logger.info("done" + "\t" + stopWatch.getElapsedTime() + " sec.");

	}

}