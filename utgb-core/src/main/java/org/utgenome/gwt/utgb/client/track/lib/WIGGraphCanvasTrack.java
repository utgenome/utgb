/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// WIGGraphCanvasTrack.java
// Since: Dec. 8, 2009
//
// $URL$ 
// $Author$ 
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.List;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas.GraphStyle;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.util.Optional;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;

/**
 * WIG Track
 * 
 * @author yoshimura
 * @author leo
 * 
 */
public class WIGGraphCanvasTrack extends TrackBase {

	/**
	 * Configuration parameter names
	 */
	private final static String CONFIG_FILENAME = "fileName";
	private final static String CONFIG_TRACK_HEIGHT = "trackHeight";
	private final static String CONFIG_MAX_VALUE = "maxValue";
	private final static String CONFIG_MIN_VALUE = "minValue";
	private final static String CONFIG_AUTO_RANGE = "isAutoRange";
	private final static String CONFIG_LOG_SCALE = "isLog";
	private final static String CONFIG_SHOW_ZERO_VALUE = "showZero";
	private final static String CONFIG_DRAW_SCALE = "drawScale";
	private final static String CONFIG_SHOW_SCALE_LABEL = "showScaleLabel";
	private final static String CONFIG_COLOR = "color";

	private Optional<String> color = new Optional<String>();
	private List<CompactWIGData> wigDataList;

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new WIGGraphCanvasTrack();
			}
		};
	}

	public WIGGraphCanvasTrack() {
		super("WIG Graph Canvas");

		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		Style.margin(layoutTable, 0);
		Style.padding(layoutTable, 0);

		layoutTable.setWidget(0, 1, graphCanvas);

	}

	private final FlexTable layoutTable = new FlexTable();
	private final GWTGraphCanvas graphCanvas = new GWTGraphCanvas();

	public Widget getWidget() {
		return layoutTable;
	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {
		update(newWindow);
	}

	@Override
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {

		if (change.containsOneOf(new String[] { UTGBProperty.TARGET })) {
			update(change.getTrackWindow());
		}
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {

		graphCanvas.setTrackGroup(group);

		TrackConfig config = getConfig();
		config.addConfigString("Path", CONFIG_FILENAME, "");
		config.addConfigDouble("Y Max", CONFIG_MAX_VALUE, 100);
		config.addConfigDouble("Y Min", CONFIG_MIN_VALUE, 0);
		config.addConfigBoolean("Auto Scale", CONFIG_AUTO_RANGE, false);
		config.addConfigBoolean("Log Scale", CONFIG_LOG_SCALE, false);
		config.addConfigString("Graph Color", CONFIG_COLOR, "");
		config.addConfigBoolean("Show Zero Value", CONFIG_SHOW_ZERO_VALUE, false);
		config.addConfigBoolean("Draw Scale", CONFIG_DRAW_SCALE, true);
		config.addConfigBoolean("Show Scale Label", CONFIG_SHOW_SCALE_LABEL, true);
		config.addConfigInteger("Pixel Height", CONFIG_TRACK_HEIGHT, 100);

		update(group.getTrackWindow());
	}

	@Override
	public void draw() {
		TrackConfig config = getConfig();

		GraphStyle style = new GraphStyle();
		style.minValue = config.getFloat(CONFIG_MIN_VALUE, 0f);
		style.maxValue = config.getFloat(CONFIG_MAX_VALUE, 20.0f);
		style.windowHeight = config.getInt(CONFIG_TRACK_HEIGHT, 100);
		style.logScale = config.getBoolean(CONFIG_LOG_SCALE, false);
		style.autoScale = config.getBoolean(CONFIG_AUTO_RANGE, false);
		style.drawZeroValue = config.getBoolean(CONFIG_SHOW_ZERO_VALUE, false);
		style.drawScale = config.getBoolean(CONFIG_DRAW_SCALE, true);
		style.showScaleLabel = config.getBoolean(CONFIG_SHOW_SCALE_LABEL, true);
		graphCanvas.clear();
		graphCanvas.setStyle(style);

		// set color
		setColor(config.getString(CONFIG_COLOR, ""));

		// draw data graph
		if (wigDataList != null) {
			graphCanvas.drawWigGraph(wigDataList, color);
		}
		getFrame().loadingDone();

	}

	public void update(TrackWindow newWindow) {
		// retrieve gene data from the API

		TrackWindow prevWindow = graphCanvas.getTrackWindow();
		TrackWindow queryWindow = newWindow;
		//		if (newWindow.hasSameScale(prevWindow)) {
		//			queryWindow = newWindow.mask(prevWindow);
		//		}

		int s = queryWindow.getStartOnGenome();
		int e = queryWindow.getEndOnGenome();
		ChrLoc l = new ChrLoc(getTrackGroupProperty(UTGBProperty.TARGET), s, e);

		getFrame().setNowLoading();
		TrackConfig config = getConfig();
		String fileName = config.getString(CONFIG_FILENAME, "");

		graphCanvas.setTrackWindow(newWindow);

		getBrowserService().getCompactWigDataList(fileName, newWindow.getPixelWidth(), l, new AsyncCallback<List<CompactWIGData>>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve wig data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<CompactWIGData> dataList) {
				wigDataList = dataList;
				refresh();
			}
		});
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.contains(CONFIG_FILENAME)) {
			update(getTrackWindow());
		}
		else {
			getFrame().setNowLoading();
			refresh();
		}
	}

	@Override
	public void restoreProperties(Properties properties) {
		super.restoreProperties(properties);

		String c = properties.get("color");
		setColor(c);
	}

	private void setColor(String colorStr) {
		if (colorStr != null && colorStr.length() > 0)
			color.set(colorStr);
		else
			color.reset();
	}

	public Color hexValue2Color(String hex) {
		int r_value = Integer.parseInt(hex.substring(1, 3), 16);
		int g_value = Integer.parseInt(hex.substring(3, 5), 16);
		int b_value = Integer.parseInt(hex.substring(5, 7), 16);
		return new Color(r_value, g_value, b_value);
	}

}
