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
// $Author$ yoshimura
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.List;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas;
import org.utgenome.gwt.utgb.client.db.datatype.BooleanType;
import org.utgenome.gwt.utgb.client.db.datatype.FloatType;
import org.utgenome.gwt.utgb.client.db.datatype.IntegerType;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.track.impl.TrackWindowImpl;
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
 * @author leo
 * 
 */
public class WIGGraphCanvasTrack extends TrackBase {

	/**
	 * Configuration parameter names
	 */
	private final static String CONFIG_FILENAME = "fileName";
	private final static String CONFIG_TRACK_HEIGHT = "trackHeight";
	private final static String CONFIG_LEFT_MARGIN = "leftMargin";
	private final static String CONFIG_MAX_VALUE = "maxValue";
	private final static String CONFIG_MIN_VALUE = "minValue";
	private final static String CONFIG_AUTO_RANGE = "isAutoRange";
	private final static String CONFIG_LOG_SCALE = "isLog";
	private final static String CONFIG_SHOW_ZERO_VALUE = "showZero";
	private final static String CONFIG_DRAW_SCALE = "drawScale";
	private final static String CONFIG_SHOW_SCALE_LABEL = "showScaleLabel";
	private final static String CONFIG_COLOR = "color";
	private final static String CONFIG_ALPHA = "alpha";

	private final String DEFAULT_COLOR = "rgba(12,106,193,0.7)";

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

		layoutTable.setWidget(0, 1, geneCanvas);

	}

	private final FlexTable layoutTable = new FlexTable();
	private final GWTGraphCanvas geneCanvas = new GWTGraphCanvas();

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
		TrackConfig config = getConfig();
		config.addConfigParameter("Path", new StringType(CONFIG_FILENAME));
		config.addConfigParameter("Y Max", new FloatType(CONFIG_MAX_VALUE), "100");
		config.addConfigParameter("Y Min", new FloatType(CONFIG_MIN_VALUE), "0");
		config.addConfigParameter("Auto Scale", new BooleanType(CONFIG_AUTO_RANGE), "false");
		config.addConfigParameter("Log Scale", new BooleanType(CONFIG_LOG_SCALE), "false");
		config.addConfigParameter("Graph Color", new StringType(CONFIG_COLOR), "");
		config.addConfigParameter("Show Zero Value", new BooleanType(CONFIG_SHOW_ZERO_VALUE), "false");
		config.addConfigParameter("Draw Scale", new BooleanType(CONFIG_DRAW_SCALE), "true");
		config.addConfigParameter("Show Scale Label", new BooleanType(CONFIG_SHOW_SCALE_LABEL), "true");
		config.addConfigParameter("Pixel Height", new IntegerType(CONFIG_TRACK_HEIGHT), "100");

		update(group.getTrackWindow());
	}

	@Override
	public void draw() {
		TrackWindow w = getTrackGroup().getTrackWindow();
		TrackConfig config = getConfig();

		float tempMinValue = config.getFloat(CONFIG_MIN_VALUE, 0f);
		float tempMaxValue = config.getFloat(CONFIG_MAX_VALUE, 20.0f);

		// get graph x-range
		int s = w.getStartOnGenome();
		int e = w.getEndOnGenome();
		int width = w.getPixelWidth();

		int leftMargin = config.getInt(CONFIG_LEFT_MARGIN, 0);
		layoutTable.getCellFormatter().setWidth(0, 0, leftMargin + "px");

		int trackHeight = config.getInt(CONFIG_TRACK_HEIGHT, 100);
		boolean isLog = config.getBoolean(CONFIG_LOG_SCALE, false);
		geneCanvas.clear();
		geneCanvas.setTrackWindow(new TrackWindowImpl(width - leftMargin, s, e));
		geneCanvas.setWindowHeight(trackHeight);
		geneCanvas.setIsLog(isLog);

		// get graph y-range
		boolean isAutoRange = config.getBoolean(CONFIG_AUTO_RANGE, false);
		if (isAutoRange) {
			tempMinValue = 0.0f;
			tempMaxValue = 0.0f;
			for (CompactWIGData data : wigDataList) {
				tempMinValue = Math.min(tempMinValue, data.getMinValue());
				tempMaxValue = Math.max(tempMaxValue, data.getMaxValue());
			}
			GWT.log("range:" + tempMinValue + "-" + tempMaxValue, null);
		}

		geneCanvas.setMinValue(tempMinValue);
		geneCanvas.setMaxValue(tempMaxValue);
		boolean showZero = config.getBoolean(CONFIG_SHOW_ZERO_VALUE, false);
		geneCanvas.setShowZeroValue(showZero);

		// set color
		setColor(config.getString(CONFIG_COLOR, ""));

		// draw frame
		boolean drawScale = config.getBoolean(CONFIG_DRAW_SCALE, true);
		if (drawScale)
			geneCanvas.drawFrame();
		if (config.getBoolean(CONFIG_SHOW_SCALE_LABEL, true))
			geneCanvas.drawScaleLabel();

		// draw data graph
		if (wigDataList != null) {
			for (CompactWIGData data : wigDataList) {

				Color graphColor = new Color(DEFAULT_COLOR);
				if (color.isDefined()) {
					graphColor = new Color(color.get());
				}
				else if (data.getTrack().containsKey("color")) {
					String colorStr = data.getTrack().get("color");
					String c[] = colorStr.split(",");
					if (c.length == 3)
						graphColor = new Color(Integer.valueOf(c[0]), Integer.valueOf(c[1]), Integer.valueOf(c[2]));
				}

				geneCanvas.drawWigGraph(data, graphColor);
			}

		}
		getFrame().loadingDone();

	}

	public void update(TrackWindow newWindow) {
		// retrieve gene data from the API

		int s = newWindow.getStartOnGenome();
		int e = newWindow.getEndOnGenome();
		TrackGroupProperty prop = getTrackGroup().getPropertyReader();
		String target = prop.getProperty(UTGBProperty.TARGET);
		ChrLoc l = new ChrLoc();
		l.start = s < e ? s : e;
		l.end = s > e ? s : e;
		l.chr = target;

		getFrame().setNowLoading();
		TrackConfig config = getConfig();
		String fileName = config.getString(CONFIG_FILENAME, "");
		int leftMargin = config.getInt(CONFIG_LEFT_MARGIN, 0);

		final TrackWindow nw = newWindow.newPixelWidthWindow(newWindow.getPixelWidth() - leftMargin);
		refresh();

		getBrowserService().getCompactWigDataList(fileName, nw.getPixelWidth(), l, new AsyncCallback<List<CompactWIGData>>() {
			TrackConfig config = getConfig();

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
		boolean isUpdate = false;

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
