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
// WIGReader.java
// Since: 2009/11/30
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-shell/src/main/java/org/utgenome/shell/db/wig/WIGReader.java $ 
// $Author: yoshimura $
//--------------------------------------
package org.utgenome.format.wig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

public class WIGDatabaseReader 
{
	private Connection connection = null;
	private Statement statement;
	
	public WIGDatabaseReader(String inputFileURL) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");

		connection = DriverManager.getConnection("jdbc:sqlite:" + inputFileURL);
		statement = connection.createStatement();
	}

	public void close() throws SQLException
	{
		if(connection != null)
			connection.close();
	}
	
	public ArrayList<Integer> getTrackIdList(String chrom) throws SQLException
	{
		ArrayList<Integer> trackIdList = new ArrayList<Integer>();
		
		ResultSet rs = statement.executeQuery(String.format("select track_id from track where name='%s' and value='%s' ", "chrom", chrom));
		while(rs.next())
		{
			trackIdList.add(Integer.valueOf(rs.getInt("track_id")));
		}
		
		return trackIdList;
	}	
	
	public HashMap<String, String> getTrack(int trackId) throws SQLException
	{
		HashMap<String, String> track = new HashMap<String, String>();

		ResultSet rs = statement.executeQuery(String.format("select * from track where track_id=%d", trackId));
		while(rs.next())
		{
			track.put(rs.getString("name"), rs.getString("value"));
		}
		
		return track;
	}
	
	public HashMap<Long, Double> getData(int trackId, long start, long end)
		throws SQLException, IOException, DataFormatException, NumberFormatException
	{
		HashMap<Long, Double> data = new HashMap<Long, Double>();
		HashMap<String, String> track = getTrack(trackId);

		ResultSet rs = statement.executeQuery(String.format("select * from data where track_id=%d and start<=%d and end>=%d", trackId, end, start));
		while(rs.next())
		{
			int i;
			int temp;
			ByteArrayInputStream buf;
			GZIPInputStream in;
			ByteArrayOutputStream os;

			int nDatas = rs.getInt("data_num");
			long[] chromStarts = new long[nDatas];
			double[] dataValues = new double[nDatas];

			// read data point
			if(track.get("stepType").equals("variableStep"))
			{
				buf = new ByteArrayInputStream(rs.getBytes("chrom_starts"));
				in = new GZIPInputStream(buf);
				os = new ByteArrayOutputStream();
				while((temp = in.read()) != -1)
					os.write(temp);
				os.flush();

				i = 0;
				for(String tempString : os.toString().split("\t"))
				{
					chromStarts[i++] = Long.parseLong(tempString);
				}
				
				os.close();
				in.close();
				buf.close();
			}
			else if(track.get("stepType").equals("fixedStep"))
			{
				long startPoint = rs.getLong("start");
				long stepSize = Long.parseLong(track.get("step"));

				for(i = 0; i < nDatas; i++)
				{
					chromStarts[i] = startPoint + (stepSize * (long)i);
				}
			}
			else
			{
				throw new DataFormatException();
			}

			// read data value
			buf = new ByteArrayInputStream(rs.getBytes("data_values"));
			in = new GZIPInputStream(buf);
			os = new ByteArrayOutputStream();
			while((temp = in.read()) != -1)
				os.write(temp);
			os.flush();

			i = 0;
			for(String tempString : os.toString().split("\t"))
			{
				dataValues[i++] = Double.parseDouble(tempString);
			}

			os.close();
			in.close();
			buf.close();				

			for(i = 0; i < nDatas; i++)
			{
				if(chromStarts[i] <= end && chromStarts[i]>= start)
				{
					data.put(chromStarts[i], dataValues[i]);
				}
			}		
		}
		
		return data;
	}
	
	public HashMap<Long, Double> getData(int trackId)
		throws NumberFormatException, SQLException, IOException, DataFormatException
	{
		return getData(trackId, 0, Long.MAX_VALUE);
	}
}




