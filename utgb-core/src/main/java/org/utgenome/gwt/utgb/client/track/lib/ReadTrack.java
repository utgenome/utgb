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

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.GenomeDB;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.OnGenomeDataSet;
import org.utgenome.gwt.utgb.client.bio.ReadQueryConfig;
import org.utgenome.gwt.utgb.client.bio.GenomeDB.DBType;
import org.utgenome.gwt.utgb.client.bio.ReadQueryConfig.Layout;
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
 * Track for displaying read data.
 * 
 * <h3>View Example</h3>
 * 
 * <pre>
 * -track
 *  -class: ReadTrack
 *  -path: (database path. e.g, db/imported/hg19/myread.bed)
 *  -properties
 *    -onclick.url: http://www.google.com/search?q=%q
 *    -onclick.action: (none|link|info|set)
 *    -showLabels: (true|false)
 *    -onclick.p.key: current.read
 *    -onclick.p.value: %q
 * </pre>
 * 
 * <p>
 * You can customize the behavior of the browser when clicking a read using <i>onclick.action</i> parameter:
 * </p>
 * 
 * <h3>On-Click Action Types</h3>
 * <table>
 * <tr>
 * <th>Type</th>
 * <th>Action</th>
 * </tr>
 * <tr>
 * <td>none</td>
 * <td>Disables click action</td>
 * </tr>
 * <tr>
 * <td>link</td>
 * <td>Opens an URL specified in <i>onclick.url</i> parameter.</td>
 * </tr>
 * <tr>
 * <td>info</td>
 * <td>Displays the detailed read information.</td>
 * </tr>
 * <tr>
 * <td>set</td>
 * <td>Sets a track group property (specified by <i>onclick.p.key</i>) using the value <i>onclick.p.value</i>.</td>
 * </tr>
 * </table>
 * 
 * <p>
 * You can embed track or track group parameters in <i>onclick.url</i> and <i>target.value</i>:
 * </p>
 * 
 * <h3>Parameters for On-Click event</h3>
 * <table>
 * <tr>
 * <th>pattern</th>
 * <th>to be replaced with</th>
 * </tr>
 * <tr>
 * <td>%q</td>
 * <td>Clicked read name</td>
 * </tr>
 * <tr>
 * <td>%start</td>
 * <td>Start position of the clicked read</td>
 * </tr>
 * <tr>
 * <td>%end</td>
 * <td>End position of the clicked read</td>
 * </tr>
 * <tr>
 * <td>%length</td>
 * <td>Length of the clicked read</td>
 * </tr>
 * <tr>
 * <td>%species</td>
 * <td>Species name</td>
 * </tr>
 * <tr>
 * <td>%ref</td>
 * <td>Reference sequence name</td>
 * </tr>
 * <tr>
 * <td>%chr</td>
 * <td>Chromosome/contig/scaffold name</td>
 * </tr>
 * <tr>
 * <td>%rstart</td>
 * <td>Start position on the genome (inclusive)</td>
 * </tr>
 * <tr>
 * <td>%rend</td>
 * <td>End position on the genome (exclusive)</td>
 * </tr>
 * <tr>
 * <td>%rlength</td>
 * <td>Sequence length currently displayed</td>
 * </tr>
 * <tr>
 * <td>%pixelwidth</td>
 * <td>Pixel width of the tracks</td>
 * </tr>
 * 
 * 
 * </table>
 * 
 * @author leo
 * 
 */
public class ReadTrack extends TrackBase {

	// track configuration parameters
	private final String CONFIG_LEFT_MARGIN = "leftMargin";
	private final String CONFIG_DB_TYPE = "dbType";
	private final String CONFIG_PATH = "path";
	private final String CONFIG_LAYOUT = "layout";
	private final String CONFIG_SHOW_LABELS = "showLabels";
	private final String CONFIG_ONCLICK_ACTION = "onclick.action";
	private final String CONFIG_ONCLICK_URL = "onclick.url";
	private final String CONFIG_ONCLICK_P_KEY = "onclick.p.key";
	private final String CONFIG_ONCLICK_P_VALUE = "onclick.p.value";

	// read data
	private OnGenomeDataSet dataSet;

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

		getConfig().setParameter(CONFIG_DB_TYPE, dbType);

		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.setWidget(0, 1, geneCanvas);

