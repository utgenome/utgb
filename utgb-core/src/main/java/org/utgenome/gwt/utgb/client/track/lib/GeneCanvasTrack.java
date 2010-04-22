/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// GeneCanvasTrack.java
// Since: Jul 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.Locus;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author leo
 * 
 */
public class GeneCanvasTrack extends TrackBase {

	private final static String KEY_DATA_SOURCE_URI = "data.uri";
	private String dataSourceURI = "http://utgenome.org/api/refseq";

	private ArrayList<Gene> genes = new ArrayList<Gene>();

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new GeneCanvasTrack();
			}
		};
	}

	public GeneCanvasTrack() {
		super("Gene Canvas");

		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		layoutTable.getCellFormatter().setWidth(0, 0, "100px");
		layoutTable.setWidget(0, 1, geneCanvas);

		//layoutTable.setHeight(300 + "px");

		//CSS.border(geneCanvas, 2, "solid", "cyan");

		geneCanvas.setLocusClickHandler(new LocusClickHandler() {
			public void onClick(Locus locus) {
				String url = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=" + locus.getName();
				Window.open(url, "ncbi", "");
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
		int width = w.getWindowWidth() - 100;

		geneCanvas.clear();
		geneCanvas.setWindow(new TrackWindowImpl(width, s, e));
		geneCanvas.draw(genes);

		getFrame().loadingDone();
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

		if (change.containsOneOf(new String[] { UTGBProperty.SPECIES, UTGBProperty.REVISION, UTGBProperty.TARGET })) {
			update(change.getTrackWindow());
		}
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		update(group.getTrackWindow());
	}

	class UpdateCommand implements Command {
		private final List<Gene> geneList;

		public UpdateCommand(List<Gene> geneList) {
			this.geneList = geneList;
		}

		public void execute() {
			genes.clear();
			genes.addAll(geneList);
			refresh();
		}
	}

	public void update(TrackWindow newWindow) {
		// retrieve gene data from the API
		long s = newWindow.getStartOnGenome();
		long e = newWindow.getEndOnGenome();
		TrackGroupProperty prop = getTrackGroup().getPropertyReader();
		String species = prop.getProperty(UTGBProperty.SPECIES);
		String revision = prop.getProperty(UTGBProperty.REVISION);
		String target = prop.getProperty(UTGBProperty.TARGET);

		String dataSourceFullURI = dataSourceURI + "/" + species + "/" + revision + "/" + target + ":" + s + "-" + e + "/list.json";

		getFrame().setNowLoading();

		GenomeBrowser.getService().getGeneList(dataSourceFullURI, new AsyncCallback<List<Gene>>() {

			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve gene data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<Gene> geneList) {
				DeferredCommand.addCommand(new UpdateCommand(geneList));
				GWT.log("canvas size= (" + geneCanvas.getOffsetWidth() + "," + geneCanvas.getOffsetHeight() + ")", null);
				GWT.log("frame size= (" + layoutTable.getOffsetWidth() + "," + layoutTable.getOffsetHeight() + ")", null);
			}
		});

	}

	@Override
	public void saveProperties(Properties saveData) {
		saveData.add(KEY_DATA_SOURCE_URI, dataSourceURI);
	}

	@Override
	public void restoreProperties(Properties properties) {
		dataSourceURI = properties.get(KEY_DATA_SOURCE_URI, "http://utgenome.org/api/refseq");
	}

}
