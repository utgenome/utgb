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
// GeneCanvas.java
// Since: Jul 8, 2008
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-core/src/main/java/org/utgenome/gwt/utgb/client/canvas/GeneCanvas.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.utgenome.gwt.utgb.client.bio.CytoBand;
import org.xerial.util.log.Logger;

/**
 * drawing chromosome map
 * 
 * @author yoshimura
 * 
 */
public class ChromosomeMapCanvas {

	private static Logger _logger = Logger.getLogger(ChromosomeMapCanvas.class);

	private final HashMap<String, ChromosomeWindow> windows;
	private BufferedImage image;
	private Graphics2D g;

	private int canvasWidth;
	private int canvasHeight;
	private int chromHeight = 10;
	private int chromMargin = 10;
	private int charHeight = 10;

	private boolean isRotate = false;

	private long maxChromosomeLength = 0;

	private boolean isLighter = false;
	private double lighterRate = 0.7;

	public ChromosomeMapCanvas(int pixelWidth, int pixelHeight, HashMap<String, ChromosomeWindow> windows) {
		this.windows = windows;
		setPixelSize(pixelWidth, pixelHeight);

		for (String chrom : windows.keySet()) {
			if (maxChromosomeLength < windows.get(chrom).getRange()) {
				maxChromosomeLength = windows.get(chrom).getRange();
			}
		}
	}

	public void setPixelSize(int width, int height) {
		canvasWidth = width;
		canvasHeight = height;

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.createGraphics();
	}

	public BufferedImage getBufferedImage() {
		return image;
	}

	public void setChromHeight(int height) {
		this.chromHeight = height;
	}

	public void setChromMargin(int margin) {
		this.chromMargin = margin;
	}

	public void setCharHeight(int charHeight) {
		this.charHeight = charHeight;
	}

	public void setPixelHeight(int height) {
		this.canvasHeight = height;
		setPixelSize(canvasWidth, canvasHeight);
	}

	public void setPixelWidth(int width) {
		this.canvasWidth = width;
		setPixelSize(canvasWidth, canvasHeight);
	}

	public static int width(int x1, int x2) {
		return (x1 < x2) ? x2 - x1 : x1 - x2;
	}

	public boolean isLighter() {
		return isLighter;
	}

	public void setLighter(boolean isLighter) {
		this.isLighter = isLighter;
	}

	public double getLighterRate() {
		return lighterRate;
	}

	public void setLighterRate(double lighterRate) {
		this.lighterRate = lighterRate;
	}

	public void setRotate() {
		this.isRotate = true;
	}

	// draw Chromosomes
	public <E extends CytoBand> void draw(List<E> cytoBandList) {

		// calc left margin
		int chrNameWidth = 0;

		if (!isRotate) {
			for (String chromName : windows.keySet()) {
				Font f = new Font("SansSerif", Font.PLAIN, charHeight);
				g.setFont(f);

				FontMetrics fontMetrics = g.getFontMetrics();
				if (chrNameWidth < fontMetrics.stringWidth(chromName)) {
					chrNameWidth = fontMetrics.stringWidth(chromName);
				}
			}
		}
		else {
			chrNameWidth = charHeight * 2;
		}
		for (String chromName : windows.keySet()) {
			windows.get(chromName).setLeftMargin(chrNameWidth);
		}

		if (!isRotate) {
			setPixelHeight((windows.size()) * (chromHeight + chromMargin) + chromMargin);
		}
		else {
			chromHeight = ((canvasWidth - chromMargin) / windows.size()) - chromMargin;
			setPixelWidth((windows.size()) * (chromHeight + chromMargin) + chromMargin);
		}

		// draw CytoBand
		for (CytoBand c : cytoBandList) {
			drawCytoBand(c, windows.get(c.getChrom()));
		}

		// draw ChromosomeName
		for (String chrName : windows.keySet()) {
			drawChrName(chrName, windows.get(chrName).getRank());
		}
	}

	// draw a CytoBand
	public void drawCytoBand(CytoBand c, ChromosomeWindow w) {

		int start = w.getXPosOnWindow(c.getStart(), windowWidth(w));
		int end = w.getXPosOnWindow(c.getEnd(), windowWidth(w));

		Font f = new Font("SansSerif", Font.PLAIN, charHeight);
		g.setFont(f);
		FontMetrics fontMetrics = g.getFontMetrics();

		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}

