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
// ReadTrack.java
// Since: May 16, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.Locus;
import org.utgenome.gwt.utgb.client.bio.Read;
import org.utgenome.gwt.utgb.client.bio.ReadSet;
import org.utgenome.gwt.utgb.client.canvas.GWTGenomeCanvas;
import org.utgenome.gwt.utgb.client.canvas.LocusClickHandler;
import org.utgenome.gwt.utgb.client.db.datatype.BooleanType;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * Track for displaying read data
 * 
 * @author leo
 * 
 */
public class ReadTrack extends TrackBase {

	protected TrackConfig config = new TrackConfig(this);
	private String fileName;
	private boolean showLabels = true;
	private String clickURLtemplate = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=%q";
	private int leftMargin = 0;

	private ArrayList<Read> genes = new ArrayList<Read>();

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new ReadTrack();
			}
		};
	}

	public ReadTrack() {
		super("Read Track");

		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.setWidget(0, 1, geneCanvas);

		geneCanvas.setLocusClickHandler(new LocusClickHandler() {
			public void onClick(Locus locus) {
				String url = clickURLtemplate;
				if (url.contains("%q") && locus.getName() != null)
					url = url.replace("%q", locus.getName());
				Window.open(url, "locus", "");
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
		TrackWindow w = getTrackGroup().getTrackWindow();

		int s = w.getStartOnGenome();
		int e = w.getEndOnGenome();
		int width = w.getWindowWidth() - leftMargin;

		if (leftMargin > 0)
			layoutTable.getCellFormatter().setWidth(0, 0, leftMargin + "px");

		geneCanvas.clear();
		geneCanvas.setWindow(new TrackWindowImpl(width, s, e));
		//geneCanvas.setShowLabels(showLabels);
		//geneCanvas.draw(genes);

		getFrame().loadingDone();
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

		if (change.containsOneOf(new String[] { UTGBProperty.SPECIES, UTGBProperty.REVISION, UTGBProperty.TARGET })) {
			update(change.getTrackWindow());
		}
	}

	@Override
	public TrackConfig getConfig() {
		return config;
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		update(group.getTrackWindow());
		config.addConfigParameter("File Name", new StringType("fileName"), fileName);
		config.addConfigParameter("Show Labels", new BooleanType("showLabels"), Boolean.toString(showLabels));
	}

	class UpdateCommand implements Command {
		private final ReadSet readSet;

		public UpdateCommand(ReadSet readSet) {
			this.readSet = readSet;
		}

		public void execute() {
			genes.clear();
			genes.addAll(readSet.read);
			refresh();
		}
	}

	public void update(TrackWindow newWindow) {
		// retrieve gene data from the API
		int s = newWindow.getStartOnGenome();
		int e = newWindow.getEndOnGenome();
		TrackGroupProperty prop = getTrackGroup().getPropertyReader();
		//String species = prop.getProperty(UTGBProperty.SPECIES);
		String revision = prop.getProperty(UTGBProperty.REVISION);
		String target = prop.getProperty(UTGBProperty.TARGET);

		String dbID = "db";
		getFrame().setNowLoading();

		GenomeBrowser.getService().getReadSet(dbID, revision, new ChrLoc(target, s, e), new AsyncCallback<ReadSet>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve gene data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(ReadSet readSet) {
				DeferredCommand.addCommand(new UpdateCommand(readSet));
			}

		});

	}

	@Override
	public void saveProperties(Properties saveData) {
		saveData.add("fileName", fileName);
		saveData.add("clickURL", clickURLtemplate);
		saveData.add("leftMargin", leftMargin);
		saveData.add("showLabels", showLabels);
	}

	@Override
	public void restoreProperties(Properties properties) {
		fileName = properties.get("fileName", fileName);
		clickURLtemplate = properties.get("clickURL", clickURLtemplate);
		leftMargin = properties.getInt("leftMargin", leftMargin);
		showLabels = properties.getBoolean("showLabels", showLabels);
	}

}
