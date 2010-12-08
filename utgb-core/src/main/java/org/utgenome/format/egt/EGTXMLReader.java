/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// EGTXMLReader.java
// Since: Dec 21, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.egt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.utgenome.UTGBException;
import org.xerial.core.XerialErrorCode;
import org.xerial.core.XerialException;
import org.xerial.db.DBException;
import org.xerial.db.sql.SQLExpression;
import org.xerial.db.sql.SQLUtil;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.lens.XMLLens;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;
import org.xerial.util.opt.OptionParserException;

public class EGTXMLReader {

	static abstract class GeneDBGenerator {
		final SQLiteAccess sqlite;

		public GeneDBGenerator(String dbFileName) throws DBException {
			File f;
			if ((f = new File(dbFileName)).exists()) {
				f.delete();
			}
			sqlite = new SQLiteAccess(dbFileName);
		}

		public abstract void insertGene(Gene gene) throws DBException;

		public abstract void createIndexes() throws DBException;

		public void beginTransaction() throws DBException {
			sqlite.update("begin transaction", false);
		}

		public void commit() throws DBException {
			sqlite.update("commit", false);
		}
	}

	static class CompactDBGenerator extends GeneDBGenerator {
		int geneCount = 0;

		public CompactDBGenerator(String dbFileName) throws DBException {
			super(dbFileName);

			sqlite.update("create table gene ( " + "id integer primary key not null, " + "target string, " + "start integer, " + "end integer, "
					+ "strand string, " + "name string)", false);

			sqlite.update("create table gene_info ( " + "id integer primary key not null, " + "url string)", false);

			sqlite.update("create table exon ( " + "gene_id integer, " + "id integer, " + "start integer, " + "end integer, "
					+ "primary key (gene_id, id, start, end))", false);

		}

		@Override
		public void insertGene(Gene gene) throws DBException {
			int geneID = geneCount++;
			sqlite.update(
					SQLExpression.fillTemplate("insert into gene values($1, $2, $3, $4, $5, $6)", geneID, SQLUtil.singleQuote(gene.getTarget()),
							gene.getStart(), gene.getEnd(), SQLUtil.singleQuote(gene.getStrand()), SQLUtil.singleQuote(gene.getName())), false);

			sqlite.update(SQLExpression.fillTemplate("insert into gene_info values($1, $2)", geneID, SQLUtil.singleQuote(gene.getUrl())), false);

			int exonID = 1;
			for (Exon e : gene.getExon()) {
				sqlite.update(SQLExpression.fillTemplate("insert into exon values($1, $2, $3, $4)", geneID, exonID++, e.getStart(), e.getEnd()), false);
			}

		}

		@Override
		public void createIndexes() throws DBException {

		}

	}

	static class MinimalDBGenerator extends GeneDBGenerator {
		int geneCount = 0;

		public MinimalDBGenerator(String dbFileName) throws DBException {
			super(dbFileName);

			sqlite.update("create table gene ( " + "id integer primary key not null, " + "target string, " + "start integer, " + "end integer, "
					+ "strand string, " + "name string)", false);

			/*
			sqlite.update(
					"create table gene_info ( " +
					"id integer primary key not null, " +
					"url string)");
			
			
			sqlite.update(
					"create table exon ( " +
					"gene_id integer, " +
					"id integer, " +
					"start integer, " +
					"end integer, " +
					"primary key (gene_id, id, start, end))");
					*/

		}

		@Override
		public void insertGene(Gene gene) throws DBException {
			int geneID = geneCount++;
			sqlite.update(
					SQLExpression.fillTemplate("insert into gene values($1, $2, $3, $4, $5, $6)", geneID, SQLUtil.singleQuote(gene.getTarget()),
							gene.getStart(), gene.getEnd(), SQLUtil.singleQuote(gene.getStrand()), SQLUtil.singleQuote(gene.getName())), false);

			/*
			sqlite.update(SQLExpression.fillTemplate(
					"insert into gene_info values($1, $2)", 
					geneID,
					SQLUtil.singleQuote(gene.getUrl())));
			
			int exonID = 1;
			for(Exon e : gene.getExon())
			{
				sqlite.update(SQLExpression.fillTemplate(
						"insert into exon values($1, $2, $3, $4)",
						geneID,
						exonID++,
						e.getStart(),
						e.getEnd()));
			}
			*/

		}

		@Override
		public void createIndexes() throws DBException {

		}

	}

	static class WithIndexDBGenerator extends MinimalDBGenerator {
		public WithIndexDBGenerator(String dbName) throws DBException {
			super(dbName);

			sqlite.update("create index gene_index on gene (target, start)", false);

		}
	}

	private static enum TableType {
		COMPACT, MINIMAL, WITHINDEX, DENORMALIZED, VERBOSE
	}

	public static enum Opt {
		HELP, DBTYPE, READ_STDIN, DBFILENAME, USEMEMORYDB
	}

	private static Logger _logger = Logger.getLogger(EGTXMLReader.class);

	private GeneDBGenerator dbGenerator;

	@Option(symbol = "h", longName = "help", description = "display help message")
	private boolean displayHelp = false;
	@Option(symbol = "t", longName = "type", varName = "DBTYPE", description = "(compact|minimal|denomalized|verbose)")
	private TableType tableType = TableType.COMPACT;

	@Option(symbol = "o", longName = "out", varName = "DBFILE", description = "output db file name")
	private String dbName = "egt.db";

	@Option(symbol = "c", longName = "stream", description = "read from standard input")
	private boolean useSTDIN = false;

	@Option(symbol = "m", longName = "memory", description = "use memory db")
	private boolean useMemoryDB = false;

	@Argument(index = 0)
	private String xmlFile = null;

	public static void main(String[] args) {
		try {
			EGTXMLReader reader = new EGTXMLReader(args);
		}
		catch (OptionParserException e) {
			System.err.println(e);
		}
	}

	public EGTXMLReader(String[] args) throws OptionParserException {
		OptionParser parser = new OptionParser(this);
		parser.parse(args);

		if (displayHelp) {
			parser.printUsage();
			return;
		}

		try {

			switch (tableType) {
			case MINIMAL:
				dbGenerator = new MinimalDBGenerator(dbName);
				break;
			case WITHINDEX:
				dbGenerator = new WithIndexDBGenerator(dbName);
				break;
			case COMPACT:
			default:
				dbGenerator = new CompactDBGenerator(dbName);
				break;
			}

			if (useSTDIN) {
				load(new InputStreamReader(System.in));
			}
			else {
				if (xmlFile == null)
					throw new OptionParserException(XerialErrorCode.INVALID_INPUT, "no xml file is given");

				Reader xmlReader = null;
				try {
					xmlReader = new BufferedReader(new FileReader(xmlFile));
					load(xmlReader);
				}
				catch (FileNotFoundException e) {
					throw new UTGBException(e);
				}
				finally {
					if (xmlReader != null)
						xmlReader.close();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void load(Reader xmlReader) throws UTGBException, XerialException, DBException {
		if (dbGenerator == null)
			throw new UTGBException("db generator is null");

		beginTime = System.currentTimeMillis();

		XMLLens.populateBeanWithXML(this, xmlReader);
		dbGenerator.commit();
	}

	private int geneCount = 0;
	private long beginTime = 0;

	public void addGene(Gene gene) {
		try {
			dbGenerator.insertGene(gene);
			if ((geneCount % 10000) == 0) {
				_logger.info("inserted: " + geneCount + "\t " + (System.currentTimeMillis() - beginTime) / 1000.0 + " sec.");
			}
			geneCount++;
		}
		catch (DBException e) {
			_logger.error(e);
		}
	}

}
