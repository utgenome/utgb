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

import java.util.HashSet;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.client.bio.Locus;
import org.utgenome.gwt.utgb.client.bio.WigGraphData;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.ui.CSS;
import org.utgenome.gwt.utgb.client.ui.FormLabel;
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
public class GWTGenomeCanvas extends Composite {

	private int windowWidth = 800;
	private int windowHeight = 100;
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

	public GWTGenomeCanvas() {
		initWidget();
	}

	public GWTGenomeCanvas(int windowPixelWidth, long startIndexOnGenome, long endIndexOnGenome) {
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
				int x1 = pixelPositionOnWindow(g.getStart()) - xBorder;
				int x2 = pixelPositionOnWindow(g.getEnd()) + xBorder;
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
		double v2 = v / (endIndexOnGenome - startIndexOnGenome);
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

		if (popup != null)
			popup.removeFromParent();
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

			int x1 = l.getStart();
			int x2 = l.getEnd();

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
			private int h = GWTGenomeCanvas.this.geneHeight + GWTGenomeCanvas.this.geneMargin;

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
			private int h = GWTGenomeCanvas.this.geneHeight + GWTGenomeCanvas.this.geneMargin;

			public void visit(LocusLayout gl) {
				gl.yOffset = gl.yOffset * h;
				Gene g = (Gene) gl.getLocus();
				int gx = pixelPositionOnWindow(g.getStart());
				int gx2 = pixelPositionOnWindow(g.getEnd());

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

	public Color hexValue2Color(String hex, float offset) {
		int r_value = Integer.parseInt(hex.substring(1, 3), 16);
		int g_value = Integer.parseInt(hex.substring(3, 5), 16);
		int b_value = Integer.parseInt(hex.substring(5, 7), 16);
		return new Color(colorOffset(r_value, offset), colorOffset(g_value, offset), colorOffset(b_value, offset));
	}

	public int colorOffset(int color, float offset) {
		return (int) (color + ((255 - color) * offset));
	}

	public Color getGeneColor(Locus g) {
		return hexValue2Color(getExonColorText(g), 0.7f);
	}

	public String getExonColorText(Locus g) {
		if (g.getColor() == null) {
			if (g.getStrand().equals("+")) {
				return "#d80067";
			}
			else {
				return "#0067d8";
			}
		}
		else {
			return g.getColor();
		}
	}

	public void draw(Gene gene, List<Exon> exonList, CDS cds, int yPosition) {
		// assumuption: exonList are sorted

		drawGeneRect(gene.getStart(), gene.getEnd(), yPosition, getGeneColor(gene));

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
		int gx = pixelPositionOnWindow(gene.getStart());
		int gx2 = pixelPositionOnWindow(gene.getEnd());

		//canvas.setGlobalAlpha(0.5f);
		drawGeneRect(gx, gx2, yPosition, getCDSColor(gene));
		//canvas.setGlobalAlpha(1f);
	}

	public void draw(Gene gene, int yPosition) {
		int gx = pixelPositionOnWindow(gene.getStart());
		int gx2 = pixelPositionOnWindow(gene.getEnd());

		//canvas.setGlobalAlpha(0.5f);
		drawGeneRect(gx, gx2, yPosition, getGeneColor(gene));
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

	private int indentHeight = 0;

	private float maxValue = 20.0f;
	private float minValue = 0.0f;
	private boolean isLog = false;

	public void drawWigGraph(WigGraphData data, Color color) {
		// TODO Auto-generated method stub
		long span = 1;
		if (data.getTrack().containsKey("span")) {
			span = Long.parseLong(data.getTrack().get("span"));
		}
		// draw data graph
		for (long pos : data.getData().keySet()) {
			float value = data.getData().get(pos);
			if (value == 0.0f)
				continue;

			float x1 = pixelPositionOnWindow(pos);
			float y1 = getYPosition(value);
			float width = pixelPositionOnWindow(pos + span) - x1;

			if (width <= 1.0f) {
				width = 1.0f;
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
			else
				exponent = 10;

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
			else
				return String.valueOf(indentValue);
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
