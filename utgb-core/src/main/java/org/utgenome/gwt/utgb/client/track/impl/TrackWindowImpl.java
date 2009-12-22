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
// TrackWindowImpl.java
// Since: Jun 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.impl;

import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.xml.XMLAttribute;
import org.utgenome.gwt.utgb.client.util.xml.XMLWriter;

/**
 * An implementation of the {@link TrackWindow}
 * 
 * @author leo
 * 
 */
public class TrackWindowImpl implements TrackWindow {
	private int windowWidth = 700;
	private long startIndexOnGenome = 0;
	private long endIndexOnGenome = 10000;

	public TrackWindowImpl() {
	}

	public TrackWindowImpl(int windowWidth, long startIndexOnGenome, long endIndexOnGenome) {
		super();
		this.windowWidth = windowWidth;
		this.startIndexOnGenome = startIndexOnGenome;
		this.endIndexOnGenome = endIndexOnGenome;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#calcXPositionOnWindow(int)
	public int calcXPositionOnWindow(long indexOnGenome) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) windowWidth;
		double v2 = v / (double) (endIndexOnGenome - startIndexOnGenome);
		return (int) v2;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getGenomeLengthScalePerWindowBit()
	public double getPixelLengthPerBase() {
		return (double) windowWidth / (double) (endIndexOnGenome - startIndexOnGenome);
	}

	public void setStartOnGenome(long startOnGenome) {
		this.startIndexOnGenome = (startOnGenome > 0) ? startOnGenome : 1;
	}

	public void setEndOnGenome(long endOnGenome) {
		this.endIndexOnGenome = (endOnGenome > 0) ? endOnGenome : (this.startIndexOnGenome > 0 ? this.startIndexOnGenome : 1);
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getWindowWidth()
	public int getWindowWidth() {
		return windowWidth;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getStartOnGenome()
	public long getStartOnGenome() {
		return startIndexOnGenome;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getEndOnGenome()
	public long getEndOnGenome() {
		return endIndexOnGenome;
	}

	public long getWidth() {
		if (startIndexOnGenome <= endIndexOnGenome)
			return endIndexOnGenome - startIndexOnGenome;
		else
			return startIndexOnGenome - endIndexOnGenome;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public void set(TrackWindow newWindow) {
		this.windowWidth = newWindow.getWindowWidth();
		setStartOnGenome(newWindow.getStartOnGenome());
		setEndOnGenome(newWindow.getEndOnGenome());
	}

	public int calcGenomePosition(long xOnWindow) {
		if (getStartOnGenome() <= getEndOnGenome()) {
			double genomeLengthPerBit = (double) (endIndexOnGenome - startIndexOnGenome) / (double) windowWidth;
			return (int) (startIndexOnGenome + (double) xOnWindow * genomeLengthPerBit);
		}
		else {
			// reverse strand
			double genomeLengthPerBit = (double) (startIndexOnGenome - endIndexOnGenome) / (double) windowWidth;
			return (int) (endIndexOnGenome + (double) (windowWidth - xOnWindow) * genomeLengthPerBit);
		}
	}

	public void toXML(XMLWriter xmlWriter) {
		xmlWriter.element("trackWindow", new XMLAttribute().add("start", getStartOnGenome()).add("end", getEndOnGenome()).add("width", getWindowWidth()));
	}

	public boolean equals(TrackWindow window) {
		return (this.startIndexOnGenome == window.getStartOnGenome()) && (this.endIndexOnGenome == window.getEndOnGenome())
				&& (this.windowWidth == window.getWindowWidth());
	}

	public boolean isReverseStrand() {
		return getStartOnGenome() > getEndOnGenome();
	}

	public TrackWindow newWindow(long newStartOnGenome, long newEndOnGenome) {
		return new TrackWindowImpl(this.windowWidth, newStartOnGenome, newEndOnGenome);
	}

}
