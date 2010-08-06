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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas;
import org.utgenome.gwt.utgb.client.canvas.TrackWindowChain;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas.GraphStyle;
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * WIG Track
 * 
 * @author yoshimura
 * @author leo
 * 
 */
public class WIGGraphCanvasTrack extends TrackBase {

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

	private TrackWindowChain chain = new TrackWindowChain();

	public Widget getWidget() {
		return layoutTable;
	}

	private GraphStyle style = new GraphStyle();

	/**
	 * Configuration parameter names
	 */
	private final static String CONFIG_PATH = "path";

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {

		graphCanvas.setTrackGroup(group);
		TrackConfig config = getConfig();
		config.addConfigString("Path", CONFIG_PATH, "");
		style.setup(config);
	}

	@Override
	public void onChangeTrackHeight(int newHeight) {
		//		// set graph height
		//		int height = getFrame().getFrameHeight();
		//		if (height > 16 && height != style.windowHeight) {
		//			getConfig().setParameter(GraphStyle.CONFIG_TRACK_HEIGHT, Integer.toString(height));
		//		}
		//
		//		refresh();
	}

	private void prepare() {

		// load style parameters from the configuration panel
		style.load(getConfig());
		// set the graph style
		graphCanvas.setStyle(style);

		graphCanvas.clearScale();
		//		if (!style.autoScale) {
		graphCanvas.drawFrame(null);
		graphCanvas.drawScaleLabel();
		//		}

	}

	@Override
	public void draw() {

		final TrackWindow newWindow = getTrackWindow();
		graphCanvas.setTrackWindow(newWindow);

		prepare();

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

		// load graph 
		for (TrackWindow each : windowToCreate) {
			loadGraph(each);
		}
		if (windowToCreate.isEmpty()) {
			getFrame().loadingDone();
		}

		// clear the remaining windows out of the global view
		for (TrackWindow each : updateInfo.windowToDiscard) {
			graphCanvas.clear(each);
		}

	}

	@Override
	public void beforeChangeTrackWindow(TrackWindow newWindow) {
		graphCanvas.setTrackWindow(newWindow);
	}

	public void loadGraph(final TrackWindow queryWindow) {

		getFrame().setNowLoading();
		String fileName = resolvePropertyValues(getConfig().getString(CONFIG_PATH, ""));

		int s = queryWindow.getStartOnGenome();
		int e = queryWindow.getEndOnGenome();
		ChrLoc l = new ChrLoc(getTrackGroupProperty(UTGBProperty.TARGET), s, e);
		getBrowserService().getCompactWigDataList(fileName, queryWindow.getPixelWidth(), l, new AsyncCallback<List<CompactWIGData>>() {
			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve wig data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<CompactWIGData> dataList) {
				graphCanvas.drawWigGraph(dataList, queryWindow);
				getFrame().loadingDone();
				refresh();
			}
		});

	}

	public void clearCanvas() {
		graphCanvas.clear();
		chain.clear();
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.contains(CONFIG_PATH)) {
			clearCanvas();
			refresh();
		}
		else {
			prepare();
			graphCanvas.redrawWigGraph();
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
			clearCanvas();
			refresh();
		}
	}

}
