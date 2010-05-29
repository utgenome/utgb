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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;

public class WIGGraphCanvasTrack extends TrackBase {

	private final String DEFAULT_COLOR = "rgba(12,106,193,0.7)";

	protected String fileName = "db/sample.wig.sqlite";
	private Optional<String> color = new Optional<String>();

	private float alpha = 1.0f;
	private float maxValue = 20.0f;
	private float minValue = 0.0f;
	private boolean isAutoRange = false;
	private boolean isLog = false;

	private int height = 100;
	private int leftMargin = 100;

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

	public static int calcXPositionOnWindow(long indexOnGenome, long startIndexOnGenome, long endIndexOnGenome, int windowWidth) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) windowWidth;
		double v2 = v / (endIndexOnGenome - startIndexOnGenome);
		return (int) v2;
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
		config.addConfigParameter("File Name", new StringType("fileName"), fileName);
		config.addConfigParameter("maxValue", new FloatType("maxValue"), String.valueOf(maxValue));
		config.addConfigParameter("minValue", new FloatType("minValue"), String.valueOf(minValue));
		config.addConfigParameter("Auto Read", new BooleanType("isAutoRange"), String.valueOf(isAutoRange));
		config.addConfigParameter("Log Scale", new BooleanType("isLog"), String.valueOf(isLog));
		config.addConfigParameter("Color", new StringType("color"), "");

		update(group.getTrackWindow());
	}

	class UpdateCommand implements Command {
		private final List<CompactWIGData> dataList;

		public UpdateCommand(List<CompactWIGData> dataList) {
			this.dataList = dataList;
		}

		public void execute() {
			TrackWindow w = getTrackGroup().getTrackWindow();

			float tempMinValue = minValue;
			float tempMaxValue = maxValue;

			// get graph x-range
			int s = w.getStartOnGenome();
			int e = w.getEndOnGenome();
			int width = w.getWindowWidth();

			layoutTable.getCellFormatter().setWidth(0, 0, leftMargin + "px");

			geneCanvas.clear();
			geneCanvas.setTrackWindow(new TrackWindowImpl(width - leftMargin, s, e));
			geneCanvas.setWindowHeight(height);
			geneCanvas.setIsLog(isLog);

			// get graph y-range
			if (isAutoRange) {
				tempMinValue = 0.0f;
				tempMaxValue = 0.0f;
				for (CompactWIGData data : dataList) {
					tempMinValue = Math.min(tempMinValue, data.getMinValue());
					tempMaxValue = Math.max(tempMaxValue, data.getMaxValue());
				}
				GWT.log("range:" + tempMinValue + "-" + tempMaxValue, null);
			}

			geneCanvas.setMinValue(tempMinValue);
			geneCanvas.setMaxValue(tempMaxValue);

			// draw frame
			geneCanvas.drawFrame();

			// draw data graph
			if (dataList != null) {
				for (CompactWIGData data : dataList) {

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
			refresh();
			getFrame().loadingDone();
		}

		private int getLabelWidth(Label nameLabel, AbsolutePanel labelPanel) {
			int nameLabelTop = labelPanel.getWidgetTop(nameLabel);
			int nameLabelBottom = nameLabelTop + nameLabel.getOffsetHeight();
			int limit = Integer.MAX_VALUE;

			for (int i = 0; i < labelPanel.getWidgetCount(); i++) {
				Widget w = labelPanel.getWidget(i);
				if (!labelPanel.getWidget(i).equals(nameLabel) && labelPanel.getWidgetTop(w) < nameLabelBottom
						&& labelPanel.getWidgetTop(w) + w.getOffsetHeight() > nameLabelTop) {
					limit = Math.min(limit, labelPanel.getWidgetLeft(w));
				}
			}

			if (limit > leftMargin)
				limit = leftMargin;

			return limit;
		}
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

		getBrowserService().getCompactWigDataList(fileName, newWindow.getWindowWidth(), l, new AsyncCallback<List<CompactWIGData>>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve wig data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<CompactWIGData> dataList) {
				wigDataList = dataList;
				//				DeferredCommand.addCommand(new UpdateCommand(dataList));
				new UpdateCommand(dataList).execute();
			}
		});
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {
		boolean isUpdate = false;

		if (change.contains("fileName")) {
			fileName = change.getValue("fileName");
			isUpdate = true;
		}

		if (change.contains("maxValue")) {
			maxValue = change.getFloatValue("maxValue");
			GWT.log("max:" + maxValue, null);
		}
		if (change.contains("minValue")) {
			minValue = change.getFloatValue("minValue");
			GWT.log("min:" + minValue, null);
		}
		if (change.contains("isAutoRange")) {
			isAutoRange = change.getBoolValue("isAutoRange");
			GWT.log("auto range:" + isAutoRange, null);
		}
		if (change.contains("isLog")) {
			isLog = change.getBoolValue("isLog");
			GWT.log("log:" + isLog, null);
		}
		if (change.contains("color")) {
			setColor(change.getValue("color"));
			GWT.log("color:" + color, null);
		}
		if (change.contains("alpha")) {
			alpha = change.getFloatValue("alpha");
			GWT.log("alpha:" + alpha, null);
		}

		if (isUpdate) {
			update(getTrackWindow());
		}
		else {
			getFrame().setNowLoading();
			DeferredCommand.addCommand(new UpdateCommand(wigDataList));
		}
	}

	@Override
	public void restoreProperties(Properties properties) {
		super.restoreProperties(properties);
		fileName = properties.get("fileName", fileName);
		height = properties.getInt("trackHeight", height);
		leftMargin = properties.getInt("leftMargin", leftMargin);
		maxValue = properties.getFloat("maxValue", maxValue);
		minValue = properties.getFloat("minValue", minValue);
		isAutoRange = properties.getBoolean("isAutoRange", isAutoRange);
		isLog = properties.getBoolean("isLog", isLog);

		alpha = properties.getFloat("alpha", alpha);
		String c = properties.get("color");
		setColor(c);

		String p = properties.get("changeParamOnClick");
		if (p != null) {
			// set canvas action

		}
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
		return new Color(r_value, g_value, b_value, alpha);
	}

}
