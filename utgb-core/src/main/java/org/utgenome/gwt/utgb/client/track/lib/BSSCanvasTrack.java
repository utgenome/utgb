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
// BSSCanvasTrack.java
// Since: Oct 14, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.List;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.canvas.GWTGenomeCanvas;
import org.utgenome.gwt.utgb.client.canvas.LocusClickHandler;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.track.impl.TrackWindowImpl;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class BSSCanvasTrack extends TrackBase {

	//private ArrayList<Locus> genes = new ArrayList<Locus>();

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new BSSCanvasTrack();
			}
		};
	}

	public BSSCanvasTrack() {
		super("BSS Canvas");

		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.getCellFormatter().setWidth(0, 0, "100px");
		layoutTable.setWidget(0, 1, geneCanvas);

		//layoutTable.setHeight(300 + "px");

		//CSS.border(geneCanvas, 2, "solid", "cyan");

		geneCanvas.setLocusClickHandler(new LocusClickHandler() {
			public void onClick(Interval locus) {
				getTrackGroup().getPropertyWriter().setProperty("bss.query", locus.getName());
			}
		});

	}

	private FlexTable layoutTable = new FlexTable();
	private GWTGenomeCanvas geneCanvas = new GWTGenomeCanvas();

	public Widget getWidget() {
		return layoutTable;
	}

	@Override
	public void draw() {

	}

	public static int calcXPositionOnWindow(long indexOnGenome, long startIndexOnGenome, long endIndexOnGenome, int windowWidth) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) windowWidth;
		double v2 = v / (double) (endIndexOnGenome - startIndexOnGenome);
		return (int) v2;
	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {

		update(newWindow);
	}

	@Override
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {

		if (change.containsOneOf(new String[] { UTGBProperty.SPECIES, UTGBProperty.REVISION, UTGBProperty.TARGET, "dbGroup", "dbName" })) {
			update(change.getTrackWindow());
		}
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		update(group.getTrackWindow());
	}

	class UpdateCommand implements Command {
		private final List<Interval> geneList;

		public UpdateCommand(List<Interval> geneList) {
			this.geneList = geneList;
		}

		public void execute() {
			TrackWindow w = getTrackGroup().getTrackWindow();

			int s = w.getStartOnGenome();
			int e = w.getEndOnGenome();
			int width = w.getWindowWidth() - 100;

			geneCanvas.clear();
			geneCanvas.setWindow(new TrackWindowImpl(width, s, e));
			geneCanvas.drawInterval(geneList);

			refresh();

			getFrame().loadingDone();
		}
	}

	public void update(TrackWindow newWindow) {
		// retrieve gene data from the API
		int s = newWindow.getStartOnGenome();
		int e = newWindow.getEndOnGenome();
		TrackGroupProperty prop = getTrackGroup().getPropertyReader();
		String dbGroup = prop.getProperty("dbGroup");
		String dbName = prop.getProperty("dbName");
		String target = prop.getProperty(UTGBProperty.TARGET);
		ChrLoc l = new ChrLoc();
		l.start = s;
		l.end = e;
		l.chr = target;

		getFrame().setNowLoading();

		getBrowserService().getLocusList(dbGroup, dbName, l, new AsyncCallback<List<Interval>>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve gene data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<Interval> geneList) {
				DeferredCommand.addCommand(new UpdateCommand(geneList));
			}
		});

	}

	@Override
	public void saveProperties(Properties saveData) {

	}

	@Override
	public void restoreProperties(Properties properties) {
		String p = properties.get("changeParamOnClick");
		if (p != null) {
			// set canvas action
		}
	}
}
