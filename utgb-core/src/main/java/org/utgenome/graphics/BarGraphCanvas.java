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
// BarGraphCanvas.java
// Since: 2009/01/15
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.utgenome.gwt.utgb.client.bio.BarGraphData;

/**
 * BarGraph drawing library
 * 
 * @author leo
 * 
 */
public class BarGraphCanvas {

	private int pixelWidth;
	private int pixelHeight;
	private final GenomeWindow genomeWindow;

	private BufferedImage buffer;
	private Graphics2D g;

	public BarGraphCanvas(int pixelWidth, int pixelHeight, GenomeWindow genomeWindow) {
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;

		this.genomeWindow = genomeWindow;

		buffer = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) buffer.getGraphics();
	}

	public void plot(long xOnGenomePosition, int value) {
		plot(xOnGenomePosition, value, Color.BLUE);
	}

	public void plot(long xOnGenomePosition, int value, Color c) {
		int xOnCanvas = genomeWindow.getXPosOnWindow(xOnGenomePosition, pixelWidth);
		g.setColor(c);
		g.fillRect(xOnCanvas, (pixelHeight - pixelHeight / 4) - value, 1, value);
	}

	public void drawXLabel(String xLabel) {
		g.drawString(xLabel, 100, 90);
	}

	public void draw(BarGraphData barGraphData) {

	}

	public BufferedImage getBufferedImage() {
		return buffer;
	}
}
