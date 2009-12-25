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
// GenomeCanvas.java
// Since: Jul 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.HashSet;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.Locus;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.ui.CSS;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Browser-side graphic canvas for drawing gene objects
 * 
 * @author leo
 * 
 */
public class GenomeCanvas extends Composite {

	private int windowWidth = 800;
	private long startIndexOnGenome = 1;
	private long endIndexOnGenome = 1000;

	private int geneHeight = 6;
	private int geneMargin = 1;

	private boolean reverse = false;

	// widget
	private FlexTable layoutTable = new FlexTable();
	private GWTCanvas canvas = new GWTCanvas();
	private AbsolutePanel panel = new AbsolutePanel();
	private GeneNamePopup popup = new GeneNamePopup(null);
	private LocusClickHandler handler = null;
	private PrioritySearchTree<LocusLayout> locusLayout = new PrioritySearchTree<LocusLayout>();

	public GenomeCanvas() {
		initWidget();
	}

	public GenomeCanvas(int windowPixelWidth, long startIndexOnGenome, long endIndexOnGenome) {
		this.windowWidth = windowPixelWidth;
		setWindow(startIndexOnGenome, endIndexOnGenome);

		initWidget();
	}

	class GeneNamePopup extends PopupPanel {

		public final Locus locus;
		private String name;

		public GeneNamePopup(Locus l) {
			super(true);
			this.locus = l;

			CSS.border(this, 1, "solid", "#666666");
			CSS.backgroundColor(this, "#FFFFF8");
			CSS.padding(this, 2);
			CSS.fontSize(this, 12);
		}

		public void setName(String name) {
			this.name = name;

			this.clear();
			//Hyperlink link = new Hyperlink(name, name);
			//link.addClickHandler(this);
			this.add(new Label(name));
		}

		public void onClick(ClickEvent e) {
			if (handler != null)
				handler.onClick(locus);
			//String url = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=" + this.name;
			//Window.open(url, "ncbi", "");
		}
	}

