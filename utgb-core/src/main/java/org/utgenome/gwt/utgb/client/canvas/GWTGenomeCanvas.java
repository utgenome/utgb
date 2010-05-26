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
// GWTGenomeCanvas.java
// Since: Jul 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.BSSRead;
import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.InfoSilkGenerator;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.client.bio.OnGenomeDataVisitor;
import org.utgenome.gwt.utgb.client.bio.Read;
import org.utgenome.gwt.utgb.client.bio.ReadCoverage;
import org.utgenome.gwt.utgb.client.bio.ReferenceSequence;
import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.utgenome.gwt.utgb.client.bio.WigGraphData;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.ui.FixedWidthLabel;
import org.utgenome.gwt.utgb.client.ui.FormLabel;
import org.utgenome.gwt.utgb.client.ui.RoundCornerFrame;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Browser-side graphic canvas for drawing gene objects
 * 
 * @author leo
 * 
 */
public class GWTGenomeCanvas extends Composite {

	private int windowWidth = 800;
	private int windowHeight = 100;
	private long startIndexOnGenome = 1;
	private long endIndexOnGenome = 1000;

	private final int DEFAULT_GENE_HEIGHT = 9;
	private int geneHeight = DEFAULT_GENE_HEIGHT;
	private int geneMargin = 2;

	private boolean reverse = false;

	// widget
	private FlexTable layoutTable = new FlexTable();
	private GWTCanvas canvas = new GWTCanvas();
	private AbsolutePanel panel = new AbsolutePanel();
	private static PopupInfo popupLabel = new PopupInfo();
	private LocusClickHandler clickHandler = null;
	private PrioritySearchTree<LocusLayout> locusLayout = new PrioritySearchTree<LocusLayout>();

	private boolean showLabelsIfPossible = true;
	private boolean canDisplayLabel = true;

	private List<Widget> labels = new ArrayList<Widget>();

	public void setShowLabels(boolean show) {
		this.showLabelsIfPossible = show;
	}

	public GWTGenomeCanvas() {
		initWidget();
	}

	public GWTGenomeCanvas(int windowPixelWidth, long startIndexOnGenome, long endIndexOnGenome) {
		this.windowWidth = windowPixelWidth;
		setWindow(startIndexOnGenome, endIndexOnGenome);

		initWidget();
	}

	static class PopupInfo extends PopupPanel {

		OnGenome locus;
		private VerticalPanel info = new VerticalPanel();

		public PopupInfo() {
			super(true);

			Style.padding(info, Style.LEFT | Style.RIGHT, 5);
			Style.fontColor(info, "white");
			Style.fontSize(info, 14);
			Style.margin(info, 0);
			Style.preserveWhiteSpace(info);

			RoundCornerFrame infoFrame = new RoundCornerFrame("336699", 0.7f, 4);
			infoFrame.setWidget(info);
			this.setWidget(infoFrame);

		}

		public void setLocus(OnGenome g) {
			if (this.locus == g)
				return;

			this.locus = g;

		}

		public void update() {
			if (locus == null)
				return;

			InfoSilkGenerator silk = new InfoSilkGenerator();
			locus.accept(silk);
			info.clear();
			for (String line : silk.getLines()) {
				info.add(new HTML(line));
			}
		}

	}

	public void setLocusClickHandler(LocusClickHandler handler) {
		this.clickHandler = handler;
	}

	@Override
	public void onBrowserEvent(Event event) {

		super.onBrowserEvent(event);

		int type = DOM.eventGetType(event);
		switch (type) {
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEMOVE: {
			// show labels 
			OnGenome g = overlappedInterval(event, 2);
			if (g != null) {
				if (popupLabel.locus != g) {

					Style.cursor(canvas, Style.CURSOR_POINTER);

					int clientX = DOM.eventGetClientX(event) + Window.getScrollLeft();
					int clientY = DOM.eventGetClientY(event) + Window.getScrollTop();
					displayInfo(clientX, clientY, g);
				}
			}
			else {
				Style.cursor(canvas, Style.CURSOR_AUTO);
				popupLabel.setLocus(null);
			}

			break;
		}
		case Event.ONMOUSEDOWN: {
			// invoke a click event 
			int clientX = DOM.eventGetClientX(event) + Window.getScrollLeft();
			int clientY = DOM.eventGetClientY(event) + Window.getScrollTop();
			OnGenome g = overlappedInterval(event, 2);
			if (g != null) {
				if (clickHandler != null)
					clickHandler.onClick(clientX, clientY, g);
			}
			break;
		}
		}

	}

