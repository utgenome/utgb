/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// SEGViewer.java
// Since: 2011/07/03
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.graphics.GenomeCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;
import org.xerial.db.sql.BeanResultHandler;
import org.xerial.db.sql.SQLExpression;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.json.JSONArray;
import org.xerial.json.JSONException;
import org.xerial.json.JSONInteger;
import org.xerial.util.log.Logger;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class CSEGViewer extends WebTrackBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(CSEGViewer.class);
	
	public String species = "human";
	public String revision = "hg18";
	public String name = "chr1";
	public long start = 1;
	public long end = 1000000;
	public String fileName = "";
	private static final int DEFAULT_WIDTH = 800;
	public int width = DEFAULT_WIDTH;
	public static final int DEFAULT_HEIGHT_PER_SAMPLE = 4;
	public static final int DEFAULT_HEIGHT = DEFAULT_HEIGHT_PER_SAMPLE * 200;
	
	/**
	 * A holder for retrieving compressed genome sequence
	 * 
	 * @author mkasa
	 * 
	 */
	public static class SEGData {
		public String chr;
		public int start;
		public int end;
		public String id;
		public String data;

		public SEGData() {
		}

		public SEGData(String chr, int start, int end, String id, String data) {
			this.chr = chr;
			this.start = start;
			this.end = end;
			this.id = id;
			this.data = data;
		}

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append("(");
			buf.append(chr);
			buf.append(":");
			buf.append(start);
			buf.append(",");
			buf.append(end);
			buf.append(")[");
			buf.append(id);
			buf.append("|");
			buf.append(data);
			buf.append("]");
			return buf.toString();
		}
	}

	public class GraphicalOutput implements BeanResultHandler<SEGData>  {
		private Logger _logger = Logger.getLogger(GraphicalOutput.class);

		protected final GenomeCanvas canvas;
		protected long startOffset;
		protected long endOffset;
		protected int width;
		private final HttpServletResponse response;
		protected Color[] colors;
		protected Color scale_color;
		public int processed_records;

		private int param_height_per_sample = 4;
		private int y_offset = 2;
		private int param_samples_per_scale = 10;
		private double font_size = 3; // w.r.t. height_per_sample
		private boolean has_to_draw_scale = true;
		private TreeSet<Integer> param_priority_indicies = new TreeSet<Integer>();
		private boolean is_drawing_priority_indicies = false;
		
		public void setIs_drawing_priority_indicies(boolean is_drawing_priority_indicies) {
			this.is_drawing_priority_indicies = is_drawing_priority_indicies;
		}
		public void setParam_height_per_sample(int param_height_per_sample) {
			this.param_height_per_sample = param_height_per_sample;
		}
		public void setParam_samples_per_scale(int param_samples_per_scale) {
			this.param_samples_per_scale = param_samples_per_scale;
		}
		public void setFont_size(double font_size) {
			this.font_size = font_size;
		}
		public void setDrawScale(int integerParameter) {
			has_to_draw_scale = integerParameter != 0;
		}
		public void setPriorityIndicies(String r) {
			String[] index_strings = r.split(",");
			param_priority_indicies = new TreeSet<Integer>();
			for(String s : index_strings) {
				final int val = Integer.parseInt(s);
				param_priority_indicies.add(val);
				_logger.debug(String.format("PINDEX += %d", val));
			}
		}

		public GraphicalOutput(HttpServletResponse response, long start, long end, int width, int height) {
			this.response = response;
			canvas = new GenomeCanvas(width, height, new GenomeWindow(start, end));
			this.startOffset = start;
			this.endOffset = end + 1;
			this.width = width;
			colors = new Color[5];
			colors[0] = GraphicUtil.parseColor("eeeeee");
			colors[1] = GraphicUtil.parseColor("ff3366");
			colors[2] = GraphicUtil.parseColor("3366ff");
			colors[3] = GraphicUtil.parseColor("ffaadd");
			colors[4] = GraphicUtil.parseColor("aaddff");
			scale_color = GraphicUtil.parseColor("000000");
			processed_records = 0;
		}
		
		/**
		 * 
		 * @param colorString (e.g., '33ffcc,ff3366'; then 0 is mapped to #33ffcc, 1 to #ff3366. do not include any space characters)
		 */
		public void setColors(String colorString)
		{
			String[] colorStrings = colorString.split(",");
			colors = new Color[colorStrings.length];
			for(int i = 0; i < colorStrings.length; i++) {
				_logger.debug(colorStrings[i]);
				colors[i] = GraphicUtil.parseColor(colorStrings[i]);
			}
		}
		
		public void setScaleColor(String colorString)
		{
			scale_color = GraphicUtil.parseColor(colorString);
		}	
		
		public void handle(SEGData sd) throws SQLException {
			final String d = sd.data;
			// d = "[0,1,2,3,2,1,2,3,2,2,2,2,2]"; // for example.
			try {
				if(is_drawing_priority_indicies)
					draw_me_later.add(sd);
				JSONArray d_array = new JSONArray(d);
				for(int i = 0; i < d_array.size(); i++) {
					final int value = d_array.getJSONInteger(i).getIntValue();
					if(value < 0 || colors.length <= value)
						continue;
					if(!is_drawing_priority_indicies || !param_priority_indicies.contains(value)) {
						canvas.drawGeneRect(sd.start, sd.end + 1, y_offset + param_height_per_sample * i, param_height_per_sample, colors[value]);
					}
				}
			} catch (JSONException e) {
				_logger.error("SEGData handle: d_array = '" + d + "'");
			}
		
			processed_records++;
		}

		private ArrayList<SEGData> draw_me_later = null;
		
		public void init() {
			draw_me_later = new ArrayList<SEGData>();
		}

		public void finish() {
			try {
				if(is_drawing_priority_indicies) {
					for(SEGData sd : draw_me_later) {
						try {
							JSONArray d_array = new JSONArray(sd.data);
							for(int i = 0; i < d_array.size(); i++) {
								final int value = d_array.getJSONInteger(i).getIntValue();
								if(value < 0 || colors.length <= value)	continue;
								if(param_priority_indicies.contains(value))
									canvas.drawGeneRect(sd.start, sd.end + 1, y_offset + param_height_per_sample * i, param_height_per_sample, colors[value]);
							}
						} catch (JSONException e) {
							_logger.error("SEGData handle: d_array = '" + sd.data + "'");
						}						
					}
				}
				if(has_to_draw_scale) {
					for(int y = 0; y < DEFAULT_HEIGHT; y += DEFAULT_HEIGHT_PER_SAMPLE * param_samples_per_scale) {
						final int font_size_in_pixel = (int)(DEFAULT_HEIGHT_PER_SAMPLE * font_size);
						canvas.drawLine(startOffset, y + 1, endOffset, y + 1, scale_color);
						canvas.drawText(String.format("%d", y / DEFAULT_HEIGHT_PER_SAMPLE + 1),
								startOffset, startOffset + (endOffset - startOffset) / param_samples_per_scale,
								y + font_size_in_pixel, (float)font_size_in_pixel , scale_color);
					}
				}
				canvas.outputImage(response, "png");
			}
			catch (IOException e) {
				_logger.error(e);
			}
		}

		public void handleException(Exception e) throws Exception {
			_logger.error(e);			
		}
	}

	private int getIntegerParameter(HttpServletRequest request, String parameter_name, int default_value) {
		String r = request.getParameter(parameter_name);
		if(r == null) return default_value;
		int f = Integer.parseInt(r);
		return f;
	}
	
	private String getStringParameter(HttpServletRequest request, String parameter_name, String default_value) {
		String r = request.getParameter(parameter_name);
		if(r == null) return default_value;
		return r;
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// this will load database file specified in config/development.silk file 
			String dbFolder = getTrackConfigProperty("utgb.db.folder", getProjectRootPath() + "/db");
			File dbFile = new File(dbFolder, String.format("p.patens/combined.genmap.imputed.seg.sqlite"));
			if (!dbFile.exists())
				throw new UTGBException(UTGBErrorCode.MISSING_FILES, "DB file doesn't exist: " + dbFile);
			SQLiteAccess db = null;
			try {
				try {
					String sql = SQLExpression.fillTemplate(
							"select chr,start,end,id,data from segdata where chr='$1' and (" +
							"(start <= $2 and $3 <= end) or " + 
							"($2 <= start and start <= $3) or " + 
							"($2 <= end and end <= $3)" + ");",
							name, start, end);
					
					GraphicalOutput handler = new GraphicalOutput(response, start, end, width, getIntegerParameter(request, "ih", 300));
					handler.setParam_height_per_sample(getIntegerParameter(request, "hps", 4));
					handler.setParam_samples_per_scale(getIntegerParameter(request, "spsl", 10));
					handler.setColors(getStringParameter(request, "ca", "eeeeee,ff3366,3366ff,ffaadd,aaddff,6633ff,33ff66,66ff33,ff6633"));
					handler.setScaleColor(getStringParameter(request, "sc", "000000"));
					handler.setDrawScale(getIntegerParameter(request, "dsc", 1));
					{
						String r = getStringParameter(request, "pi", "");
						_logger.debug("PI = [" + r + "]");
						if(r != null && !r.isEmpty()) {
							handler.setPriorityIndicies(r);
							handler.setIs_drawing_priority_indicies(true);
						}
					}
					db = new SQLiteAccess(dbFile.getAbsolutePath());
					db.query(sql, SEGData.class, handler);

					_logger.debug(String.format("processed %d records", handler.processed_records));
				}
				finally {
					if (db != null)
						db.dispose();
				}
			}
			catch (Exception e) {
				throw UTGBException.convert(e);
			}
		}
		catch (UTGBException e) {
			_logger.error(e);
		}

	}

}




