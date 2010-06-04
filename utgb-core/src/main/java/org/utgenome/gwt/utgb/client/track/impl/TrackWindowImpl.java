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

import org.utgenome.gwt.utgb.client.bio.OnGenome;
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
	private final int windowWidth;
	private final int startIndexOnGenome;
	private final int endIndexOnGenome;

	public TrackWindowImpl(int windowWidth, int startIndexOnGenome, int endIndexOnGenome) {
		this.windowWidth = windowWidth;
		this.startIndexOnGenome = startIndexOnGenome;
		this.endIndexOnGenome = endIndexOnGenome;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#calcXPositionOnWindow(int)
	public int getPixelXOf(int indexOnGenome) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) windowWidth;
		double v2 = v / (endIndexOnGenome - startIndexOnGenome + 1);
		if (!isReverseStrand())
			return (int) v2;
		else
			return (int) (windowWidth - v2);
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getGenomeLengthScalePerWindowBit()
	public double getPixelLengthPerBase() {
		return (double) windowWidth / (double) (endIndexOnGenome - startIndexOnGenome);
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getWindowWidth()
	public int getPixelWidth() {
		return windowWidth;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getStartOnGenome()
	public int getStartOnGenome() {
		return startIndexOnGenome;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getEndOnGenome()
	public int getEndOnGenome() {
		return endIndexOnGenome;
	}

	public int getWidth() {
		if (startIndexOnGenome <= endIndexOnGenome)
			return endIndexOnGenome - startIndexOnGenome;
		else
			return startIndexOnGenome - endIndexOnGenome;
	}

	public int calcGenomePosition(int xOnWindow) {
		if (getStartOnGenome() <= getEndOnGenome()) {
			double genomeLengthPerBit = (double) (endIndexOnGenome - startIndexOnGenome) / (double) windowWidth;
			return (int) (startIndexOnGenome + xOnWindow * genomeLengthPerBit);
		}
		else {
			// reverse strand
			double genomeLengthPerBit = (double) (startIndexOnGenome - endIndexOnGenome) / (double) windowWidth;
			return (int) (endIndexOnGenome + (windowWidth - xOnWindow) * genomeLengthPerBit);
		}
	}

	public void toXML(XMLWriter xmlWriter) {
		xmlWriter.element("trackWindow", new XMLAttribute().add("start", getStartOnGenome()).add("end", getEndOnGenome()).add("width", getPixelWidth()));
	}

	public boolean equals(TrackWindow window) {
		return sameRangeWith(window) && (this.windowWidth == window.getPixelWidth());
	}

	public boolean sameRangeWith(TrackWindow window) {
		return this.startIndexOnGenome == window.getStartOnGenome() && this.endIndexOnGenome == window.getEndOnGenome();
	}

	public boolean isReverseStrand() {
		return getStartOnGenome() > getEndOnGenome();
	}

	public TrackWindow newWindow(int newStartOnGenome, int newEndOnGenome) {
		return new TrackWindowImpl(this.windowWidth, newStartOnGenome, newEndOnGenome);
	}

	public TrackWindow newPixelWidthWindow(int pixelSize) {
		return new TrackWindowImpl(pixelSize, this.startIndexOnGenome, this.endIndexOnGenome);
	}

	public boolean hasOverlapWith(OnGenome g) {

		int s1 = getStartOnGenome();
		int e1 = getEndOnGenome();
		int s2 = g.getStart();
		int e2 = g.getEnd();

		return s1 <= e2 && s2 <= e1;
	}

	public boolean hasSameScale(TrackWindow other) {
		if (other == null)
			return false;
		return this.getPixelWidth() == other.getPixelWidth() && this.getWidth() == other.getWidth();
	}

	public TrackWindow mask(TrackWindow mask) {

		int s, e, pixelWidth;
		if (this.getStartOnGenome() < mask.getStartOnGenome()) {
			s = this.getStartOnGenome();
			e = mask.getStartOnGenome();
			pixelWidth = getPixelXOf(e);
		}
		else {
			s = mask.getEndOnGenome();
			e = this.getEndOnGenome();
			pixelWidth = this.getPixelWidth() - getPixelXOf(s);
		}

		return new TrackWindowImpl(pixelWidth, s, e);
	}

}
