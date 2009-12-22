/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// GenomeBrowser Project
//
// GeneTrack.java
// Since: Jun 5, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import org.utgenome.gwt.utgb.client.track.Design;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A demo track, which shows how to draw objects (genes) on the browser screen.
 * 
 * @author leo
 * 
 */
public class SampleGeneTrack extends TrackBase implements GeneListObserver {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new SampleGeneTrack();
			}
		};
	}

	private AbsolutePanel _canvas = new AbsolutePanel();
	private int _start = 0;
	private int _end = 10000;
	private GeneList _geneList = null;
	private int _numGeneOnCanvas = 0;

	private int GENE_HEIGHT = 10;

	public SampleGeneTrack() {
		super("Sample Gene");

		_canvas.setStyleName("fasttrack");
	}

	void drawGene(GeneData gene) {
		if (_geneList == null)
			return;

		int geneStart = gene.getStart();
		int geneEnd = gene.getEnd();

		Image geneImage = new Image(Design.IMAGE_GENE);
		geneImage.setStyleName("gene");

		int geneImageSize = calcGeneImageSize(geneStart, geneEnd);
		geneImage.setWidth(geneImageSize + "px");
		geneImage.setHeight((GENE_HEIGHT - 2) + "px");
		Label label = new Label(gene.getName());
		label.setStyleName("gene-label");

		int xPosOnWindow = convertScale(geneStart);
		_canvas.add(geneImage, xPosOnWindow, GENE_HEIGHT * _numGeneOnCanvas);
		_canvas.add(label, xPosOnWindow + geneImageSize + 3, GENE_HEIGHT * _numGeneOnCanvas);

		_numGeneOnCanvas++;

		// extend the canvas height
		if ((_canvas.getOffsetHeight() - 10) < GENE_HEIGHT * _numGeneOnCanvas)
			_canvas.setHeight((GENE_HEIGHT * _numGeneOnCanvas + 10) + "px");
	}

	public void draw() {
		_canvas.clear();
		int width = getTrackGroup().getTrackWindow().getWindowWidth();
		_canvas.setSize(width + "px", "100%");

		_numGeneOnCanvas = 0;
		for (GeneData gene : _geneList) {
			drawGene(gene);
		}

	}

	public int calcGeneImageSize(int start, int end) {
		int x1 = convertScale(start);
		int x2 = convertScale(end);
		int width = (x1 < x2) ? x2 - x1 : x1 - x2;
		return (width <= 0) ? 1 : width;
	}

	public int convertScale(int x) {
		return getTrackGroup().getTrackWindow().calcXPositionOnWindow(x);
	}

	public Widget getWidget() {
		return _canvas;
	}

	public void onNewGeneAdded(GeneData gene) {
		drawGene(gene);
		getFrame().onUpdateTrackWidget();
	}

	public void onGeneListIsCleared() {
		refresh();
	}

	public int getDefaultWindowHeight() {
		return 80;
	}

	public void onChangeTrackWindow(TrackWindow newWindow) {
		refresh();
	}

	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {

	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		SampleGeneTrackGroup geneGroup = (SampleGeneTrackGroup) group;
		_geneList = geneGroup.getGeneList();
		_geneList.addObserver(this);
	}

}
