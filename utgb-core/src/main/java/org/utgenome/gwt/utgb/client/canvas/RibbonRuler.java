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
// RibbonCanvas.java
// Since: Jul 13, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.utgenome.gwt.utgb.client.track.TrackWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

/**
 * Ribbon coordinate ruler
 * 
 * @author leo
 * 
 */
public class RibbonRuler extends Composite {
	private TrackWindow window;

	private GWTCanvas canvas = new GWTCanvas();

	private TreeSet<RibbonCrease> ribbonPoint = new TreeSet<RibbonCrease>();

	private enum RibbonType {
		GAP, FOLD, NORMAL
	}

	/**
	 * folding/gap point on a ribbon
	 * 
	 * @author leo
	 * 
	 */
	private static class RibbonCrease implements Comparable<RibbonCrease> {
		public final int pos;
		public final int len;

		private final RibbonType type;
		private boolean isOpen = true;

		public RibbonCrease(RibbonType type, int pos, int len) {
			this.type = type;
			this.pos = pos;
			this.len = len;
		}

		public boolean contains(long indexOnGenome) {
			return (pos <= indexOnGenome && indexOnGenome <= (pos + len));
		}

		public long getEnd() {
			switch (type) {
			case GAP:
				return pos;
			case FOLD:
			case NORMAL:
			default:
				return pos + len - 1;
			}

		}

		public int compareTo(RibbonCrease o) {
			return (this.pos - o.pos);
		}
	}

	private static class RibbonBlock {
		public final long startOnGenome;
		public final int size;
		public final RibbonType type;

		public RibbonBlock(RibbonType type, long start, int size) {
			this.startOnGenome = start;
			this.size = size;
			this.type = type;
		}

		public long getEnd() {
			switch (type) {
			case GAP:
				return startOnGenome;
			case FOLD:
			case NORMAL:
			default:
				return startOnGenome + size - 1;
			}

		}

	}

	private ImageElement fontPanel = null;

	public RibbonRuler() {
		initWidget(canvas);

	}

	private final int RIBBON_HEIGHT = 17;

	void drawFold(int x, int baseLength) {

	}

	void drawGap(int x, int baseLength) {

	}

	void redraw() {
		canvas.clear();
		canvas.setCoordSize(window.getPixelWidth(), RIBBON_HEIGHT);
		canvas.setPixelSize(window.getPixelWidth(), RIBBON_HEIGHT);

		long genomeCursor = window.getStartOnGenome();
		List<RibbonBlock> ribbonBlocks = new ArrayList<RibbonBlock>();
		// build logical ribbon blocks 
		for (RibbonCrease rp : ribbonPoint) {
			if (rp.getEnd() < genomeCursor)
				continue;

			if (genomeCursor < rp.pos)
				ribbonBlocks.add(new RibbonBlock(RibbonType.NORMAL, rp.pos, (int) (rp.pos - genomeCursor)));

			if (rp.isOpen)
				ribbonBlocks.add(new RibbonBlock(rp.type, rp.pos, rp.len));
			else
				ribbonBlocks.add(new RibbonBlock(rp.type, rp.pos, 0));

			genomeCursor = rp.getEnd() + 1;
		}

		if (genomeCursor < window.getEndOnGenome())
			ribbonBlocks.add(new RibbonBlock(RibbonType.NORMAL, genomeCursor, (int) (window.getEndOnGenome() - genomeCursor)));

		double pixelWidthPerBase = window.getPixelLengthPerBase();
		int numBase = 0;
		genomeCursor = window.getStartOnGenome();

		// draw ribbon
		for (RibbonBlock rb : ribbonBlocks) {
			boolean drawLeftSide = true;
			long left = rb.startOnGenome;
			if (rb.startOnGenome < genomeCursor && genomeCursor < rb.getEnd()) {
				drawLeftSide = false;
				left = genomeCursor;
			}

			float x1 = (float) (numBase * pixelWidthPerBase) + 0.5f;
			float x2 = (float) ((numBase + rb.size) * pixelWidthPerBase) - 0.5f;

			switch (rb.type) {
			case FOLD:

				break;
			case GAP:
				break;
			case NORMAL:
				canvas.setFillStyle(new Color("#FFEEEE"));
				canvas.setGlobalAlpha(0.7f);
				canvas.fillRect(x1, 0, x2 - x1, RIBBON_HEIGHT);
				break;
			}
			numBase += rb.size;
			genomeCursor = rb.getEnd();
		}

		// draw tick
		if (fontPanel == null) {
			ImageLoader.loadImages(new String[] { GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0x663333" }, new ImageLoader.CallBack() {
				public void onImagesLoaded(ImageElement[] imageElements) {
					fontPanel = imageElements[0];
					drawTick();
				}
			});
		}
		else
			drawTick();

	}

	void drawTick() {
		int displayedGenomeWidth = (window.getEndOnGenome() - window.getStartOnGenome());
		if (displayedGenomeWidth < 0)
			displayedGenomeWidth = -displayedGenomeWidth;

		if (displayedGenomeWidth > 300)
			return;

		int fontWidth = 7;
		int fontHeight = 13;
		canvas.setLineWidth(0.5f);
		canvas.setGlobalAlpha(1f);
		canvas.setStrokeStyle(new Color("#EEDDDD"));
		for (int s = (window.getStartOnGenome() / 10) * 10; s < window.getEndOnGenome(); s += 10L) {

			if (s <= 0)
				s = 1;

			float x = window.convertToPixelX(s);

			canvas.beginPath();
			canvas.moveTo(x + 0.5f, 0);
			canvas.lineTo(x + 0.5f, RIBBON_HEIGHT);
			canvas.stroke();

			String tick = Long.toString(s);
			for (int i = 0; i < tick.length(); ++i) {
				char c = tick.charAt(i);
				canvas.drawImage(fontPanel, (c) * fontWidth, 0, fontWidth, fontHeight, (int) x + i * fontWidth + 3, 2, fontWidth, fontHeight);
			}

		}
	}

	public void setWindow(TrackWindow w) {
		this.window = new TrackWindow(w.getPixelWidth() - 100, w.getStartOnGenome(), w.getEndOnGenome());

		redraw();
	}

	public void setGap(int indexOnGenome, int gapLength) {
		ribbonPoint.add(new RibbonCrease(RibbonType.GAP, indexOnGenome, gapLength));
	}

	public void setFold(int indexOnGenome, int foldLength) {
		ribbonPoint.add(new RibbonCrease(RibbonType.FOLD, indexOnGenome, foldLength));
	}

}
