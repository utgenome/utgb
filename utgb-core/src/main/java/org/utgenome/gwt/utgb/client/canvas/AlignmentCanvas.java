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
// AlignmentCanvas.java
// Since: Sep 5, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.Alignment;
import org.utgenome.gwt.utgb.client.bio.AlignmentResult;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * A canvas object for displaying genome alignment results
 * 
 * @author leo
 * 
 */
public class AlignmentCanvas extends Composite {

	private VerticalPanel panel = new VerticalPanel();
	private GWTCanvas canvas = new GWTCanvas();
	private AlignmentLabel alignmentLabel = new AlignmentLabel();
	private HashMap<String, Long> querySequenceSizeTable = new HashMap<String, Long>();
	private AlignmentLayout layout = null;
	private AlignmentResult alignmentResult = null;
	private PrettyAlignmentPanel prettyAlignmentView = new PrettyAlignmentPanel();
	private Track parentTrack = null;
	private int windowWidth = 500;
	private static int ALIGNMENT_HEIGHT = 5;

	static class AlignmentLabel extends PopupPanel {
		private Label label = new Label();

		public AlignmentLabel() {
			super();
			this.add(label);
			Style.border(this, 1, "solid", "black");
			Style.backgroundColor(this, "#FFFFEE");
		}

		public void setText(String text) {
			label.setText(text);
		}
	}

	static class PrettyAlignmentPanel extends HTML {
		public PrettyAlignmentPanel() {
			Style.fontSize(this, 12);
			Style.fontFamily(this, "monospace");
			Style.margin(this, Style.TOP, 5);
		}

		public void setText(String text) {
			super.setHTML("<pre>" + text + "</pre>");
		}
	}

