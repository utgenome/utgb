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
// Since: Jan 17, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.xerial.util.log.Logger;

/**
 * A canvas class for drawing genes. Modify this implementation so that it can handle Ribbon coordinate
 * 
 * @author leo
 * 
 */
public class GenomeCanvas {

	private static Logger _logger = Logger.getLogger(GenomeCanvas.class);

	private final GenomeWindow window;
	private BufferedImage image;
	private Graphics2D g;
	private int canvasWidth;
	private int canvasHeight;

	private int yOffset = 0;

	private boolean isReverse = false;

	private ArrayList<Integer> lastGeneEnds = new ArrayList<Integer>();

	private int geneHeight = 5;

	public void setGeneHeight(int height) {
		this.geneHeight = height;
	}

	public Graphics2D getGraphics() {
		return g;
	}

	public GenomeWindow getGenomeWindow() {
		return window;
	}

	public GenomeCanvas(int width, int height, GenomeWindow window) {
		this.window = window;
		setPixelSize(width, height);

		isReverse = window.getReverse();
		if (!isReverse)
			this.lastGeneEnds.add(Integer.MIN_VALUE);
		else
			this.lastGeneEnds.add(Integer.MAX_VALUE);
	}

	public int getWidth() {
		return canvasWidth;
	}

	public BufferedImage getBufferedImage() {
		return image;
	}

	public void drawTag(long startIndexOnGenome, long endIndexOnGenome) {
		drawTag(startIndexOnGenome, endIndexOnGenome, Color.GRAY);
	}

