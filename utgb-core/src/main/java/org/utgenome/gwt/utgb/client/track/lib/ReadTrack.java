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

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.GenomeDB;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.OnGenomeDataSet;
import org.utgenome.gwt.utgb.client.bio.GenomeDB.DBType;
import org.utgenome.gwt.utgb.client.canvas.GWTGenomeCanvas;
import org.utgenome.gwt.utgb.client.canvas.LocusClickHandler;
import org.utgenome.gwt.utgb.client.db.ValueDomain;
import org.utgenome.gwt.utgb.client.db.datatype.BooleanType;
import org.utgenome.gwt.utgb.client.db.datatype.StringType;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.track.impl.TrackWindowImpl;
import org.utgenome.gwt.utgb.client.util.BrowserInfo;
import org.utgenome.gwt.utgb.client.util.Properties;

import com.google.gwt.core.client.GWT;
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

	// track configuration parameters
	private boolean showLabels = true;
	private String clickAction = "link";
	private String clickURLtemplate = "http://www.google.com/search?q=%q";
	private int leftMargin = 0;
	private String path;
	private String dbType;

	private ArrayList<OnGenome> onGenomeData = new ArrayList<OnGenome>();

	// widgets
	private FlexTable layoutTable = new FlexTable();
	private GWTGenomeCanvas geneCanvas = new GWTGenomeCanvas();

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new ReadTrack();
			}
		};
	}

	public ReadTrack() {
		this("Read Track", "AUTO");

	}

	public ReadTrack(String trackName, String dbType) {
		super("Read Track");
		this.dbType = dbType;

		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.setWidget(0, 1, geneCanvas);

		updateClickAction();
	}

	private void updateClickAction() {

		if ("none".equals(clickAction)) {
			geneCanvas.setLocusClickHandler(null);
		}
		else if ("link".equals(clickAction)) {
			geneCanvas.setLocusClickHandler(new LocusClickHandler() {
				public void onClick(int x, int y, OnGenome locus) {
					String url = clickURLtemplate;
					if (url.contains("%q") && locus.getName() != null)
						url = url.replace("%q", locus.getName());
					Window.open(url, "locus", "");
				}
			});
		}
		else if ("info".equals(clickAction)) {
			geneCanvas.setLocusClickHandler(new LocusClickHandler() {
				public void onClick(int x, int y, OnGenome locus) {
					geneCanvas.displayInfo(x, y, locus);
				}
			});
		}

	}

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
		geneCanvas.setShowLabels(showLabels);
		geneCanvas.draw(onGenomeData);

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
		config.addConfigParameter("DB Path", new StringType("path"), path);

		ValueDomain dbTypes = ValueDomain.createNewValueDomain(DBType.getDBTypeList());
		config.addConfigParameter("DB Type", new StringType("dbType", dbTypes), dbType);

		config.addConfigParameter("Show Labels", new BooleanType("showLabels"), Boolean.toString(showLabels));
		ValueDomain actionTypes = ValueDomain.createNewValueDomain(new String[] { "none", "link", "info" });
		config.addConfigParameter("On Click Action", new StringType("onclick.action", actionTypes), clickAction);
		config.addConfigParameter("On Click URL", new StringType("onclick.url"), clickURLtemplate);

	}

	public void update(TrackWindow newWindow) {
		// retrieve gene data from the API
		int s = newWindow.getStartOnGenome();
		int e = newWindow.getEndOnGenome();
		String chr = getTrackGroupProperty(UTGBProperty.TARGET);

		getFrame().setNowLoading();

		getBrowserService().getOnGenomeData(getGenomeDB(), new ChrLoc(chr, s, e), BrowserInfo.getUserAgent(), newWindow.getWindowWidth(),
				new AsyncCallback<OnGenomeDataSet>() {

					public void onFailure(Throwable e) {
						GWT.log("failed to retrieve gene data", e);
						getFrame().loadingDone();
					}

					public void onSuccess(OnGenomeDataSet readSet) {
						onGenomeData.clear();
						onGenomeData.addAll(readSet.read);

						refresh();
					}

				});

	}

	public String getPath() {
		return path;
	}

	/**
	 * Override this method to extend db info parameters
	 * 
	 * @return
	 */
	public GenomeDB getGenomeDB() {
		String ref = getTrackGroupProperty(UTGBProperty.REVISION);
		return new GenomeDB(DBType.valueOf(DBType.class, dbType), path, ref);
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.contains("onclick.url")) {
			clickURLtemplate = change.getValue("onclick.url");
		}

		if (change.contains("onclick.action")) {
			clickAction = change.getValue("onclick.action");
		}

		if (change.contains("showLabels")) {
			showLabels = change.getBoolValue("showLabels");
			refresh();
		}

		updateClickAction();
	}

	@Override
	public void saveProperties(Properties saveData) {
		saveData.add("leftMargin", leftMargin);
		saveData.add("showLabels", showLabels);
		saveData.add("onclick.url", clickURLtemplate);
		saveData.add("onclick.action", clickAction);
		saveData.add("path", path);
		saveData.add("dbType", path);
	}

	@Override
	public void restoreProperties(Properties properties) {
		leftMargin = properties.getInt("leftMargin", leftMargin);
		showLabels = properties.getBoolean("showLabels", showLabels);
		clickURLtemplate = properties.get("onclick.url", clickURLtemplate);
		clickAction = properties.get("onclick.action", clickAction);
		path = properties.get("path", path);
		dbType = properties.get("dbType", dbType);

		updateClickAction();
	}

}