	public AlignmentCanvas(Track parentTrack) {

		this.parentTrack = parentTrack;

		Style.borderCollapse(prettyAlignmentView);

		panel.add(canvas);
		panel.add(prettyAlignmentView);
		setPixelSize(0, 0);
		initWidget(panel);

		sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEOUT | Event.ONMOUSEMOVE);
	}

	public void clear() {
		canvas.clear();

	}

	public void setPixelSize(int pixelWidth, int pixelHeight) {
		canvas.setCoordSize(pixelWidth, pixelHeight);
		canvas.setPixelWidth(pixelWidth);
		canvas.setPixelHeight(pixelHeight);
	}

	static class Region extends Interval {
		Alignment alignment;

		public Region(int x1, int x2, Alignment alignment) {
			super(x1, x2);
			this.alignment = alignment;
		}
	}

	/**
	 * A layout of alignment results
	 * 
	 * @author leo
	 * 
	 */
	class AlignmentLayout {
		private long querySequenceLength;
		private IntervalLayout<Region> plusStrandLayout;
		private IntervalLayout<Region> minusStrandLayout;
		public static final int QSEQ_HEIGHT = 10;

		public AlignmentLayout(long querySequenceLength) {
			this.querySequenceLength = querySequenceLength;
		}

		public void add(List<Alignment> alignmentList) {
			ArrayList<Alignment> plusStrand = new ArrayList<Alignment>();
			ArrayList<Alignment> minusStrand = new ArrayList<Alignment>();

			for (Alignment alignment : alignmentList) {
				if (alignment.isPlusStrand())
					plusStrand.add(alignment);
				else
					minusStrand.add(alignment);
			}
			plusStrandLayout = layout(plusStrand);
			minusStrandLayout = layout(minusStrand);
		}

		protected IntervalLayout<Region> layout(List<Alignment> alignmentList) {
			ArrayList<Region> alignmentRegionList = new ArrayList<Region>();
			for (Alignment alignment : alignmentList) {
				int x1 = getXPixelPosition(querySequenceLength, alignment.getQueryStart());
				int x2 = getXPixelPosition(querySequenceLength, alignment.getQueryEnd());
				alignmentRegionList.add(new Region(x1, x2, alignment));
			}
			return new IntervalLayout<Region>(ALIGNMENT_HEIGHT, alignmentRegionList);
		}

		public void draw(GWTCanvas canvas) {
			// set the canvas size
			int canvasWidth = windowWidth;
			int canvasHeight = plusStrandLayout.getCanvasHeight() + minusStrandLayout.getCanvasHeight() + QSEQ_HEIGHT;
			setPixelSize(canvasWidth, canvasHeight);

			// draw query sequence
			Color seqColor = new Color("#CCCCCC");
			canvas.setFillStyle(seqColor);
			canvas.fillRect(0, plusStrandLayout.getCanvasHeight() + 2, canvasWidth, QSEQ_HEIGHT - 4);

			// draw plus strand
			Color plusStrandColor = new Color("#ED9DB9");
			for (IntervalLocation<Region> loc : plusStrandLayout.getIntervalLayoutList()) {
				draw(loc, true, plusStrandColor);
			}
			Color minusStrandColor = new Color("#9DB9ED");
			for (IntervalLocation<Region> loc : minusStrandLayout.getIntervalLayoutList()) {
				draw(loc, false, minusStrandColor);
			}
		}

		public int getPlusStrandCanvasYOffset() {
			return plusStrandLayout.getCanvasHeight();
		}

		public int getMinusStrandCanvasYOffset() {
			return plusStrandLayout.getCanvasHeight() + QSEQ_HEIGHT;
		}

		public void draw(IntervalLocation<Region> loc, boolean isPlusStrand, Color c) {
			Region r = loc.interval;
			int width = r.end - r.start;
			if (width < 0)
				width = -width;
			if (width == 0)
				width = 1;

			int boxX = r.start;
			int boxY = getAlignmentYPosOnCanvas(isPlusStrand, loc.y);

			// draw line
			canvas.setLineWidth(0.5f);
			canvas.setStrokeStyle(new Color("#333333"));
			float lineY = getAlignmentYPosOnCanvas(isPlusStrand, loc.y) + (ALIGNMENT_HEIGHT / 2) + 0.5f;
			canvas.beginPath();
			canvas.moveTo(r.start + 0.5f, lineY);
			canvas.lineTo(r.start + width - 0.5f, lineY);
			canvas.stroke();

			// draw the matched blocks
			canvas.setFillStyle(c);
			Alignment alignment = r.alignment;
			for (int i = 0; i < alignment.getBlockCount(); i++) {
				int blockWidth = getXPixelPosition(querySequenceLength, alignment.getBlockSizes().get(i));
				int blockStart = getXPixelPosition(querySequenceLength, alignment.getQueryStarts().get(i));
				if (!alignment.isPlusStrand()) {
					// reverse the qStarts index to the corrdinate of the forward starand
					int queryPixelSize = getXPixelPosition(querySequenceLength, querySequenceLength);
					blockStart = queryPixelSize - blockStart - blockWidth - 1;
				}
				canvas.fillRect(blockStart + 0.5f, boxY + 0.5f, blockWidth - 0.5f, ALIGNMENT_HEIGHT - 0.5f);
			}

		}

		public int getAlignmentYPosOnCanvas(boolean isPlusStrand, int y) {
			if (isPlusStrand) {
				return getPlusStrandCanvasYOffset() - y - ALIGNMENT_HEIGHT;
			}
			else {
				return getMinusStrandCanvasYOffset() + y;
			}
		}

		public Alignment getAlignment(int x, int y) {

			if (y <= getPlusStrandCanvasYOffset()) {
				// search the plus strand canvas
				for (IntervalLocation<Region> loc : plusStrandLayout.getIntervalLayoutList()) {
					Region r = loc.interval;
					int ay1 = getAlignmentYPosOnCanvas(true, loc.y);
					int ay2 = ay1 + ALIGNMENT_HEIGHT;

					if (r.start <= x && x <= r.end && ay1 <= y && y <= ay2) {
						return r.alignment;
					}
				}
			}
			else {
				// search the minus strand canvas
				for (IntervalLocation<Region> loc : minusStrandLayout.getIntervalLayoutList()) {
					Region r = loc.interval;
					int ay1 = getAlignmentYPosOnCanvas(false, loc.y);
					int ay2 = ay1 + ALIGNMENT_HEIGHT;
					if (r.start <= x && x <= r.end && ay1 <= y && y <= ay2) {
						return r.alignment;
					}
				}
			}
			return null;
		}

	}

	@Override
	public void onBrowserEvent(Event event) {

		super.onBrowserEvent(event);

		int type = DOM.eventGetType(event);
		switch (type) {
		case Event.ONMOUSEMOVE: {
			Alignment alignment = getAlignment(event);
			if (alignment != null) {
				int x = getXOnCanvas(event);
				int y = getYOnCanvas(event);

				Style.cursor(canvas, Style.CURSOR_POINTER);

				alignmentLabel.setText(alignment.getTargetName() + "/" + alignment.getTargetStart() + ":" + alignment.getTargetEnd());
				alignmentLabel.setPopupPosition(canvas.getAbsoluteLeft() + x + 15, canvas.getAbsoluteTop() + y);
				alignmentLabel.show();

				// display pretty alignment
				int prettyAlignmentIndex = alignmentResult.getAlignment().indexOf(alignment);
				if (prettyAlignmentIndex != -1) {
					prettyAlignmentView.setText(alignmentResult.getPrettyAlignment().get(prettyAlignmentIndex));
					parentTrack.refresh();
				}

			}
			else {
				Style.cursor(canvas, Style.CURSOR_AUTO);
				alignmentLabel.hide();
			}
			break;
		}
		case Event.ONMOUSEDOWN: {
			//int clientX = DOM.eventGetScreenX(event);
			//int clientY = DOM.eventGetScreenY(event);
			Alignment alignment = getAlignment(event);
			if (alignment == null)
				return;

			// move to the clicked location
			String target = alignment.getTargetName();
			int start = alignment.getTargetStart();
			int end = alignment.getTargetEnd();

			TrackGroupPropertyWriter writer = parentTrack.getTrackGroup().getPropertyWriter();
			try {
				writer.setProperyChangeNotifaction(false);
				writer.setTrackWindow(start, end);
			}
			finally {
				writer.setProperyChangeNotifaction(true);
			}
			writer.setProperty(UTGBProperty.TARGET, target);
			break;
		}
		}

	}

	Alignment getAlignment(Event event) {
		int x = getXOnCanvas(event);
		int y = getYOnCanvas(event);
		if (layout != null)
			return layout.getAlignment(x, y);
		else
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

	static class Sequence {
		String sequenceName;
		boolean isPlusStrand;

		public Sequence(String sequenceName, boolean isPlusStrand) {
			this.sequenceName = sequenceName;
			this.isPlusStrand = isPlusStrand;
		}

		@Override
		public boolean equals(Object obj) {
			Sequence other = (Sequence) obj;
			return this.isPlusStrand == other.isPlusStrand && (this.sequenceName.equals(other.sequenceName));
		}

	}

	public void drawAlignment(AlignmentResult alignmentResult) {

		canvas.clear();
		this.alignmentResult = alignmentResult;

		// retrieve query sequence sizes
		for (Alignment alignment : alignmentResult.getAlignment()) {
			String queryName = alignment.getQueryName();
			if (!querySequenceSizeTable.containsKey(queryName)) {
				long querySequenceLength = alignment.getQueryLen();
				querySequenceSizeTable.put(queryName, querySequenceLength);
			}
		}

		// layout query sequences for each query sequence name
		for (String querySequenceName : querySequenceSizeTable.keySet()) {
			long querySequenceLength = querySequenceSizeTable.get(querySequenceName);

			layout = new AlignmentLayout(querySequenceLength);
			// TODO we have to extend this part to draw multiple query sequences 
			layout.add(alignmentResult.getAlignment());
			layout.draw(canvas);
			parentTrack.refresh();
		}

	}

	public int getXPixelPosition(long querySequenceLength, long indexOnQuerySequence) {
		double v = indexOnQuerySequence * (double) windowWidth;
		double v2 = v / (double) querySequenceLength;
		return (int) v2;
	}

}
