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
// SAMCanvas.java
// Since: Mar. 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.List;

import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.ui.FormLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

/**
 * Text Alignment Viewer using SAM format data
 * 
 * @author yoshimura
 * 
 */
public class SAMCanvas extends Composite {
	private static final boolean isDebug = true;
	private TrackWindow window;
	private int windowWidth = 800;
	private int windowHeight = 200;
	private int labelWidth = 100;

	private boolean isC2T = false;

	public boolean isC2T() {
		return isC2T;
	}

	public void setC2T(boolean isC2T) {
		this.isC2T = isC2T;
	}

	private String colorMode = null;

	public String getColorMode() {
		return colorMode;
	}

	public void setColorMode(String colorMode) {
		this.colorMode = colorMode;
	}

	// widget
	private FlexTable layoutTable = new FlexTable();
	private GWTCanvas canvas = new GWTCanvas();
	private AbsolutePanel panel = new AbsolutePanel();

	//	private HashMap<String, ImageElement> fontPanel = new HashMap<String, ImageElement>();
	private ImageElement[] fontPanel = null;

	//	private ImageElement fontPanel = null;

	private void initWidget() {
		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);

		panel.add(canvas, 0, 0);
		layoutTable.setWidget(0, 1, panel);
		initWidget(layoutTable);

		sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEOVER | Event.ONMOUSEDOWN);
	}

	public SAMCanvas() {
		initWidget();

	}

	private final int _OFFSET = 6;
	private final int _HEIGHT = 17;
	private int fontWidth = 7;
	private int fontHeight = 13;

	public void clear() {
		canvas.clear();
	}

	void redraw() {
		canvas.clear();
		canvas.setCoordSize(window.getPixelWidth(), windowHeight);
		canvas.setPixelSize(window.getPixelWidth(), windowHeight);
		panel.setPixelSize(window.getPixelWidth(), windowHeight);
	}

	public void setWindow(TrackWindow w, int leftMargin) {
		this.window = new TrackWindow(w.getPixelWidth(), w.getStartOnGenome(), w.getEndOnGenome());

		redraw();
	}

	public void setPixelHeight(int height) {
		canvas.setCoordHeight(height);
		canvas.setPixelHeight(height);
		panel.setHeight(height + "px");
	}

	public void setPixelWidth(int width) {
		canvas.setCoordWidth(width);
		canvas.setPixelWidth(width);
		//		panel.setWidth(width + "px");
		panel.setPixelSize(width, windowHeight);
	}

	public void drawSAMRead(final int count, final SAMRead read) {
		windowHeight = (count + 1) * (_HEIGHT * _OFFSET);
		setPixelHeight(windowHeight);

		if (isDebug) {
			GWT.log("draw read : " + read.qname, null);
			GWT.log("read  : " + read.seq, null);
			GWT.log("ref   : " + read.refSeq, null);
			GWT.log("CIGAR : " + read.cigar, null);
		}

		if (fontPanel == null) {
			// get charactor images
			if (isDebug)
				GWT.log("get images:" + count, null);

			ImageLoader.loadImages(new String[] { GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0x000000", // black
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0xe0a000", // yellow
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0x0000ff", // blue
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0x00a000", // green
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0xff0000" }, // red
					new ImageLoader.CallBack() {
						public void onImagesLoaded(ImageElement[] imageElements) {
							fontPanel = imageElements;
							if (isDebug)
								GWT.log("draw images:" + count, null);

							drawSAMCanvas(count, read);
						}
					});
		}
		else {
			drawSAMCanvas(count, read);
		}
	}

	public void drawSAMRead(final List<SAMRead> readList) {
		windowHeight = readList.size() * (_HEIGHT * _OFFSET);
		setPixelHeight(windowHeight);

		if (fontPanel == null) {
			// get charactor images
			if (isDebug)
				GWT.log("get images", null);

			ImageLoader.loadImages(new String[] { GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0x000000", // black
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0xe0a000", // yellow
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0x0000ff", // blue
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0x00a000", // green
					GWT.getModuleBaseURL() + "utgb-core/FontPanel?fontsize=9.5&color=0xff0000" }, // red
					new ImageLoader.CallBack() {
						public void onImagesLoaded(ImageElement[] imageElements) {
							fontPanel = imageElements;
							draw(readList);
						}
					});
		}
		else {
			draw(readList);
		}
	}

	private void draw(List<SAMRead> readList) {
		int count = 0;

		for (SAMRead read : readList) {
			if (isDebug) {
				GWT.log("draw read : " + read.qname, null);
				GWT.log("read  : " + read.seq, null);
				GWT.log("ref   : " + read.refSeq, null);
				GWT.log("CIGAR : " + read.cigar, null);
			}

			drawSAMCanvas(count, read);
			count++;
		}
	}

	public void drawSAMCanvas(int count, SAMRead read) {
		StringBuilder num = new StringBuilder();
		char readc, refc, diffc;
		int readi = 0, refi = 0, position = 0;

		for (int cursor = 0; cursor < read.cigar.length(); cursor++) {
			char temp = read.cigar.charAt(cursor);
			if ('0' <= temp && temp <= '9') {
				num.append(temp);
			}
			else {
				for (int i = 0; i < Integer.valueOf(num.toString()); i++, position++) {

					if (temp == 'I') {
						readc = read.seq.charAt(readi++);
						refc = '*';
					}
					else if (temp == 'S') {
						refc = ' ';
						readc = read.seq.toLowerCase().charAt(readi++);
					}
					else if (temp == 'D') {
						refc = read.refSeq.charAt(refi++);
						readc = '*';
					}
					else if (temp == 'P') {
						refc = '*';
						readc = '*';
					}
					else if (temp == 'H') {
						position--;
						continue;
					}
					else {
						refc = read.refSeq.charAt(refi++);
						readc = read.seq.charAt(readi++);
					}

					if (temp == 'P' || temp == 'S')
						diffc = ' ';
					else if (refc != readc)
						if (isC2T && readc == 'T' && refc == 'C')
							diffc = 'T';
						else
							diffc = 'X';
					else
						diffc = '|';

					// draw indent line
					canvas.setFillStyle(Color.BLACK);
					if (refc != ' ' && refc != '*') {
						canvas.fillRect(position * fontWidth + 3, _HEIGHT * (count * _OFFSET + 1), fontWidth, 1);
						String indent = String.valueOf(read.getStart() + refi - 1);
						if ((read.getStart() + refi - 1) % (int) (Math.ceil(indent.length() / 5.0) * 5) == 0) {
							canvas.fillRect((position + 0.5) * fontWidth + 2, _HEIGHT * (count * _OFFSET + 1) - 3, 1, 5);
							for (int j = 0; j < indent.length(); j++)
								canvas.drawImage(fontPanel[0], (indent.charAt(j)) * fontWidth, 0, fontWidth, fontHeight, (position + j) * fontWidth + 2,
										_HEIGHT * (count * _OFFSET), fontWidth, fontHeight);
						}
					}

					// draw reference sequence
					canvas.drawImage(fontPanel[getColorInt(refc)], (refc) * fontWidth, 0, fontWidth, fontHeight, position * fontWidth + 3, _HEIGHT
							* (count * _OFFSET + 1) + 3, fontWidth, fontHeight);
					// draw reference sequence
					canvas.drawImage(fontPanel[0], (diffc) * fontWidth, 0, fontWidth, fontHeight, position * fontWidth + 3, _HEIGHT * (count * _OFFSET + 2),
							fontWidth, fontHeight);
					// draw read sequence
					canvas.drawImage(fontPanel[getColorInt(readc)], (readc) * fontWidth, 0, fontWidth, fontHeight, position * fontWidth + 3, _HEIGHT
							* (count * _OFFSET + 3) - 3, fontWidth, fontHeight);
				}

				num = new StringBuilder();
			}
		}

		// draw tag
		for (int cursor = 0; cursor < read.toString().length(); cursor++) {
			canvas.drawImage(fontPanel[0], (read.toString().charAt(cursor)) * fontWidth, 0, fontWidth, fontHeight, cursor * fontWidth + 3, _HEIGHT
					* (count * _OFFSET + 4) - 6, fontWidth, fontHeight);
		}
	}

	public void drawLabelPanel(int count, SAMRead read, AbsolutePanel panel, int leftMargin) {
		panel.setHeight(windowHeight + "px");

		// draw reference label
		//		FixedWidthLabel refSeqLabel = new FixedWidthLabel(read.rname, leftMargin);
		FormLabel refSeqLabel = new FormLabel(read.rname);
		refSeqLabel.setStyleName("search-label");
		// draw read label
		//		FixedWidthLabel readLabel = new FixedWidthLabel(read.qname, leftMargin);
		FormLabel readLabel = new FormLabel(read.qname);
		readLabel.setStyleName("search-label");

		panel.add(refSeqLabel);
		panel.setWidgetPosition(refSeqLabel, 0, _HEIGHT * (count * _OFFSET + 1) + 5);
		panel.add(readLabel);
		panel.setWidgetPosition(readLabel, 0, _HEIGHT * (count * _OFFSET + 3) - 1);
	}

	public void drawLabelPanel(List<SAMRead> readList, AbsolutePanel panel, int leftMargin) {
		panel.setHeight(windowHeight + "px");

		//		FixedWidthLabel[] refSeqLabel = new FixedWidthLabel[readList.size()];
		//		FixedWidthLabel[] readLabel = new FixedWidthLabel[readList.size()];

		FormLabel[] refSeqLabel = new FormLabel[readList.size()];
		FormLabel[] readLabel = new FormLabel[readList.size()];

		int count = 0;
		for (SAMRead read : readList) {
			// draw reference label
			refSeqLabel[count] = new FormLabel(read.rname);
			refSeqLabel[count].setStyleName("search-label");
			// draw read label
			readLabel[count] = new FormLabel(read.qname);
			readLabel[count].setStyleName("search-label");

			panel.add(refSeqLabel[count]);
			panel.setWidgetPosition(refSeqLabel[count], 0, _HEIGHT * (count * _OFFSET + 1) + 5);
			panel.add(readLabel[count]);
			panel.setWidgetPosition(readLabel[count], 0, _HEIGHT * (count * _OFFSET + 3) - 1);

			count++;
		}
	}

	private int getColorInt(char nucleotide) {
		int color = 0; // black
		if (colorMode.equals("nucleotide")) {
			if (nucleotide == 'G' || nucleotide == 'g')
				color = 1; // yellow
			else if (nucleotide == 'C' || nucleotide == 'c')
				color = 2; // blue
			else if (nucleotide == 'A' || nucleotide == 'a')
				color = 3; // green
			else if (nucleotide == 'T' || nucleotide == 't')
				color = 4; // red
		}
		return color;
	}

	public int countNucleotides(String cigar) {
		int count = 0;
		StringBuilder num = new StringBuilder();
		for (int cursor = 0; cursor < cigar.length(); cursor++) {
			char temp = cigar.charAt(cursor);
			if ('0' <= temp && temp <= '9') {
				num.append(temp);
			}
			else if (temp != 'H') {
				count += Integer.valueOf(num.toString()).intValue();
				num = new StringBuilder();
			}
		}
		return count;
	}

	public int getReadWidth(String cigar) {
		return (countNucleotides(cigar) + 1) * fontWidth + 3;
	}
}