		if (((!isRotate && start <= canvasWidth) || (isRotate && start <= canvasHeight)) && end >= 0) {
			int offset = w.getRank() * (chromHeight + chromMargin) + chromMargin;

			if (c.getGieStain() != null && c.getGieStain().equals("acen")) {
				//draw Centromere
				if (c.getName().startsWith("p")) {
					drawTriangle(start, end, offset, getCytoBandColor(c));
				}
				else {
					drawTriangle(end, start, offset, getCytoBandColor(c));
				}
			}
			else {
				// draw Chromosome
				//canvas.setGlobalAlpha(0.5f);
				fillRect(start, end, offset, getCytoBandColor(c));
				drawRect(start, end, offset, getFrameColor());
				//canvas.setGlobalAlpha(1f);

				// Then Band Width longer than Name String, drawing Band Name.
				if (end - start > fontMetrics.stringWidth(c.getName()) && charHeight <= chromHeight && !isRotate) {
					drawCytoBandName(c, w);
				}
			}
		}
	}

	// draw Mapping data
	public void drawMapping(String[] data) {
		drawMapping(data[1], Integer.valueOf(data[8]), Integer.valueOf(data[9]));
	}

	public void drawMapping(String chrom, int startOnChrom, int endOnChrom) {
		if (startOnChrom <= endOnChrom) {
			drawMapping(chrom, startOnChrom, endOnChrom, '+');
		}
		else {
			drawMapping(chrom, endOnChrom, startOnChrom, '-');
		}
	}

	public void drawMapping(String chrom, int startOnChrom, int endOnChrom, char strand) {
		assert startOnChrom <= endOnChrom;
		Color color;
		if (windows.containsKey(chrom)) {
			int start = windows.get(chrom).getXPosOnWindow(startOnChrom, windowWidth(windows.get(chrom)));
			int end = windows.get(chrom).getXPosOnWindow(endOnChrom, windowWidth(windows.get(chrom)));
			int offset = windows.get(chrom).getRank() * (chromHeight + chromMargin) + chromMargin;

			if (strand == '+') {
				color = new Color(255, 0, 0, 192);
			}
			else {
				color = new Color(0, 0, 255, 192);
			}

			if (start > end) {
				int t = start;
				start = end;
				end = t;
			}
			int w = end - start;
			if (w <= 2)
				w = 2;

			fillRect(start, offset - 3, w, chromHeight + 6, color);
			//			Rectangle2D rect = new Rectangle2D.Double(start, offset - 3, 2, chromHeight + 6);
			//			g.fill(rect);
		}
	}

	public void drawGenomeWindow(String chrom, long startOnChrom, long endOnChrom) {
		if (windows.containsKey(chrom)) {
			int start = windows.get(chrom).getXPosOnWindow(startOnChrom, windowWidth(windows.get(chrom)));
			int end = windows.get(chrom).getXPosOnWindow(endOnChrom, windowWidth(windows.get(chrom)));
			int offset = windows.get(chrom).getRank() * (chromHeight + chromMargin) + chromMargin;

			g.setColor(new Color(128, 64, 255));
			g.setStroke(new BasicStroke(1.0f));

			if (start > end) {
				int t = start;
				start = end;
				end = t;
			}
			int w = end - start;
			if (w <= 3)
				w = 3;

			drawRect(start, offset - 3, w, chromHeight + 6, new Color(128, 64, 255));
			//			Rectangle2D rect = new Rectangle2D.Double(start, offset - 3, w, chromHeight + 6);
			//			g.draw(rect);
		}
	}

	// draw Band Name
	public void drawCytoBandName(CytoBand c, ChromosomeWindow w) {
		Font f = new Font("SansSerif", Font.PLAIN, charHeight);
		g.setFont(f);
		FontMetrics fontMetrics = g.getFontMetrics();
		int fontWidth = fontMetrics.stringWidth(c.getName());

		int start = w.getXPosOnWindow(c.getStart(), windowWidth(w));
		int end = w.getXPosOnWindow(c.getEnd(), windowWidth(w));

		int offset = (w.getRank() + 1) * (chromHeight + chromMargin);

		drawText(c.getName(), (start + end - fontWidth) / 2, offset, getCytoBandNameColor(c));
	}

	public void drawChrName(String chrName, Integer rank) {
		if (!isRotate) {
			drawText(chrName, 0, (rank + 1) * (chromHeight + chromMargin), Color.BLACK);
		}
		else {
			drawText(chrName, charHeight * (rank % 2), rank * (chromHeight + chromMargin) + chromMargin, Color.BLACK);
		}
	}

	public void drawText(String text, int x, int y, Color color) {
		Font f = new Font("SansSerif", Font.PLAIN, charHeight);
		g.setFont(f);
		g.setColor(color);
		if (!isRotate)
			g.drawString(text, x, y - 1);
		else
			g.drawString(text, y - 1, canvasHeight - x);
	}

	public void fillRect(int x1, int x2, int y, Color c) {
		fillRect(x1, y, x2 - x1, chromHeight, c);
	}

	public void fillRect(int x, int y, int width, int height, Color color) {
		Rectangle2D rect;

		if (!isRotate)
			rect = new Rectangle2D.Double(x, y, width, height);
		else
			rect = new Rectangle2D.Double(y, canvasHeight - x - width, height, width);

		g.setColor(color);
		g.fill(rect);
	}

	public void drawRect(int x1, int x2, int y, Color c) {
		drawRect(x1, y, x2 - x1, chromHeight, c);
	}

	public void drawRect(int x, int y, int width, int height, Color color) {
		Rectangle2D rect;

		if (!isRotate)
			rect = new Rectangle2D.Double(x, y, width, height);
		else
			rect = new Rectangle2D.Double(y, canvasHeight - x - width, height, width);

		g.setColor(color);
		g.draw(rect);
	}

	public void drawTriangle(int x1, int x2, int y, Color color) {
		Polygon tri = new Polygon();

		if (!isRotate) {
			tri.addPoint(x1, y);
			tri.addPoint(x1, y + chromHeight);
			tri.addPoint(x2, y + (chromHeight / 2));
		}
		else {
			tri.addPoint(y, canvasHeight - x1);
			tri.addPoint(y + chromHeight, canvasHeight - x1);
			tri.addPoint(y + (chromHeight / 2), canvasHeight - x2);
		}

		g.setColor(color);
		g.fill(tri);
	}

	public void toPNG(OutputStream out) throws IOException {
		ImageIO.write(image, "png", out);
	}

	public Color getCytoBandColor(CytoBand c) {
		Color color;

		if (c.getGieStain() == null) {
			color = Color.WHITE;
		}
		else if (c.getGieStain().equals("gpos100")) {
			color = Color.BLACK;
		}
		else if (c.getGieStain().equals("gpos75")) {
			color = Color.DARK_GRAY;
		}
		else if (c.getGieStain().equals("gpos") || c.getGieStain().equals("gpos50")) {
			color = Color.GRAY;
		}
		else if (c.getGieStain().equals("gpos25")) {
			color = Color.LIGHT_GRAY;
		}
		else if (c.getGieStain().equals("gvar")) {
			color = Color.LIGHT_GRAY;
		}
		else if (c.getGieStain().equals("acen")) {
			color = new Color(150, 25, 25);
		}
		else if (c.getGieStain().equals("stalk")) {
			color = Color.GRAY;
		}
		else {
			color = Color.WHITE;
		}

		if (isLighter) {
			return colorLighter(color);
		}
		return color;
	}

	public Color getCytoBandNameColor(CytoBand c) {
		if (c.getGieStain() == null)
			return Color.WHITE;

		if (c.getGieStain().equals("gpos100") || c.getGieStain().equals("gpos75")) {
			return Color.WHITE;
		}
		else {
			if (isLighter) {
				return colorLighter(Color.BLACK);
			}
			return Color.BLACK;
		}
	}

	public Color getFrameColor() {
		if (isLighter) {
			return colorLighter(Color.BLACK);
		}
		return Color.BLACK;
	}

	public Color colorLighter(Color color) {
		return new Color(calcLighterValue(color.getRed()), calcLighterValue(color.getGreen()), calcLighterValue(color.getBlue()));
	}

	public int calcLighterValue(int value) {
		return value + (int) ((255 - value) * lighterRate);
	}

	// adjust window width of each chromosome
	private int windowWidth(ChromosomeWindow w) {
		double rate = 1.0;
		double ratio = 1.0 - ((1.0 - ((double) w.getRange() / (double) maxChromosomeLength)) / rate);

		if (!isRotate) {
			return (int) ((canvasWidth - w.getLeftMargin()) * ratio);
		}
		else {
			return (int) ((canvasHeight - w.getLeftMargin()) * ratio);
		}
	}
}