	public void drawTag(long startIndexOnGenome, long endIndexOnGenome, Color color) {
		int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
		int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);

		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}

		int width = end - start;
		if (width <= 0)
			width = 1;

		drawRect(start, yOffset, width, geneHeight - 1, color);
		yOffset += geneHeight;
		if (yOffset > canvasHeight)
			yOffset = 0;
	}

	public void drawGeneRect(long startIndexOnGenome, long endIndexOnGenome, int yOffset, int height, Color color) {
		int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
		int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);

		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}

		int width = end - start;
		if (width <= 0)
			width = 1;

		if (start <= canvasWidth && end >= 0)
			drawRect(start, yOffset, width, height, color);
	}

	public void drawRect(int x, int y, int width, int height, Color color) {
		Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
		g.setColor(color);
		g.fill(rect);
	}

	public void drawBase(String text, long startIndexOnGenome, long endIndexOnGenome, int yOffset, float fontSize, Color color) {
		int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
		int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);
		int drawStart;

		Font f = new Font("SansSerif", Font.PLAIN, 1);
		f = f.deriveFont(fontSize);
		g.setFont(f);

		FontMetrics fontMetrics = g.getFontMetrics();
		int fontWidth = fontMetrics.stringWidth(text);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);

		if (isReverse) {
			int temp = start;
			start = end;
			end = temp;
		}

		drawStart = (int) (start + (end - start) / 2.0f - fontWidth / 2.0f);
		if (drawStart < 0)
			drawStart = end;

		g.drawString(text, drawStart, yOffset);

	}

	/**
	 * @param text
	 * @param startIndexOnGenome
	 *            font range start
	 * @param endIndexOnGenome
	 *            font range end
	 * @param yOffset
	 *            offset value of Y-axis
	 * @param fontSize
	 * @param color
	 * @throws IOException
	 */
	public void drawText(String text, long startIndexOnGenome, long endIndexOnGenome, int yOffset, float fontSize, Color color) {
		int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
		int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);
		int drawStart;

		Font f = new Font("Arial", Font.PLAIN, 1);
		f = f.deriveFont(fontSize);
		g.setFont(f);

		FontMetrics fontMetrics = g.getFontMetrics();
		int fontWidth = fontMetrics.stringWidth(text);
		//		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(color);

		if (isReverse) {
			int temp = start;
			start = end;
			end = temp;
		}

		drawStart = (int) (start + (end - start) / 2.0f - fontWidth / 2.0f);
		if (drawStart < 0)
			drawStart = end;

		g.drawString(text, drawStart, yOffset);
	}

	public void drawText(String text, long startIndexOnGenome, long endIndexOnGenome, int yOffset, int fontSize, Color color) {
		int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
		int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);
		int drawStart;

		g.setFont(new Font("Arial", Font.PLAIN, fontSize));
		FontMetrics fontMetrics = g.getFontMetrics();
		int fontWidth = fontMetrics.stringWidth(text) + 2;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		g.setColor(color);

		if (isReverse) {
			int temp = start;
			start = end;
			end = temp;
		}
		drawStart = start - fontWidth;
		if (drawStart < 0)
			drawStart = end + 2;

		g.drawString(text, drawStart, yOffset + (int) (fontSize * 0.85));
	}

	public void drawLocusLabel(String text, long startIndexOnGenome, long endIndexOnGenome, int yOffset, float fontSize, Color color) {
		int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
		int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);

		int drawStart;

		Font f = new Font("Arial", Font.PLAIN, 1);
		f = f.deriveFont(fontSize);
		g.setFont(f);

		FontMetrics fontMetrics = g.getFontMetrics();
		int fontWidth = fontMetrics.stringWidth(text);
		//	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(color);

		if (isReverse) {
			int temp = start;
			start = end;
			end = temp;
		}

		drawStart = (start - fontWidth) - 1;

		g.drawString(text, drawStart, yOffset);
	}

	public void outputImage(HttpServletResponse response, final String imageType) throws IOException {
		response.setContentType("image/" + imageType);
		ImageIO.write(image, imageType, response.getOutputStream());
	}

	public void setPixelSize(int width, int height) {
		canvasWidth = width;
		canvasHeight = height;

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();

	}

	public void drawLine(long x1, long y1, long x2, long y2, Color lineColor) {
		g.setColor(lineColor);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawLine((int) (window.getXPosOnWindow(x1, canvasWidth) + 0.5f), (int) y1, (int) (window.getXPosOnWindow(x2, canvasWidth) - 0.5f), (int) y2);
	}

	public void setPixelHeight(int height) {

		canvasHeight = height;

		image = new BufferedImage(canvasWidth, height, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();

	}

	public int getFontWidth(String text) {
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		return metrics.stringWidth(text);
	}

	public int getXPosOnWindow(long indexOnGenome) {
		return window.getXPosOnWindow(indexOnGenome, canvasWidth);
	}

	public int getOffset(String text, int fontSize, long startIndexOnGenome, long endIndexOnGenome, int gapWidth) {
		int offset = 0;
		boolean isSpace = false;

		int start = window.getXPosOnWindow(startIndexOnGenome, canvasWidth);
		int end = window.getXPosOnWindow(endIndexOnGenome, canvasWidth);
		if (isReverse) {
			int temp = start;
			start = end;
			end = temp;
		}

		g.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(text);

		// calculate offset
		if (!isReverse) {
			for (int i = 0; i < lastGeneEnds.size(); i++) {
				if (start - textWidth <= lastGeneEnds.get(i).intValue()) {
					offset++;
				}
				else {
					lastGeneEnds.set(i, Integer.valueOf(getEndIndex(text, fontSize, start, end, gapWidth)));
					isSpace = true;
					break;
				}
			}
			if (!isSpace) {
				lastGeneEnds.add(getEndIndex(text, fontSize, start, end, gapWidth));
			}
		}
		else {
			for (int i = 0; i < lastGeneEnds.size(); i++) {
				if (getEndIndex(text, fontSize, start, end, gapWidth) >= lastGeneEnds.get(i).intValue()) {
					offset++;
				}
				else {
					lastGeneEnds.set(i, Integer.valueOf(start - textWidth));
					isSpace = true;
					break;
				}
			}
			if (!isSpace) {
				lastGeneEnds.add(start - textWidth);
			}
		}

		return offset;
	}

	public int getEndIndex(String text, int fontSize, int start, int end, int gapWidth) {
		String tempText = "a";

		g.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
		FontMetrics fontMetrics = g.getFontMetrics();

		int fontWidth = fontMetrics.stringWidth(text);
		int tempWidth = fontMetrics.stringWidth(tempText);

		if (start - fontWidth < 0) {
			fontWidth += tempWidth * gapWidth;
		}
		else {
			fontWidth = tempWidth * gapWidth;
		}
		return end + fontWidth;
	}
}