		updateClickAction();
	}

	public String resolveURL(String urlTemplate, OnGenome locus) {
		String url = urlTemplate;
		if (url == null)
			return url;

		if (locus != null) {
			if (locus.getName() != null) {
				if (url.contains("%q"))
					url = url.replaceAll("%q", locus.getName());
				if (url.contains("%name"))
					url = url.replaceAll("%name", locus.getName());
			}

			if (url.contains("%start"))
				url = url.replaceAll("%start", Integer.toString(locus.getStart()));
			if (url.contains("%end"))
				url = url.replaceAll("%end", Integer.toString(locus.getEnd()));
			if (url.contains("%length"))
				url = url.replaceAll("%length", Integer.toString(locus.length()));

		}

		// replace track group properties
		TrackWindow w = getTrackWindow();
		if (url.contains("%rstart"))
			url = url.replaceAll("%rstart", Integer.toString(w.getStartOnGenome()));
		if (url.contains("%rend"))
			url = url.replaceAll("%rend", Integer.toString(w.getEndOnGenome()));
		if (url.contains("%rlength"))
			url = url.replaceAll("%rlength", Integer.toString(w.getWidth()));
		if (url.contains("%pixelwidth"))
			url = url.replaceAll("%pixelwidth", Integer.toString(w.getWindowWidth()));
		String chr = getTrackGroupProperty(UTGBProperty.TARGET);
		if (chr != null && url.contains("%chr"))
			url = url.replaceAll("%chr", chr);
		String ref = getTrackGroupProperty(UTGBProperty.REVISION);
		if (ref != null && url.contains("%ref"))
			url = url.replaceAll("%ref", ref);
		String species = getTrackGroupProperty(UTGBProperty.SPECIES);
		if (species != null && url.contains("%species"))
			url = url.replaceAll("%species", species);

		return url;
	}

	private void updateClickAction() {

		String clickAction = getConfig().getParameter(CONFIG_ONCLICK_ACTION);
		if (clickAction == null)
			return;
		if ("none".equals(clickAction)) {
			geneCanvas.setLocusClickHandler(null);
		}
		else if ("link".equals(clickAction)) {
			geneCanvas.setLocusClickHandler(new LocusClickHandler() {
				public void onClick(int x, int y, OnGenome locus) {
					String url = getConfig().getParameter(CONFIG_ONCLICK_URL);
					url = resolveURL(url, locus);
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
		else if ("set".equals(clickAction)) {
			geneCanvas.setLocusClickHandler(new LocusClickHandler() {
				public void onClick(int clientX, int clientY, OnGenome locus) {
					String key = getConfig().getParameter(CONFIG_ONCLICK_P_KEY);
					if (key == null)
						return;
					String value = getConfig().getParameter(CONFIG_ONCLICK_P_VALUE);
					value = resolveURL(value, locus);
					if (value == null)
						return;

					setTrackGroupProperty(key, value);
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

		int leftMargin = getConfig().getInt(CONFIG_LEFT_MARGIN, 0);
		boolean showLabels = getConfig().getBoolean(CONFIG_SHOW_LABELS, true);

		int s = w.getStartOnGenome();
		int e = w.getEndOnGenome();
		int width = w.getWindowWidth() - leftMargin;

		if (leftMargin > 0)
			layoutTable.getCellFormatter().setWidth(0, 0, leftMargin + "px");

		geneCanvas.clear();
		geneCanvas.setWindow(new TrackWindowImpl(width, s, e));
		geneCanvas.setShowLabels(showLabels);

		String layout = getConfig().getString(CONFIG_LAYOUT, "pileup");
		if ("pileup".equals(layout)) {
			if (dataSet.read != null && !dataSet.read.isEmpty())
				geneCanvas.draw(dataSet.read);
			else
				geneCanvas.drawBlock(dataSet.block);
		}
		else {
			geneCanvas.drawBlock(dataSet.block);
		}

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
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		update(group.getTrackWindow());
		TrackConfig config = getConfig();
		config.addHiddenConfiguration(CONFIG_LEFT_MARGIN, "0");
		config.addConfigParameter("DB Path", new StringType(CONFIG_PATH));

		ValueDomain dbTypes = ValueDomain.createNewValueDomain(DBType.getDBTypeList());
		config.addConfigParameter("DB Type", new StringType(CONFIG_DB_TYPE, dbTypes), "AUTO");

		ValueDomain layoutTypes = ValueDomain.createNewValueDomain(new String[] { "pileup", "coverage" });
		config.addConfigParameter("Layout", new StringType(CONFIG_LAYOUT, layoutTypes), "pileup");
		config.addConfigParameter("Show Labels", new BooleanType(CONFIG_SHOW_LABELS), "true");
		ValueDomain actionTypes = ValueDomain.createNewValueDomain(new String[] { "none", "link", "info", "set" });
		config.addConfigParameter("On Click Action", new StringType(CONFIG_ONCLICK_ACTION, actionTypes), "link");
		config.addConfigParameter("On Click URL", new StringType(CONFIG_ONCLICK_URL), "http://www.google.com/search?q=%q");
		config.addConfigParameter("On Click - Set Key", new StringType(CONFIG_ONCLICK_P_KEY), "read");
		config.addConfigParameter("On Click - Set Value", new StringType(CONFIG_ONCLICK_P_VALUE), "%q");

		updateClickAction();
	}

	protected void update(TrackWindow newWindow) {
		// retrieve gene data from the API
		int s = newWindow.getStartOnGenome();
		int e = newWindow.getEndOnGenome();
		String chr = getTrackGroupProperty(UTGBProperty.TARGET);

		getFrame().setNowLoading();

		String layout = getConfig().getString(CONFIG_LAYOUT, "pileup");

		ReadQueryConfig queryConfig = new ReadQueryConfig(newWindow.getWindowWidth(), BrowserInfo.isCanvasSupported(), Layout.valueOf(Layout.class, layout
				.toUpperCase()));

		getBrowserService().getOnGenomeData(getGenomeDB(), new ChrLoc(chr, s, e), queryConfig, new AsyncCallback<OnGenomeDataSet>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve gene data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(OnGenomeDataSet readSet) {
				dataSet = readSet;

				refresh();
			}

		});

	}

	public String getPath() {
		return getConfig().getParameter(CONFIG_PATH);
	}

	/**
	 * Override this method to extend db info parameters
	 * 
	 * @return
	 */
	public GenomeDB getGenomeDB() {
		String ref = getTrackGroupProperty(UTGBProperty.REVISION);
		String dbType = getConfig().getString("dbType", "AUTO");
		return new GenomeDB(DBType.valueOf(DBType.class, dbType), getPath(), ref);
	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.containsOneOf(new String[] { CONFIG_ONCLICK_ACTION, CONFIG_ONCLICK_URL })) {
			updateClickAction();
		}

		if (change.containsOneOf(new String[] { CONFIG_LAYOUT, CONFIG_SHOW_LABELS, CONFIG_LEFT_MARGIN, CONFIG_PATH, CONFIG_DB_TYPE })) {
			refresh();
		}
	}

	@Override
	public void restoreProperties(Properties properties) {
		super.restoreProperties(properties);
		updateClickAction();
	}

}
