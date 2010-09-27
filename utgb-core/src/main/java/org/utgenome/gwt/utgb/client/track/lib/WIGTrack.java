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
// WIGTrack.java
// Since: 2010/09/27
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.canvas.BarGraphCanvas;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas.GraphStyle;
import org.utgenome.gwt.utgb.client.canvas.TrackWindowChain;
import org.utgenome.gwt.utgb.client.canvas.TrackWindowChain.WindowUpdateInfo;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * WIG data track
 * 
 * @author leo
 * 
 */
public class WIGTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new WIGTrack();
			}
		};
	}

	private final AbsolutePanel panel = new AbsolutePanel();
	private final FlexTable layoutTable = new FlexTable();

	private TrackWindowChain chain = new TrackWindowChain();
	private final List<List<BarGraphCanvas>> buffer = new ArrayList<List<BarGraphCanvas>>(2);
	private int frontBuffer = 0; // 0 or 1

	private GraphStyle style = new GraphStyle();

	{
		buffer.add(new ArrayList<BarGraphCanvas>());
		buffer.add(new ArrayList<BarGraphCanvas>());
	}

	public WIGTrack() {
		super("WIG Track");
		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		Style.margin(layoutTable, 0);
		Style.padding(layoutTable, 0);
		Style.fullSize(layoutTable);
		layoutTable.setWidget(0, 0, panel);
	}

	public Widget getWidget() {
		return layoutTable;
	}

	/**
	 * Configuration parameter names
	 */
	private final static String CONFIG_PATH = "path";

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		TrackConfig config = getConfig();
		config.addConfigString("Path", CONFIG_PATH, "");
		style.setup(config);
	}

	@Override
	public void onChangeTrackHeight(int newHeight) {
		style.windowHeight = newHeight;
		refresh();
	}

	private boolean needToUpdateStyle = true;

	private void updateStyle() {
		// load style parameters from the configuration panel

		// set the graph style
		//graphCanvas.setStyle(style);

		needToUpdateStyle = false;
	}

	@Override
	public void draw() {

		if (needToUpdateStyle) {
			updateStyle();
		}

		final TrackWindow newWindow = getTrackWindow();
		panel.setPixelSize(newWindow.getPixelWidth(), style.windowHeight);

		// swap the graph buffer when we have to scale the graphs
		if (chain.getViewWindow() != null && !chain.getViewWindow().hasSameScaleWith(newWindow)) {

			// scale the currently displayed bar graph
			for (BarGraphCanvas each : buffer.get(frontBuffer)) {
				each.setTrackWindow(newWindow);
			}

			// switch the front buffer
			frontBuffer = (frontBuffer + 1) % 2;
			for (Widget each : buffer.get(frontBuffer)) {
				each.removeFromParent();
			}
			buffer.get(frontBuffer).clear();
		}

		WindowUpdateInfo updateInfo = chain.setViewWindow(newWindow);
		List<TrackWindow> windowToCreate = updateInfo.windowToCreate;
		// sort the windows in the nearest neighbor order from the view window 
		Collections.sort(windowToCreate, new Comparator<TrackWindow>() {
			public int compare(TrackWindow o1, TrackWindow o2) {
				int d1 = Math.abs(o1.center() - newWindow.center());
				int d2 = Math.abs(o2.center() - newWindow.center());
				return d1 - d2;
			}
		});

		// move the graph canvases
		for (BarGraphCanvas each : buffer.get(frontBuffer)) {
			int x = newWindow.convertToPixelX(each.getTrackWindow().getStartOnGenome());
			panel.setWidgetPosition(each, x, 0);
		}

		// load graph
		String filePath = resolvePropertyValues(getConfig().getString(CONFIG_PATH, ""));
		List<BarGraphCanvas> front = buffer.get(frontBuffer);
		for (final TrackWindow queryWindow : windowToCreate) {
			final BarGraphCanvas graph = new BarGraphCanvas(queryWindow, style.windowHeight);
			int x = newWindow.convertToPixelX(queryWindow.getStartOnGenome());
			panel.add(graph, x, 0);
			front.add(graph);

			int s = queryWindow.getStartOnGenome();
			int e = queryWindow.getEndOnGenome();
			ChrLoc l = new ChrLoc(getTrackGroupProperty(UTGBProperty.TARGET), s, e);
			getBrowserService().getCompactWigDataList(filePath, queryWindow.getPixelWidth(), l, new AsyncCallback<List<CompactWIGData>>() {
				public void onFailure(Throwable e) {
					error("failed to retrieve wig data: " + e.getMessage());
					clearBackgroundGraph(queryWindow);
				}

				public void onSuccess(List<CompactWIGData> dataList) {
					for (CompactWIGData each : dataList) {
						graph.draw(each, style);
					}
					clearBackgroundGraph(queryWindow);
				}
			});

		}

	}

	void clearBackgroundGraph(TrackWindow window) {
		List<BarGraphCanvas> background = buffer.get((frontBuffer + 1) % 2);
		for (BarGraphCanvas each : background) {
			if (each.getTrackWindow().overlapWith(window)) {
				each.removeFromParent();
				each.clear();
			}
		}

	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.contains(CONFIG_PATH)) {
			needToUpdateStyle = true;
			refresh();
		}
		else {
			needToUpdateStyle = true;
			refresh();
		}
	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {
		refresh();
	}

	@Override
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {

		if (change.containsOneOf(new String[] { UTGBProperty.TARGET })) {
			refresh();
		}
	}

}
