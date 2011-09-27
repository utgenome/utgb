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
// SEGTrack.java
// Since: 2011/07/03
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;

/**
 * Track for displaying read data in SEG format.
 *  
 * <h3>Simplest View Example</h3>
 * 
 * <pre>
 * -track
 *  -class: SEGTrack
 *  -name: Hoge Segment
 *  -properties
 *    -path: db/imported/celegans/normal_gene_exp.seg.sqlite
 * </pre>
 * 
 * <h3>More Complex View Example</h3>
 *
 * <pre>
 * -track
 * -name: Segregation
 * -class: SEGTrack
 * -height: 600
 * -property
 *  -path: db/p.patens/combined.genmap.imputed.seg.sqlite
 *     -Image height: 700
 *     -Color array: eeeeee,ff3366,3366ff,ffaadd,aaddff
 *     -Scale color: 222222
 *     -Height per sample: 4
 *     -Samples per scale line: 10
 * </pre>
 * 
 * @author mkasa
 * 
 */

public class CSEGTrack extends GenomeTrack {

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new CSEGTrack();
			}	
		};
	}

	public CSEGTrack() {
		super("Segments");
	}

	private static final String CONFIG_PATH = "path";
	private static final String CONFIG_HEIGHT_PER_SAMPLE = "Height per sample";
	private int param_height_per_sample = 4;
	private static final String CONFIG_TOTAL_HEIGHT      = "Image height";
	private int param_total_height = 300;
	private static final String CONFIG_COLOR_ARRAY       = "Color array";
	private String param_color_array = "eeeeee,ff3366,3366ff,ffaadd,aaddff";
	private static final String CONFIG_SCALE_COLOR       = "Scale color";
	private String param_scale_color = "000000";
	private static final String CONFIG_SAMPLES_PER_SCALE_LINE = "Samples per scale line";
	private int param_samples_per_scale_line = 10;
	private static final String CONFIG_DRAW_SCALE = "Draw scale";
	private boolean param_draw_scale = true;
	private static final String CONFIG_PRIORITY_INDEX = "Priority indicies";
	private String param_priority_indicies = "";

	// draw() から呼ばれている.
	private void loadConfig() {
		TrackConfig config = getConfig();
		param_height_per_sample = config.getInt(CONFIG_HEIGHT_PER_SAMPLE, param_height_per_sample);
		param_total_height      = config.getInt(CONFIG_TOTAL_HEIGHT, param_total_height);
		param_color_array       = config.getString(CONFIG_COLOR_ARRAY, "ffffff,ff0000,00ff00,0000ff,ffff00,00ffff,ff00ff");
		param_scale_color       = config.getString(CONFIG_SCALE_COLOR, "000000");
		param_samples_per_scale_line = config.getInt(CONFIG_SAMPLES_PER_SCALE_LINE, 10);
		param_draw_scale        = config.getBoolean(CONFIG_DRAW_SCALE, true);
		param_priority_indicies = config.getString(CONFIG_PRIORITY_INDEX, "");
	}

	@Override
	public void draw() {
		loadConfig();
		TrackConfig config = getConfig();
		String path = config.getString(CONFIG_PATH, "file not found");
		// String trackBaseURL = "utgb-core/SEGViewer?%q&fileName=" + path;
		StringBuilder sb = new StringBuilder(256);
		sb.append("utgb-core/CSEGViewer?%%q&ih=");
		sb.append(param_total_height);
		sb.append("&hps=");
		sb.append(param_height_per_sample);
		sb.append("&ca=");
		sb.append(param_color_array);
		sb.append("&sc=");
		sb.append(param_scale_color);
		sb.append("&spsl=");
		sb.append(param_samples_per_scale_line);
		sb.append("&fileName=");
		sb.append(path);
		sb.append("&dsc=");
		sb.append(param_draw_scale ? "1" : "0");
		sb.append("&pi=");
		sb.append(param_priority_indicies);
		config.setParameter(GenomeTrack.CONFIG_TRACK_BASE_URL, sb.toString() /*trackBaseURL*/);

		super.draw();
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		super.setUp(trackFrame, group);
		TrackConfig config = getConfig();
				
		// We don't need the following two lines.
		// config.addConfigString("path", CONFIG_PATH, "");
		// config.setParameter(GenomeTrack.CONFIG_TRACK_TYPE, "image");
		config.addConfigInteger(CONFIG_HEIGHT_PER_SAMPLE, CONFIG_HEIGHT_PER_SAMPLE, param_height_per_sample);
		config.addConfigInteger(CONFIG_TOTAL_HEIGHT, CONFIG_TOTAL_HEIGHT, param_total_height);
		config.addConfig(new StringType(CONFIG_COLOR_ARRAY), param_color_array);
		config.addConfig(new StringType(CONFIG_SCALE_COLOR), param_scale_color);
		config.addConfig(new StringType(CONFIG_PRIORITY_INDEX), param_priority_indicies);
		config.addConfigInteger(CONFIG_SAMPLES_PER_SCALE_LINE, CONFIG_SAMPLES_PER_SCALE_LINE, param_samples_per_scale_line);
		config.addConfigBoolean(CONFIG_DRAW_SCALE, CONFIG_DRAW_SCALE, param_draw_scale);
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.containsOneOf(new String[]{CONFIG_PATH, CONFIG_HEIGHT_PER_SAMPLE, CONFIG_TOTAL_HEIGHT, CONFIG_COLOR_ARRAY, CONFIG_SCALE_COLOR, CONFIG_SAMPLES_PER_SCALE_LINE, CONFIG_PRIORITY_INDEX, CONFIG_DRAW_SCALE})) {
			refresh();
		}
	}

}
