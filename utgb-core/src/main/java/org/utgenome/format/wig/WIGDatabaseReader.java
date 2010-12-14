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
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.bio.GraphWindow;
import org.utgenome.gwt.utgb.client.bio.WigGraphData;
import org.xerial.util.StopWatch;
import org.xerial.util.log.Logger;

/**
 * Wig database (sqlite) reader
 * 
 * 
 * @author yoshimura
 * @author leo
 * 
 */
public class WIGDatabaseReader {
	private Connection connection = null;
	private Statement statement;
	private static Logger _logger = Logger.getLogger(WIGDatabaseReader.class);

	private static float minValue = Float.MAX_VALUE;
	private static float maxValue = Float.MIN_VALUE;

	private GraphWindow windowFunc = GraphWindow.MEDIAN;

	public WIGDatabaseReader(File file, GraphWindow windowFunc) throws UTGBException {
		this(file.toString(), windowFunc);
	}

	public WIGDatabaseReader(String inputFileURL, GraphWindow windowFunc) throws UTGBException {
		this.windowFunc = windowFunc;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + inputFileURL);
			statement = connection.createStatement();
		}
		catch (Exception e) {
			throw UTGBException.convert(e);
		}
	}

	public void close() throws SQLException {
		if (connection != null)
			connection.close();
	}

	public ArrayList<String> getBrowser() throws SQLException {
		ArrayList<String> browser = new ArrayList<String>();

		ResultSet rs = statement.executeQuery(String.format("select * from browser"));
		while (rs.next()) {
			browser.add(rs.getString("description"));
		}

		return browser;
	}

	public ArrayList<Integer> getTrackIdList(String chrom) throws SQLException {
		ArrayList<Integer> trackIdList = new ArrayList<Integer>();

		ResultSet rs = statement.executeQuery(String.format("select distinct track_id from track where name='chrom' and value='%s'", chrom));
		while (rs.next()) {
			trackIdList.add(Integer.valueOf(rs.getInt("track_id")));
		}

		return trackIdList;
	}

	public ArrayList<Integer> getTrackIdList() throws SQLException {
		ArrayList<Integer> trackIdList = new ArrayList<Integer>();

		ResultSet rs = statement.executeQuery("select distinct track_id from track");
		while (rs.next()) {
			trackIdList.add(Integer.valueOf(rs.getInt("track_id")));
		}

		return trackIdList;
	}

	public ArrayList<String> getChromList() throws SQLException {
		ArrayList<String> trackIdList = new ArrayList<String>();

		ResultSet rs = statement.executeQuery("select distinct value from track where name='chrom'");
		while (rs.next()) {
			trackIdList.add(rs.getString("value"));
		}

		return trackIdList;
	}

	public HashMap<String, String> getTrack(int trackId) throws SQLException {
		HashMap<String, String> track = new HashMap<String, String>();

		ResultSet rs = statement.executeQuery(String.format("select * from track where track_id=%d", trackId));
		while (rs.next()) {
			track.put(rs.getString("name"), rs.getString("value"));
		}

		return track;
	}

	public HashMap<Integer, Float> getData(int trackId, int start, int end) throws SQLException, IOException, DataFormatException, NumberFormatException,
			ClassNotFoundException, UTGBException {
		return (getData((end - start), trackId, start, end));
	}

	private static interface ValueSelecter {
		public float select(float prev, float max, float min, float avg, float median);
	}

	public static class MAXSelector implements ValueSelecter {
		public float select(float prev, float max, float min, float avg, float median) {
			return Math.max(prev, max);
		}
	}

	public static class MINSelector implements ValueSelecter {
		public float select(float prev, float max, float min, float avg, float median) {
			return Math.min(prev, min);
		}
	}

	public static class MedianSelector implements ValueSelecter {
		public float select(float prev, float max, float min, float avg, float median) {
			return Math.max(prev, median);
		}
	}

	public static class AvgSelector implements ValueSelecter {
		public float select(float prev, float max, float min, float avg, float median) {
			return Math.max(prev, avg);
		}
	}

	private ValueSelecter getSelector() throws UTGBException {
		ValueSelecter selector = null;
		switch (windowFunc) {
		case AVG:
			selector = new AvgSelector();
			break;
		case MAX:
			selector = new MAXSelector();
			break;
		case MEDIAN:
			selector = new MedianSelector();
			break;
		case MIN:
			selector = new MINSelector();
			break;
		default:
			throw new UTGBException(UTGBErrorCode.INVALID_INPUT, "unknown window function: " + windowFunc);
		}
		return selector;
	}

	public CompactWIGData fillPixelsWithMedian(CompactWIGData cwig, int pixelWidth, int trackId, int start, int end) throws SQLException, UTGBException {

		GenomeWindow w = new GenomeWindow(start, end);
		float[] dataValues = new float[pixelWidth];
		for (int i = 0; i < dataValues.length; ++i)
			dataValues[i] = 0.0f;

		StopWatch st1 = new StopWatch();
		StopWatch st2 = new StopWatch();

		float minInBlock = Float.MAX_VALUE;
		float maxInBlock = Float.MIN_VALUE;

		ValueSelecter selector = getSelector();

		ResultSet rs = null;
		try {
			rs = statement.executeQuery(String.format(
					"select start, end, min_value, max_value, median, avg from data where track_id=%d and start<=%d and end>=%d order by start", trackId, end,
					start));
			while (rs.next()) {
				int s = rs.getInt("start");
				int e = rs.getInt("end");
				float max = rs.getFloat("max_value");
				float min = rs.getFloat("min_value");
				float median = rs.getFloat("median");
				float avg = rs.getFloat("avg");

				int pixelStart = w.getXPosOnWindow(s, pixelWidth);
				if (pixelStart <= 0)
					pixelStart = 0;
				int pixelEnd = w.getXPosOnWindow(e + cwig.getSpan(), pixelWidth);
				for (int x = pixelStart; x < pixelWidth && x < pixelEnd; ++x) {
					dataValues[x] = selector.select(dataValues[x], max, min, avg, median);
				}

				minInBlock = Math.min(min, minInBlock);
				maxInBlock = Math.max(max, maxInBlock);
			}
		}
		finally {
			if (rs != null)
				rs.close();
		}

		cwig.setMinValue(minInBlock);
		cwig.setMaxValue(maxInBlock);
		cwig.setData(dataValues);

		_logger.debug("Time(all)    : " + st1.getElapsedTime());
		return cwig;

	}

	public HashMap<Integer, Float> getData(int windowWidth, int trackId, int start, int end) throws SQLException, IOException, DataFormatException,
			NumberFormatException, ClassNotFoundException, UTGBException {
		HashMap<Integer, Float> data = new HashMap<Integer, Float>();
		HashMap<String, String> track = getTrack(trackId);

		int[] chromStarts;
		float[] dataValues;

		minValue = Float.MAX_VALUE;
		maxValue = Float.MIN_VALUE;

		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}

		int rough = (int) Math.floor((end - start) / windowWidth);
		if (rough < 1)
			rough = 1;

		ValueSelecter selector = getSelector();

		StopWatch st1 = new StopWatch();
		StopWatch st2 = new StopWatch();

		ResultSet rs = statement.executeQuery(String.format("select * from data where track_id=%d and start<=%d and end>=%d order by start", trackId, end,
				start));
		while (rs.next()) {
			int i;
			ByteArrayInputStream bis;
			GZIPInputStream in;
			ObjectInputStream ois;

			int nDatas = rs.getInt("data_num");

			// read data point
			if (track.get("stepType").equals("variableStep")) {
				bis = new ByteArrayInputStream(rs.getBytes("chrom_starts"));
				in = new GZIPInputStream(bis);
				ois = new ObjectInputStream(in);

				chromStarts = (int[]) ois.readObject();

				ois.close();
				in.close();
				bis.close();
			}
			else if (track.get("stepType").equals("fixedStep")) {
				int startPoint = rs.getInt("start");
				int stepSize = Integer.parseInt(track.get("step"));
				chromStarts = new int[nDatas];

				for (i = 0; i < nDatas; i++) {
					chromStarts[i] = startPoint + (stepSize * i);
				}
			}
			else {
				throw new DataFormatException();
			}

			// read data value
			bis = new ByteArrayInputStream(rs.getBytes("data_values"));
			in = new GZIPInputStream(bis);
			ois = new ObjectInputStream(in);

			dataValues = (float[]) ois.readObject();

			ois.close();
			in.close();
			bis.close();

			st2.resume();
			for (i = 0; i < nDatas; i++) {
				if (start <= chromStarts[i] && chromStarts[i] <= end) {
					if (dataValues[i] != 0.0f) {
						int chromStart = chromStarts[i] - (chromStarts[i] % rough);
						if (data.containsKey(chromStart)) {
							float prev = data.get(chromStart);
							float current = dataValues[i];
							float newValue = selector.select(prev, maxValue, minValue, current, current);
							data.put(chromStart, newValue);
						}
						else {
							data.put(chromStart, dataValues[i]);
						}
					}

					minValue = Math.min(minValue, dataValues[i]);
					maxValue = Math.max(maxValue, dataValues[i]);
				}
			}
			st2.stop();
		}

		rs.close();

		_logger.debug("min: " + minValue + ", max: " + maxValue);
		_logger.debug("Time(all)    : " + st1.getElapsedTime());
		_logger.debug("Time(archive): " + st2.getElapsedTime());
		return data;
	}

	public HashMap<Integer, Float> getData(int trackId) throws NumberFormatException, SQLException, IOException, DataFormatException, ClassNotFoundException,
			UTGBException {
		return getData(trackId, 0, Integer.MAX_VALUE);
	}

	public WigGraphData getWigData(int windowWidth, int trackId, int start, int end) throws SQLException, NumberFormatException, IOException,
			DataFormatException, ClassNotFoundException, UTGBException {
		WigGraphData wigData = prepareWigData(trackId);
		wigData.setData(getData(windowWidth, trackId, start, end));
		return wigData;
	}

	public WigGraphData getWigData(int trackId, int start, int end) throws SQLException, NumberFormatException, IOException, DataFormatException,
			ClassNotFoundException, UTGBException {
		WigGraphData wigData = prepareWigData(trackId);
		wigData.setData(getData(trackId, start, end));
		return wigData;
	}

	public CompactWIGData getCompactWigData(int trackId, int start, int end, int pixelWidth) throws SQLException, UTGBException {
		WigGraphData wigData = prepareWigData(trackId);
		CompactWIGData cWig = prepareCompactWigData(wigData, new ChrLoc(null, start, end));
		fillPixelsWithMedian(cWig, pixelWidth, trackId, start, end);
		return cWig;
	}

	private WigGraphData prepareWigData(int trackId) throws SQLException {
		WigGraphData wigData = new WigGraphData();
		wigData.setTrack_id(trackId);
		wigData.setBrowser(getBrowser());
		wigData.setTrack(getTrack(trackId));
		wigData.setMinValue(minValue);
		wigData.setMaxValue(maxValue);
		return wigData;
	}

	public WigGraphData getWigData(int trackId) throws SQLException, NumberFormatException, IOException, DataFormatException, ClassNotFoundException,
			UTGBException {
		return getWigData(trackId, 0, Integer.MAX_VALUE);
	}

	public ArrayList<WigGraphData> getWigDataList(int windowWidth, String chrom, int start, int end) throws SQLException, NumberFormatException, IOException,
			DataFormatException, ClassNotFoundException, UTGBException {
		ArrayList<WigGraphData> wigDataList = new ArrayList<WigGraphData>();

		for (int id : getTrackIdList(chrom)) {
			wigDataList.add(getWigData(windowWidth, id, start, end));
		}

		return wigDataList;
	}

	public ArrayList<WigGraphData> getWigDataList(String chrom, int start, int end) throws SQLException, NumberFormatException, IOException,
			DataFormatException, ClassNotFoundException, UTGBException {
		ArrayList<WigGraphData> wigDataList = new ArrayList<WigGraphData>();

		for (int id : getTrackIdList(chrom)) {
			wigDataList.add(getWigData(id, start, end));
		}

		return wigDataList;
	}

	public static List<WigGraphData> getWigDataList(File fileName, int windowWidth, ChrLoc location, GraphWindow windowFunc) throws UTGBException, SQLException {
		ArrayList<WigGraphData> wigDataList = null;

		WIGDatabaseReader reader = new WIGDatabaseReader(fileName, windowFunc);
		try {
			wigDataList = reader.getWigDataList(windowWidth, location.chr, location.start, location.end);
		}
		catch (Exception e) {
			_logger.error(e);
			e.printStackTrace(System.err);
		}
		finally {
			reader.close();
		}

		return wigDataList;
	}

	public static List<CompactWIGData> getRoughCompactWigDataList(File path, int pixelWidth, ChrLoc location, GraphWindow windowFunc) throws UTGBException,
			SQLException {

		ArrayList<CompactWIGData> cWig = new ArrayList<CompactWIGData>();
		WIGDatabaseReader reader = new WIGDatabaseReader(path, windowFunc);
		for (int id : reader.getTrackIdList(location.chr)) {
			cWig.add(reader.getCompactWigData(id, location.start, location.end, pixelWidth));
		}
		return cWig;
	}

	public static List<CompactWIGData> getCompactWigDataList(File path, int pixelWidth, ChrLoc location, GraphWindow windowFunc) throws UTGBException,
			SQLException {

		int numBlocks = location.length() / WIGDatabaseGenerator.DATA_SPLIT_UNIT;
		if (numBlocks >= pixelWidth) {
			// use max values in the wig data table
			_logger.debug(String.format("query wig (path:%s, width:%d, loc:%s)", path, pixelWidth, location));
			return getRoughCompactWigDataList(path, pixelWidth, location, windowFunc);
		}
		else {
			ArrayList<CompactWIGData> cWig = new ArrayList<CompactWIGData>();
			List<WigGraphData> wig = getWigDataList(path, pixelWidth, location, windowFunc);
			for (WigGraphData w : wig) {
				cWig.add(WIGDatabaseReader.convertResolution(w, location, pixelWidth));
			}
			return cWig;
		}
	}

	public static CompactWIGData prepareCompactWigData(WigGraphData w, ChrLoc location) {
		CompactWIGData cwig = new CompactWIGData();
		cwig.setTrack(w.getTrack());
		cwig.setMaxValue(w.getMaxValue());
		cwig.setMinValue(w.getMinValue());
		cwig.setBrowser(w.getBrowser());
		cwig.setTrack_id(w.getTrack_id());
		cwig.setStart(location.start < location.end ? location.start : location.end);
		int span = 1;
		if (w.getTrack().containsKey("span")) {
			span = Integer.parseInt(w.getTrack().get("span"));
			cwig.setSpan(span);
		}
		return cwig;
	}

	public static CompactWIGData convertResolution(WigGraphData w, ChrLoc location, int windowWidth) {
		CompactWIGData cwig = prepareCompactWigData(w, location);
		final int span = cwig.getSpan();

		GenomeWindow window = new GenomeWindow(location.start, location.end);

		float[] pixelWiseGraphData = new float[windowWidth + span];

		Map<Integer, Float> data = w.getData();
		for (Map.Entry<Integer, Float> each : data.entrySet()) {
			int xOnGenome = each.getKey();
			float val = each.getValue();

			int x1 = window.getXPosOnWindow(xOnGenome, windowWidth);
			int x2 = window.getXPosOnWindow(xOnGenome + span, windowWidth);
			if (x1 == x2)
				x2 = x1 + 1;

			if (x1 < 0)
				x1 = 0;

			for (int i = x1; i < x2 && i < windowWidth + span; ++i) {
				float current = pixelWiseGraphData[i];
				float abs = Math.abs(val);
				if (current < abs) {
					pixelWiseGraphData[i] = val; // take the max (or min for negative value)
				}
			}
		}

		cwig.setData(pixelWiseGraphData);
		return cwig;
	}

}
