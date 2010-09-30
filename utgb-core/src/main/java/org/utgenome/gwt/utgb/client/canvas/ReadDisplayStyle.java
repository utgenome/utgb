/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// ReadDisplayStyle.java
// Since: 2010/09/30
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.db.ValueDomain;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.TrackConfig;

import com.google.gwt.widgetideas.graphics.client.Color;

/**
 * Configuration of ReadTrack display
 * 
 * @author leo
 * 
 */
public class ReadDisplayStyle {

	String DEFAULT_READ_COLOR = "#666666";
	String FORWARD_READ_COLOR = "#d80067";
	String REVERSE_READ_COLOR = "#0067d8";
	String PADDING_COLOR = "#333333";

	String ORPHANED_READ_COLOR = "#333333";
	String WEIRED_READ_COLOR = "#993333";

	public boolean showLabels = true;
	public boolean useDifferentColorForForwardAndReverse = true;
	public boolean drawShadow = true;
	public boolean showBaseQuality = false;
	public boolean overlapPairedReads = false;
	public int numReadsMax = 500;
	public float clippedRegionAlpha = 0.2f;

	public int readHeight = 12;
	public int minReadHeight = 2;
	public String coverageStyle = "default";
	public String layout = "pileup";

	public final static String CONFIG_LAYOUT = "layout";
	public final static String CONFIG_SHOW_LABELS = "showLabels";
	public final static String CONFIG_READ_HEIGHT = "read height";
	public final static String CONFIG_MIN_READ_HEIGHT = "min read height";
	public final static String CONFIG_NUM_READ_MAX = "num reads to display";
	public final static String CONFIG_SHOW_BASE_QUALITY = "show base quality";
	public final static String CONFIG_PE_OVERLAP = "overlap paired reads";
	public final static String CONFIG_COVERAGE_STYLE = "coverage.style";

	public void setup(TrackConfig config) {
		ValueDomain layoutTypes = ValueDomain.createNewValueDomain(new String[] { "pileup", "coverage" });
		config.addConfig("Layout", new StringType(CONFIG_LAYOUT, layoutTypes), "pileup");
		config.addConfigBoolean("Show Labels", CONFIG_SHOW_LABELS, showLabels);
		config.addConfigInteger("Read Height", CONFIG_READ_HEIGHT, readHeight);
		config.addConfigInteger("Read Height (min)", CONFIG_MIN_READ_HEIGHT, minReadHeight);
		config.addConfigInteger("# of Reads to Cache", CONFIG_NUM_READ_MAX, numReadsMax);
		config.addConfigBoolean("Overlap Paired-End Reads", CONFIG_PE_OVERLAP, overlapPairedReads);
		config.addConfigBoolean("Show Base Quality", CONFIG_SHOW_BASE_QUALITY, showBaseQuality);

		config.addConfig("Coverage Display Style",
				new StringType(CONFIG_COVERAGE_STYLE, ValueDomain.createNewValueDomain(new String[] { "default", "smooth" })), coverageStyle);
	}

	public void loadConfig(TrackConfig config) {

		layout = config.getString(CONFIG_LAYOUT, layout);
		numReadsMax = config.getInt(CONFIG_NUM_READ_MAX, 500);
		showLabels = config.getBoolean(CONFIG_SHOW_LABELS, showLabels);
		readHeight = config.getInt(CONFIG_READ_HEIGHT, readHeight);
		minReadHeight = config.getInt(CONFIG_MIN_READ_HEIGHT, minReadHeight);
		coverageStyle = config.getString(CONFIG_COVERAGE_STYLE, coverageStyle);
		overlapPairedReads = config.getBoolean(CONFIG_PE_OVERLAP, overlapPairedReads);
		showBaseQuality = config.getBoolean(CONFIG_SHOW_BASE_QUALITY, showBaseQuality);

	}

	public Color getClippedReadColor(OnGenome g) {
		return toColor(getReadColorHex(g), clippedRegionAlpha);
	}

	public Color getPaddingColor() {
		return toColor(PADDING_COLOR);
	}

	private String getReadColorHex(OnGenome g) {
		if (useDifferentColorForForwardAndReverse) {
			if (g instanceof Interval) {
				Interval r = (Interval) g;
				if (r.getColor() != null)
					return r.getColor();

				return r.isSense() ? FORWARD_READ_COLOR : REVERSE_READ_COLOR;
			}
		}
		return DEFAULT_READ_COLOR;
	}

	public Color getReadColor(OnGenome g) {
		return toColor(getReadColorHex(g));
	}

	public Color getReadColor(OnGenome g, float alpha) {
		return toColor(getReadColorHex(g), alpha);
	}

	public static Color toColor(String hex) {
		int r = Integer.parseInt(hex.substring(1, 3), 16);
		int g = Integer.parseInt(hex.substring(3, 5), 16);
		int b = Integer.parseInt(hex.substring(5, 7), 16);
		return new Color(r, g, b);
	}

	public static Color toColor(String hex, float alpha) {
		int r = Integer.parseInt(hex.substring(1, 3), 16);
		int g = Integer.parseInt(hex.substring(3, 5), 16);
		int b = Integer.parseInt(hex.substring(5, 7), 16);
		return new Color(r, g, b, alpha);
	}

	public static ReadDisplayStyle defaultStyle() {
		return new ReadDisplayStyle();
	}

	public static ReadDisplayStyle highlightImproperMate() {
		ReadDisplayStyle s = new ReadDisplayStyle();
		s.useDifferentColorForForwardAndReverse = true;
		return s;
	}

}
