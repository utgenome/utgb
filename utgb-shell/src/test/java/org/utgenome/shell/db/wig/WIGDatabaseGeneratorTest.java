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
// WIGDatabaseGeneratorTest.java
// Since: Nov. 20, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-shell/src/test/java/org/utgenome/shell/db/wig/WIGDatabaseGeneratorTest.java $ 
// $Author: yoshimura $
//--------------------------------------
package org.utgenome.shell.db.wig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.format.wig.WIGDatabaseGenerator;
import org.utgenome.format.wig.WIGDatabaseReader;
import org.utgenome.format.wig.WIGLexer;
import org.utgenome.format.wig.WIGParser;
import org.utgenome.gwt.utgb.client.bio.WigGraphData;
import org.xerial.core.XerialException;
import org.xerial.db.DBException;
import org.xerial.util.FileResource;
import org.xerial.util.bean.impl.BeanUtilImpl;
import org.xerial.util.log.Logger;

public class WIGDatabaseGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToSQLiteDB() throws Exception {
//		WIGDatabaseGenerator.toSQLiteDB(FileResource.open(WIGDatabaseGeneratorTest.class, "sample.wig"), ":memory:");
		WIGDatabaseGenerator.toSQLiteDB(FileResource.open(WIGDatabaseGeneratorTest.class, "sample.wig"), 
										"target/test-classes/org/utgenome/shell/db/wig/sample.wig.sqlite");
	}

	private static Logger _logger = Logger.getLogger(WIGDatabaseGeneratorTest.class);
	private static boolean isVariableStep = true;
	private static boolean isAddTrackId = true;
	
	private static int buffer_count = 0;
	private static long buffer_start = -1;
	private static long buffer_end = -1;
	private static long data_start = 0;
	private static long data_step = 0;

	final int dataSplitUnit = 7777;

	@Test
	public void testWIGDatabaseReader() throws Exception{
		try
		{
//			WIGDatabaseReader wigDBReader = new WIGDatabaseReader("target/test-classes/org/utgenome/shell/db/wig/sample.wig.sqlite"); 
//			for(int trackId : wigDBReader.getTrackIdList()){
//				_logger.info(wigDBReader.getWigData(trackId));
//			}
//			wigDBReader.close();
		}
		catch(Exception e)
		{
			System.err.println(e);			
		}
	}
	
	@Test
	public void testWIGDatabase() throws Exception{		
		try
		{
			HashMap<Long, Float> data = new HashMap<Long, Float>();
			int track_id = -1;
			long nPoints = 0;

			BufferedReader wigReader = new BufferedReader(FileResource.open(WIGDatabaseGeneratorTest.class, "sample.wig"));

			String line = null;
			int lineNum = 1;

			WIGDatabaseReader wigDBReader = new WIGDatabaseReader("target/test-classes/org/utgenome/shell/db/wig/sample.wig.sqlite"); 

			while((line = wigReader.readLine()) != null)
			{
				if (line.startsWith("#") || line.trim().length() == 0)
				{}
				else if (line.startsWith("browser"))
				{
					// insert browser line
					checkBrowserLine(line, wigDBReader);
					nPoints = 0;
				}
				else if (line.startsWith("track") || line.startsWith("variableStep") || line.startsWith("fixedStep"))
				{
					if(isAddTrackId)
					{
						track_id++;
						isAddTrackId = false;
					}
					// insert track line
					readHeaderLine(track_id, line, wigDBReader);
					
					nPoints = 0;
				}
				else
				{
					isAddTrackId = true;
					
					if(isVariableStep)
					{
						String[] lineValues = readDataLine(line, lineNum);
						long currentPoint = Long.parseLong(lineValues[0]);
						if(buffer_count == 0)
						{
							buffer_start = currentPoint;
						}
						else
						{
							buffer_end = currentPoint;
						}
						data.put(currentPoint, Float.parseFloat(lineValues[1]));
					}
					else
					{
						String[] lineValues = readDataLine(line, lineNum);
						long currentPoint = data_start + (nPoints * data_step);
						if(buffer_count == 0)
						{
							buffer_start = currentPoint;
						}
						else
						{
							buffer_end = currentPoint;
						}
						data.put(currentPoint, Float.parseFloat(lineValues[0]));
					}

					nPoints++;
					buffer_count++;
					if(buffer_count >= dataSplitUnit)
					{
						checkDataLine(track_id, data, wigDBReader);
						data.clear();
					}
				}
			}

			wigDBReader.close();
		}
		catch(Exception e)
		{
			System.err.println(e);			
		}
	}
	
	private static void checkBrowserLine(String line, WIGDatabaseReader reader) 
		throws SQLException, DataFormatException
	{
		ArrayList<String> browser = reader.getBrowser();
		
		if(browser.equals(line))
		{
			throw new DataFormatException(String.format("wig(text) : browser = %s, but wig(DB) : browser = null", line));
		}
	}
	
	private static void checkDataLine(int trackId, HashMap<Long,Float> data, WIGDatabaseReader reader) 
		throws NumberFormatException, SQLException, IOException, DataFormatException, ClassNotFoundException
	{
		HashMap<Long, Float> dbData = reader.getData(trackId, buffer_start, buffer_end);
		for(Long key: data.keySet()){
			_logger.debug("text -> db: track = " + trackId + ", point = " + key + ", value = " + data.get(key));
    		if(data.get(key).floatValue() != dbData.get(key).floatValue())
    		{
    			throw new DataFormatException(String.format("wig(text) : data[%s] = %s, but wig(DB) : data[%s] = %s", key, data.get(key), key, dbData.get(key)));
    		}
		}

		for(Long key: dbData.keySet()){
			_logger.debug("db -> text: track = " + trackId + ", point = " + key + ", value = " + dbData.get(key));
    		if(data.get(key).floatValue() != dbData.get(key).floatValue())
    		{
    			throw new DataFormatException(String.format("wig(text) : data[%s] = %s, but wig(DB) : data[%s] = %s", key, data.get(key), key, dbData.get(key)));
    		}
		}
		
		// init variables
		isAddTrackId = true;
		buffer_count = 0;
		buffer_start = -1;
		buffer_end = -1;
	}
	
	private static String[] readDataLine(String line, int lineNum) throws DataFormatException
    {
        String[] temp = line.replace(" ", "\t").trim().split("\t+");
        // split by tab or space
        if (temp.length > 2)
        {
        	throw new DataFormatException("Number of line parameters > 2");
        }
        return temp;
    }

    private static void readHeaderLine(int trackId, String line, WIGDatabaseReader reader) throws IOException, XerialException,
    		RecognitionException, NumberFormatException, DBException, SQLException, DataFormatException
    {
		HashMap<String, String> dbTrack = reader.getTrack(trackId);
		HashMap<String, String> track = new HashMap<String, String>();
		
    	WIGLexer lexer = new WIGLexer(new ANTLRReaderStream(new StringReader(line)));
    	CommonTokenStream tokens = new CommonTokenStream(lexer);
    	
    	WIGParser parser = new WIGParser(tokens);
    	WIGParser.description_return ret = parser.description();

//    	for(String temp:dbTrack.keySet())
//    		_logger.info(temp + ":" + track.get(temp));
    	
    	for (WIGHeaderAttribute a : BeanUtilImpl.createBeanFromParseTree(WIGHeaderDescription.class, (Tree) ret.getTree(),
                WIGParser.tokenNames).attributes)
        {
    		if (line.startsWith("variableStep"))
    		{
    			isVariableStep = true;
        		if(!dbTrack.get("stepType").equals("variableStep"))
        		{
        			throw new DataFormatException(String.format("wig(text) : stepType = variableStep, but wig(db) : stepType = %s", dbTrack.get("stepType")));
        		}
    		}
    		else if (line.startsWith("fixedStep"))
    		{
    			isVariableStep = false;
        		if(!dbTrack.get("stepType").equals("fixedStep"))
        		{
        			throw new DataFormatException(String.format("wig(text) : stepType = fixedStep, but wig(db) : stepType = %s", dbTrack.get("stepType")));
        		}
    		}
        	if(a.name.equals("start"))
        	{
        		data_start = Long.parseLong(a.value);
        	}
        	else if(a.name.equals("step"))
        	{
        		data_step = Long.parseLong(a.value);        		
        	}
			_logger.debug("text -> db: track = " + trackId + ", name = " + a.name + ", value = " + a.value);

    		if(!a.value.equals(dbTrack.get(a.name)))
    		{
    			throw new DataFormatException(String.format("wig(text) : %s = %s, but wig(db) : %s = %s", a.name, a.value, a.name, dbTrack.get(a.name)));
    		}
    		track.put(a.name, a.value);
        }

    	for(String key: dbTrack.keySet())
    	{
    		if(key.equals("stepType"))
    		{
    			continue;
    		}
    		if (line.startsWith("variableStep") && !key.equals("stepType") 
    				&& !key.equals("chrom") && !key.equals("span"))
    		{
    			continue;
    		}
    		else if(line.startsWith("fixedStep") && !key.equals("stepType")
    				&& !key.equals("chrom") && !key.equals("start")
    				&& !key.equals("step") && !key.equals("span"))
    		{
    			continue;
    		}
    		else if(key.equals("stepType") || key.equals("chrom")
    				|| key.equals("start") || key.equals("step")
    				|| key.equals("span"))
    		{
    			continue;
    		}

			_logger.debug("db -> text: track = " + trackId + ", name = " + key + ", value = " + dbTrack.get(key));

    		if(!dbTrack.get(key).equals(track.get(key)))
    		{
    			throw new DataFormatException(String.format("wig(db) : %s = %s, but wig(text) : %s = %s", key, dbTrack.get(key), key, track.get(key)));
    		}    		
    	}
    }
    public static class WIGHeaderDescription
    {
        String name;
        ArrayList<WIGHeaderAttribute> attributes = new ArrayList<WIGHeaderAttribute>();

        public void setName(String name)
        {
            this.name = name;
        }

        public void addAttribute(WIGHeaderAttribute attribute)
        {
            attributes.add(attribute);
        }

        @Override
        public String toString()
        {
            return String.format("name=%s, attributes=%s", name, attributes.toString());
        }
    }

    public static class WIGHeaderAttribute
    {
        String name;
        String value;

        public void setName(String name)
        {
            this.name = name;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return String.format("{name=%s, value=%s}", name, value);
        }
    }
    
}