	public void setLocusClickHandler(LocusClickHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onBrowserEvent(Event event) {

		super.onBrowserEvent(event);

		int type = DOM.eventGetType(event);
		switch (type) {
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEMOVE: {
			Locus g = overlappedLocus(event, 2);
			if (g != null) {
				if (popup.locus != g) {
					popup.removeFromParent();
					popup = new GeneNamePopup(g);

					Style.cursor(canvas, Style.CURSOR_POINTER);

					int clientX = DOM.eventGetClientX(event) + Window.getScrollLeft();
					int clientY = DOM.eventGetClientY(event) + Window.getScrollTop();
					popup.setPopupPosition(clientX + 10, clientY + 3);
					popup.setName(g.getName());
					popup.show();
				}
			}
			else
				Style.cursor(canvas, Style.CURSOR_AUTO);

			break;
		}
		case Event.ONMOUSEDOWN: {
			Locus g = overlappedLocus(event, 2);
			if (g != null) {
				if (handler != null)
					handler.onClick(g);
			}
			break;
		}
		}

	}

	private Locus overlappedLocus(Event event, int xBorder) {

		int x = getXOnCanvas(event);
		int y = getYOnCanvas(event);

		int xOnGenome = calcGenomePosition(x);

		for (LocusLayout gl : locusLayout.rangeQuery(xOnGenome, Integer.MAX_VALUE, xOnGenome)) {
			Locus g = gl.getLocus();
			int y1 = gl.getYOffset();
			int y2 = y1 + geneHeight;

			if (y1 <= y && y <= y2) {
				int x1 = pixelPositionOnWindow(g.getViewStart()) - xBorder;
				int x2 = pixelPositionOnWindow(g.getViewEnd()) + xBorder;
				if (x1 <= x && x <= x2)
					return g;
			}
		}
		return null;

	}

	public int getXOnCanvas(Event event) {
		int clientX = DOM.eventGetClientX(event);
		return clientX - canvas.getAbsoluteLeft() + Window.getScrollLeft();
	}

	public int getYOnCanvas(Event event) {
		int clientY = DOM.eventGetClientY(event);
		return clientY - canvas.getAbsoluteTop() + Window.getScrollTop();
	}

	private void initWidget() {
		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);

		panel.add(canvas, 0, 0);
		layoutTable.setWidget(0, 1, panel);
		initWidget(layoutTable);

		sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEOVER | Event.ONMOUSEDOWN);
	}

	public void setWindow(long startIndexOnGenome, long endIndexOnGenome) {
		if (startIndexOnGenome > endIndexOnGenome) {
			this.startIndexOnGenome = endIndexOnGenome;
			this.endIndexOnGenome = startIndexOnGenome;
			reverse = true;
		}
		else {
			this.startIndexOnGenome = startIndexOnGenome;
			this.endIndexOnGenome = endIndexOnGenome;
			reverse = false;
		}
	}

	public void setWindow(TrackWindow w) {
		this.windowWidth = w.getWindowWidth();
		setWindow(w.getStartOnGenome(), w.getEndOnGenome());
		canvas.setCoordSize(windowWidth, 100);
		canvas.setPixelWidth(windowWidth);
	}

	public int pixelPositionOnWindow(long indexOnGenome) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) windowWidth;
		double v2 = v / (double) (endIndexOnGenome - startIndexOnGenome);
		return (int) v2;
	}

	public int calcGenomePosition(int xOnWindow) {
		if (startIndexOnGenome <= endIndexOnGenome) {
			double genomeLengthPerBit = (double) (endIndexOnGenome - startIndexOnGenome) / (double) windowWidth;
			return (int) (startIndexOnGenome + (double) xOnWindow * genomeLengthPerBit);
		}
		else {
			// reverse strand
			double genomeLengthPerBit = (double) (startIndexOnGenome - endIndexOnGenome) / (double) windowWidth;
			return (int) (endIndexOnGenome + (double) (windowWidth - xOnWindow) * genomeLengthPerBit);
		}
	}

	public void clear() {
		canvas.clear();
		locusLayout.clear();

		if (popup != null)
			popup.removeFromParent();
	}

	public void setPixelSize(int width, int height) {
		this.windowWidth = width;
		canvas.setCoordSize(width, height);
		canvas.setPixelWidth(width);
		canvas.setPixelHeight(height);
		panel.setPixelSize(width, height);
	}

	public static int width(int x1, int x2) {
		return (x1 < x2) ? x2 - x1 : x1 - x2;
	}

	public class LocusLayout implements Comparable<LocusLayout> {
		private Locus locus;
		private int yOffset;

		public LocusLayout(Locus locus, int yOffset) {
			this.locus = locus;
			this.yOffset = yOffset;
		}

		public Locus getLocus() {
			return locus;
		}

		public int getYOffset() {
			return yOffset;
		}

		public int compareTo(LocusLayout other) {
			return locus.compareTo(other.locus);
		}

	}

	<T extends Locus> int createLayout(List<T> locusList) {
		int maxYOffset = 0;
		locusLayout.clear();

		for (Locus l : locusList) {

			int x1 = l.getViewStart();
			int x2 = l.getViewEnd();

			List<LocusLayout> activeLocus = locusLayout.rangeQuery(x1, Integer.MAX_VALUE, x2);

			HashSet<Integer> filledY = new HashSet<Integer>();
			// overlap test
			for (LocusLayout al : activeLocus) {
				filledY.add(al.yOffset);
			}

			int blankY = 0;
			for (; filledY.contains(blankY); blankY++) {
			}

			locusLayout.insert(new LocusLayout(l, blankY), x2, x1);

			if (blankY > maxYOffset)
				maxYOffset = blankY;
		}

		if (maxYOffset <= 0)
			maxYOffset = 1;
		return maxYOffset;
	}

	public void drawLocus(List<Locus> locusList) {

		int maxOffset = createLayout(locusList);

		int h = geneHeight + geneMargin;
		int height = maxOffset * h;

		setPixelSize(windowWidth, height);

		locusLayout.depthFirstSearch(new PrioritySearchTree.Visitor<LocusLayout>() {
			private int h = GenomeCanvas.this.geneHeight + GenomeCanvas.this.geneMargin;

			public void visit(LocusLayout gl) {
				gl.yOffset = gl.yOffset * h;
				draw(gl.getLocus(), gl.getYOffset());
			}
		});

	}

	public void draw(List<Gene> geneList) {

		int maxOffset = createLayout(geneList);

		int h = geneHeight + geneMargin;
		int height = maxOffset * h;

		setPixelSize(windowWidth, height);

		locusLayout.depthFirstSearch(new PrioritySearchTree.Visitor<LocusLayout>() {
			private int h = GenomeCanvas.this.geneHeight + GenomeCanvas.this.geneMargin;

			public void visit(LocusLayout gl) {
				gl.yOffset = gl.yOffset * h;
				Gene g = (Gene) gl.getLocus();
				int gx = pixelPositionOnWindow(g.getViewStart());
				int gx2 = pixelPositionOnWindow(g.getViewEnd());

				int geneWidth = gx2 - gx;
				if (geneWidth <= 10) {
					draw(g, gl.getYOffset());
				}
				else {
					CDS cds = g.getCDS().size() > 0 ? g.getCDS().get(0) : null;
					draw(g, g.getExon(), cds, gl.getYOffset());
				}
			}

		});

	}

	public void draw(Gene gene, List<Exon> exonList, CDS cds, int yPosition) {
		// assumuption: exonList are sorted

		//GWT.log("exon: ", null);
		for (Exon e : exonList) {
			draw(gene, e, cds, yPosition);
		}

		// draw arrow between exons
		for (int i = 0; i < exonList.size() - 1; i++) {
			Exon prev = exonList.get(i);
			Exon next = exonList.get(i + 1);

			int x1 = pixelPositionOnWindow(prev.getEnd());
			int x2 = pixelPositionOnWindow(next.getStart());
			float yAxis = yPosition + (geneHeight / 2);
			float xMiddle = (x1 + x2) / 2;
			//GWT.log("exon  :(" + prev.getEnd() + "-" + next.getStart() + ")", null);
			//GWT.log("canvas:(" + x1 + ", " + xMiddle + ", " + x2 + ")", null);

			int range = x2 - x1;

			canvas.setLineWidth(0.5f);
			canvas.setStrokeStyle(getExonColor(gene));
			canvas.beginPath();

			canvas.moveTo(drawPosition(x1 + 0.5f), yAxis + 0.5f);
			if (range > 10)
				canvas.lineTo(drawPosition(xMiddle), yPosition);
			canvas.lineTo(drawPosition(x2 - 0.5f), yAxis + 0.5f);
			canvas.stroke();
		}

	}

	public float drawPosition(float x) {
		if (!reverse)
			return x;
		else
			return windowWidth - x;
	}

	public Color getExonColor(Gene g) {
		if (g.getStrand().equals("+")) {
			//return new Color("#3686AA");
			return new Color("#d80067");
		}
		else {
			//return new Color("#AA8636");
			return new Color("#0067d8");
		}
	}

	public Color getCDSColor(Locus g) {
		if (g.getStrand().equals("+")) {
			//return new Color("#F7B357");
			return new Color("#ED9DB9");
		}
		else {
			//return new Color("#57B3F7");
			return new Color("#9DB9ED");
		}
	}

	public Color getIntronColor(Gene g) {
		if (g.getStrand().equals("+")) {
			return new Color("#ED9DB9");
		}
		else {
			return new Color("#9DB9ED");
		}
	}

	public void draw(Locus gene, int yPosition) {
		int gx = pixelPositionOnWindow(gene.getViewStart());
		int gx2 = pixelPositionOnWindow(gene.getViewEnd());

		//canvas.setGlobalAlpha(0.5f);
		drawGeneRect(gx, gx2, yPosition, getCDSColor(gene));
		//canvas.setGlobalAlpha(1f);
	}

	public void draw(Gene gene, int yPosition) {
		int gx = pixelPositionOnWindow(gene.getViewStart());
		int gx2 = pixelPositionOnWindow(gene.getViewEnd());

		//canvas.setGlobalAlpha(0.5f);
		drawGeneRect(gx, gx2, yPosition, getCDSColor(gene));
		//canvas.setGlobalAlpha(1f);
	}

	public void draw(Gene gene, Exon exon, CDS cds, int yPosition) {
		int ex = pixelPositionOnWindow(exon.getStart());
		int ex2 = pixelPositionOnWindow(exon.getEnd());

		drawGeneRect(ex, ex2, yPosition, getExonColor(gene));

		int exonStart = ex;
		int exonEnd = ex2;

		if (cds != null) {
			int cx = pixelPositionOnWindow(cds.getStart());
			int cx2 = pixelPositionOnWindow(cds.getEnd());

			if (cx <= cx2) {
				if (ex <= cx2 && ex2 >= cx) {
					int cdsStart = (ex <= cx) ? cx : ex;
					int cdsEnd = (ex2 <= cx2) ? ex2 : cx2;

					drawGeneRect(cdsStart, cdsEnd, yPosition, getCDSColor(gene));
				}
			}

		}
	}

	public void drawGeneRect(int x1, int x2, int y, Color c) {

		long boxWidth = x2 - x1;
		if (boxWidth <= 0)
			boxWidth = 1;

		canvas.setFillStyle(c);
		if (!reverse)
			canvas.fillRect(drawPosition(x1), y, boxWidth, geneHeight);
		else
			canvas.fillRect(drawPosition(x2), y, boxWidth, geneHeight);
	}

}