	public void displayInfo(final int clientX, final int clientY, final OnGenome g) {
		if (popupLabel == null)
			popupLabel = new PopupInfo();

		popupLabel.setLocus(g);

		Timer timer = new Timer() {
			@Override
			public void run() {
				popupLabel.removeFromParent();
				if (popupLabel.locus == g) {
					popupLabel.setPopupPosition(clientX + 10, clientY + 3);
					popupLabel.update();
					popupLabel.show();
				}
			}
		};

		timer.schedule(100);
	}

	/**
	 * compute the overlapped intervals for the mouse over event
	 * 
	 * @param event
	 * @param xBorder
	 * @return
	 */
	private OnGenome overlappedInterval(Event event, int xBorder) {

		int x = drawPosition(getXOnCanvas(event));
		int y = getYOnCanvas(event);

		for (LocusLayout gl : locusLayout.rangeQuery(x, Integer.MAX_VALUE, x)) {
			OnGenome g = gl.getLocus();
			int y1 = gl.getYOffset();
			int y2 = y1 + geneHeight;

			if (y1 <= y && y <= y2) {
				int x1 = pixelPositionOnWindow(g.getStart()) - xBorder;
				int x2 = pixelPositionOnWindow(g.getStart() + g.length()) + xBorder;

				if (canDisplayLabel) {
					int labelWidth = estimiateLabelWidth(g);
					if (x1 - labelWidth > 0)
						x1 -= labelWidth;
					else
						x2 += labelWidth;
				}

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

	public int pixelPositionOnWindow(int indexOnGenome) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) windowWidth;
		double v2 = v / (endIndexOnGenome - startIndexOnGenome + 1);
		return (int) v2;
	}

	public int calcGenomePosition(int xOnWindow) {
		if (startIndexOnGenome <= endIndexOnGenome) {
			double genomeLengthPerBit = (double) (endIndexOnGenome - startIndexOnGenome) / (double) windowWidth;
			return (int) (startIndexOnGenome + xOnWindow * genomeLengthPerBit);
		}
		else {
			// reverse strand
			double genomeLengthPerBit = (double) (startIndexOnGenome - endIndexOnGenome) / (double) windowWidth;
			return (int) (endIndexOnGenome + (windowWidth - xOnWindow) * genomeLengthPerBit);
		}
	}

	public void clear() {
		canvas.clear();
		locusLayout.clear();

		for (Widget w : labels) {
			w.removeFromParent();
		}
		labels.clear();

		if (popupLabel != null)
			popupLabel.removeFromParent();
	}

	@Override
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

	public class LocusLayout {
		private OnGenome locus;
		private int yOffset;

		public LocusLayout(OnGenome locus, int yOffset) {
			this.locus = locus;
			this.yOffset = yOffset;
		}

		public OnGenome getLocus() {
			return locus;
		}

		public int getYOffset() {
			return yOffset;
		}

	}

	private int estimiateLabelWidth(OnGenome l) {
		String name = l.getName();
		int labelWidth = name != null ? (int) (name.length() * DEFAULT_GENE_HEIGHT * 0.9) : 0;
		if (labelWidth > 150)
			labelWidth = 150;
		return labelWidth;
	}

	<T extends OnGenome> int createLayout(List<T> locusList) {

		int maxYOffset = 0;
		boolean showLabelsFlag = showLabelsIfPossible && (locusList.size() < 500);
		boolean toContinue = false;

		do {
			toContinue = false;
			maxYOffset = 0;
			locusLayout.clear();

			for (OnGenome l : locusList) {

				int x1 = pixelPositionOnWindow(l.getStart());
				int x2 = pixelPositionOnWindow(l.getEnd());

				if (showLabelsFlag) {
					int labelWidth = estimiateLabelWidth(l);
					if (x1 - labelWidth > 0)
						x1 -= labelWidth;
					else
						x2 += labelWidth;
				}

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

				if (blankY > maxYOffset) {
					maxYOffset = blankY;
					if (showLabelsFlag && maxYOffset > 30) {
						showLabelsFlag = false;
						toContinue = true;
						break;
					}
				}
			}
		}
		while (toContinue);

		if (maxYOffset <= 0)
			maxYOffset = 1;

		canDisplayLabel = showLabelsFlag;
		return maxYOffset;
	}

	class ReadPainter implements OnGenomeDataVisitor {

		private LocusLayout gl;
		private int h = GWTGenomeCanvas.this.geneHeight + GWTGenomeCanvas.this.geneMargin;

		public void setLayoutInfo(LocusLayout layout) {
			this.gl = layout;
			gl.yOffset = gl.yOffset * h;
		}

		public void visitBSSRead(BSSRead b) {
			// TODO Auto-generated method stub

		}

		public void visitGene(Gene g) {
			int gx1 = pixelPositionOnWindow(g.getStart());
			int gx2 = pixelPositionOnWindow(g.getEnd());

			int geneWidth = gx2 - gx1;
			if (geneWidth <= 10) {
				draw(g, gl.getYOffset());
			}
			else {
				CDS cds = g.getCDS().size() > 0 ? g.getCDS().get(0) : null;
				draw(g, g.getExon(), cds, gl.getYOffset());
			}

			drawLabel(g);
		}

		private void drawLabel(OnGenome r) {
			int gx1 = pixelPositionOnWindow(r.getStart());
			int gx2 = pixelPositionOnWindow(r.getStart() + r.length());

			if (canDisplayLabel) {
				String n = r.getName();
				if (n != null) {
					int textWidth = estimiateLabelWidth(r);

					FixedWidthLabel label = new FixedWidthLabel(n, textWidth);
					Style.fontSize(label, geneHeight);
					Style.fontColor(label, getExonColorText(r));

					Style.verticalAlign(label, "middle");

					int yPos = gl.getYOffset() - 1;

					if (gx1 - textWidth < 0) {
						if (reverse) {
							Style.textAlign(label, "right");
							panel.add(label, drawPosition(gx2) - textWidth - 1, yPos);
						}
						else {
							Style.textAlign(label, "left");
							panel.add(label, drawPosition(gx2) + 1, yPos);
						}
					}
					else {
						if (reverse) {
							Style.textAlign(label, "left");
							panel.add(label, drawPosition(gx1) + 1, yPos);
						}
						else {
							Style.textAlign(label, "right");
							panel.add(label, drawPosition(gx1) - textWidth - 1, yPos);
						}
					}

					labels.add(label);
				}
			}

		}

		public void visitInterval(Interval interval) {
			draw(interval, gl.getYOffset());
		}

		public void visitRead(Read r) {
			draw(r, gl.getYOffset());
			drawLabel(r);
		}

		public void visitSAMRead(SAMRead r) {
			draw(r, gl.getYOffset());
			drawLabel(r);
		}

		public void visitSequence(ReferenceSequence referenceSequence) {
			// TODO Auto-generated method stub

		}

		public void visitReadCoverage(ReadCoverage readCoverage) {

		}

	};

	public <T extends OnGenome> void draw(List<T> locusList) {

		layoutRead(locusList);

		final ReadPainter painter = new ReadPainter();

		locusLayout.depthFirstSearch(new PrioritySearchTree.Visitor<LocusLayout>() {
			public void visit(LocusLayout gl) {
				painter.setLayoutInfo(gl);
				gl.getLocus().accept(painter);
			}
		});

	}

	private <T extends OnGenome> void layoutRead(List<T> readList) {
		int maxOffset = createLayout(readList);
		if (maxOffset > 30)
			geneHeight = 2;
		else
			geneHeight = DEFAULT_GENE_HEIGHT;

		int h = geneHeight + geneMargin;
		int height = (maxOffset + 1) * h;

		setPixelSize(windowWidth, height);
	}

	public static Color getGeneColor(Interval l) {
		return getGeneColor(l, 1f);
	}

	public static Color getGeneColor(Interval l, float alpha) {
		String hex = getExonColorText(l);
		int r = Integer.parseInt(hex.substring(1, 3), 16);
		int g = Integer.parseInt(hex.substring(3, 5), 16);
		int b = Integer.parseInt(hex.substring(5, 7), 16);
		return new Color(r, g, b, alpha);
	}

	private static String getExonColorText(OnGenome g) {
		final String senseColor = "#d80067";
		final String antiSenseColor = "#0067d8";
		if (g instanceof Read) {
			Read r = (Read) g;
			if (r.getColor() == null) {
				return r.isSense() ? senseColor : antiSenseColor;
			}
			else {
				return r.getColor();
			}
		}
		else
			return senseColor;
	}

	public void draw(Gene gene, List<Exon> exonList, CDS cds, int yPosition) {
		// assume that exonList are sorted

		if (exonList.isEmpty()) {
			Color c = getGeneColor(gene);
			drawGeneRect(pixelPositionOnWindow(gene.getStart()), pixelPositionOnWindow(gene.getEnd()), yPosition, c, true);
		}

		for (Exon e : exonList) {
			drawExon(gene, e, cds, yPosition);
		}

		canvas.saveContext();
		canvas.setFillStyle(getGeneColor(gene, 0.7f));
		canvas.setStrokeStyle(getGeneColor(gene, 0.7f));
		canvas.setLineWidth(0.5f);

		// draw the arrow between exons
		boolean isSense = gene.isSense() ? !reverse : reverse;
		double arrowHeight = geneHeight / 2.0 + 0.5;

		for (int i = 0; i < exonList.size() - 1; i++) {
			Exon prev = exonList.get(i);
			Exon next = exonList.get(i + 1);

			int x1 = pixelPositionOnWindow(prev.getEnd());
			int x2 = pixelPositionOnWindow(next.getStart());
			float yAxis = yPosition + (geneHeight / 2) + 0.5f;

			canvas.saveContext();
			canvas.beginPath();
			canvas.moveTo(drawPosition(x1) + 0.5f, yAxis);
			canvas.lineTo(drawPosition(x2) - 0.5f, yAxis);
			canvas.stroke();
			canvas.restoreContext();

			for (int x = x1; x + 4 <= x2; x += 5) {
				canvas.saveContext();
				canvas.translate(drawPosition(x) + 2.0f, yPosition + arrowHeight);
				if (!isSense)
					canvas.rotate(Math.PI);
				canvas.beginPath();
				canvas.moveTo(-2.0f, -arrowHeight + 1.5f);
				canvas.lineTo(1.5f, 0);
				canvas.lineTo(-2.0f, arrowHeight - 1.5f);
				canvas.stroke();
				canvas.restoreContext();
			}

		}
		canvas.restoreContext();

	}

	private int drawPosition(int x) {
		if (reverse)
			return (windowWidth - x);
		else
			return x;
	}

	public static Color getExonColor(Gene g) {
		return getGeneColor(g, 0.5f);
	}

	public static Color getCDSColor(Interval g) {
		return getGeneColor(g);
	}

	public void draw(Interval gene, int yPosition) {
		int gx = pixelPositionOnWindow(gene.getStart());
		int gx2 = pixelPositionOnWindow(gene.getEnd());

		drawGeneRect(gx, gx2, yPosition, getCDSColor(gene), true);
	}

	public void draw(Gene gene, int yPosition) {
		int gx = pixelPositionOnWindow(gene.getStart());
		int gx2 = pixelPositionOnWindow(gene.getEnd());

		drawGeneRect(gx, gx2, yPosition, getGeneColor(gene), true);
	}

	public void drawExon(Gene gene, Exon exon, CDS cds, int yPosition) {
		int ex = pixelPositionOnWindow(exon.getStart());
		int ex2 = pixelPositionOnWindow(exon.getEnd());

		drawGeneRect(ex, ex2, yPosition, getExonColor(gene), true);

		// draw CDS
		if (cds != null) {
			int cx = pixelPositionOnWindow(cds.getStart());
			int cx2 = pixelPositionOnWindow(cds.getEnd());

			if (cx <= cx2) {
				if (ex <= cx2 && ex2 >= cx) {
					int cdsStart = (ex <= cx) ? cx : ex;
					int cdsEnd = (ex2 <= cx2) ? ex2 : cx2;

					drawGeneRect(cdsStart, cdsEnd, yPosition, getCDSColor(gene), false);
				}
			}

		}

	}

	public void drawGeneRect(int x1, int x2, int y, Color c, boolean drawShadow) {

		float boxWidth = x2 - x1 - 0.5f;
		if (boxWidth <= 0)
			boxWidth = 1f;

		canvas.saveContext();
		double drawX = drawPosition(reverse ? x2 : x1);
		canvas.setFillStyle(Color.WHITE);
		canvas.fillRect(drawX, y, boxWidth, geneHeight);
		canvas.setFillStyle(c);
		canvas.fillRect(drawX, y, boxWidth, geneHeight);
		canvas.restoreContext();

		if (drawShadow) {
			canvas.saveContext();
			canvas.setStrokeStyle(new Color(30, 30, 30, 0.6f));
			double shadowStart = drawPosition(reverse ? x2 : x1);
			canvas.translate(shadowStart, y);
			canvas.beginPath();
			canvas.moveTo(1.5f, geneHeight + 0.5f);
			canvas.lineTo(boxWidth + 0.5f, geneHeight + 0.5f);
			canvas.lineTo(boxWidth + 0.5f, 0.5f);
			canvas.stroke();
			canvas.restoreContext();
		}

	}

	private int indentHeight = 0;

	private float maxValue = 20.0f;
	private float minValue = 0.0f;
	private boolean isLog = false;

	public void drawWigGraph(WigGraphData data, Color color) {
		int span = 1;
		if (data.getTrack().containsKey("span")) {
			span = Integer.parseInt(data.getTrack().get("span"));
		}
		// draw data graph
		for (int pos : data.getData().keySet()) {
			float value = data.getData().get(pos);
			if (value == 0.0f)
				continue;

			int x1 = pixelPositionOnWindow(pos);
			float y1 = getYPosition(value);
			int width = pixelPositionOnWindow(pos + span) - x1;

			if (width <= 1) {
				width = 1;
			}

			if (reverse) {
				width *= -1.0f;
				x1 = windowWidth - x1;
			}

			float height;
			if (y1 == getYPosition(0.0f)) {
				continue;
			}
			else {
				if (y1 < 0.0f)
					y1 = 0.0f;
				else if (y1 > windowHeight)
					y1 = windowHeight;

				height = getYPosition(0.0f) - y1;
			}

			canvas.setFillStyle(color);
			canvas.fillRect(x1, y1, width, height);
		}
	}

	public void drawFrame(AbsolutePanel panel, int leftMargin) {
		// draw frame
		canvas.setFillStyle(Color.BLACK);
		canvas.fillRect(0, 0, 1, windowHeight);
		canvas.fillRect(windowWidth - 1, 0, 1, windowHeight);
		canvas.fillRect(0, 0, windowWidth, 1);
		canvas.fillRect(0, windowHeight - 1, windowWidth, 1);

		// draw indent line & label
		Indent indent = new Indent(minValue, maxValue);

		FormLabel[] label = new FormLabel[indent.nSteps + 1];
		for (int i = 0; i <= indent.nSteps; i++) {
			float value = indent.getIndentValue(i);

			label[i] = new FormLabel();
			label[i].setStyleName("search-label");
			label[i].setText(indent.getIndentString(i));

			panel.add(label[i], 0, 0);

			int labelPosition = 0;
			if (label[i].getOffsetWidth() < leftMargin)
				labelPosition = leftMargin - label[i].getOffsetWidth();

			panel.setWidgetPosition(label[i], labelPosition, (int) (getYPosition(value) - (label[i].getOffsetHeight() - indentHeight) / 2.0));

			if (getYPosition(value) < 0.0f || getYPosition(value) > windowHeight) {
				panel.remove(label[i]);
				continue;
			}

			// draw indent line
			canvas.setGlobalAlpha(0.2);
			canvas.fillRect(0, getYPosition(value), windowWidth, 1);
			// draw zero line
			canvas.setGlobalAlpha(1.0);
		}

		canvas.fillRect(0, getYPosition(0.0f), windowWidth, 1);
	}

	public class Indent {
		public int exponent = 0;
		public long fraction = 0;

		public int nSteps = 0;

		public float min = 0.0f;
		public float max = 0.0f;

		public Indent(float minValue, float maxValue) {
			if (indentHeight == 0)
				indentHeight = 10;

			min = minValue < maxValue ? minValue : maxValue;
			max = minValue > maxValue ? minValue : maxValue;

			if (isLog) {
				min = getLogValue(min);
				max = getLogValue(max);
			}

			double tempIndentValue = (max - min) / windowHeight * indentHeight;

			if (isLog && tempIndentValue < 1.0)
				tempIndentValue = 1.0;

			fraction = (long) Math.floor(Math.log10(tempIndentValue));
			exponent = (int) Math.ceil(Math.round(tempIndentValue / Math.pow(10, fraction - 3)) / 1000.0);

			if (exponent <= 5)
				;
			//			else if(exponent <= 7)
			//				exponent = 5;
			else {
				exponent = 1;
				fraction++;
			}
			double stepSize = exponent * Math.pow(10, fraction);
			max = (float) (Math.floor(max / stepSize) * stepSize);
			min = (float) (Math.ceil(min / stepSize) * stepSize);

			nSteps = (int) Math.abs((max - min) / stepSize);
		}

		public float getIndentValue(int step) {
			double indentValue = min + (step * exponent * Math.pow(10, fraction));

			if (!isLog)
				return (float) indentValue;
			else if (indentValue == 0.0f)
				return 0.0f;
			else if (indentValue >= 0.0f)
				return (float) Math.pow(2, indentValue - 1);
			else
				return (float) -Math.pow(2, -indentValue - 1);
		}

		public String getIndentString(int step) {
			float indentValue = getIndentValue(step);

			if (indentValue == (int) indentValue)
				return String.valueOf((int) indentValue);
			else {
				int exponent_tmp = (int) Math.ceil(Math.round(indentValue / Math.pow(10, fraction - 3)) / 1000.0);
				int endIndex = String.valueOf(exponent_tmp).length() + 1;
				if (fraction < 0)
					endIndex -= fraction;
				endIndex = Math.min(String.valueOf(indentValue).length(), endIndex);

				return String.valueOf(indentValue).substring(0, endIndex);
			}
		}
	}

	public float getYPosition(float value) {
		if (maxValue == minValue)
			return 0.0f;

		float tempMin = maxValue < minValue ? maxValue : minValue;
		float tempMax = maxValue > minValue ? maxValue : minValue;

		if (isLog) {
			value = getLogValue(value);
			tempMax = getLogValue(tempMax);
			tempMin = getLogValue(tempMin);
		}
		float valueHeight = (value - tempMin) / (tempMax - tempMin) * windowHeight;

		if (maxValue < minValue)
			return valueHeight;
		else
			return windowHeight - valueHeight;
	}

	private float logBase = 2.0f;

	public float getLogBase() {
		return logBase;
	}

	public void setLogBase(float logBase) {
		this.logBase = logBase;
	}

	public float getLogValue(float value) {
		if (Math.log(logBase) == 0.0)
			return value;

		float temp = 0.0f;
		if (value > 0.0f) {
			temp = (float) (Math.log(value) / Math.log(logBase) + 1.0);
			if (temp < 0.0f)
				temp = 0.0f;
		}
		else if (value < 0.0f) {
			temp = (float) (Math.log(-value) / Math.log(logBase) + 1.0);
			if (temp < 0.0f)
				temp = 0.0f;
			temp *= -1.0f;
		}
		return temp;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
		setPixelSize(windowWidth, windowHeight);
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setPanelHeight(int height) {
		if (height > 0) {
			panel.setHeight(height + "px");
			panel.setWidgetPosition(canvas, 0, indentHeight / 2);
		}
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public boolean getIsLog() {
		return isLog;
	}

	public void setIsLog(boolean isLog) {
		this.isLog = isLog;
	}

	public int getIndentHeight() {
		return indentHeight;
	}

	public void setIndentHeight(int indentHeight) {
		this.indentHeight = indentHeight;
	}

}
