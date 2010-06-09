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

/**
 * An implementation of the {@link TrackWindow}
 * 
 * @author leo
 * 
 */
public class TrackWindowImpl implements TrackWindow {
	private final int pixelWidth;
	private final int startIndexOnGenome;
	private final int endIndexOnGenome;

	public TrackWindowImpl(int pixelWidth, int startIndexOnGenome, int endIndexOnGenome) {
		this.pixelWidth = pixelWidth;
		this.startIndexOnGenome = startIndexOnGenome;
		this.endIndexOnGenome = endIndexOnGenome;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#calcXPositionOnWindow(int)
	public int convertToPixelX(int indexOnGenome) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) pixelWidth;
		double v2 = v / (endIndexOnGenome - startIndexOnGenome + 1);
		if (!isReverseStrand())
			return (int) v2;
		else
			return (int) (pixelWidth - v2);
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getGenomeLengthScalePerWindowBit()
	public double getPixelLengthPerBase() {
		return (double) pixelWidth / (double) (endIndexOnGenome - startIndexOnGenome);
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getWindowWidth()
	public int getPixelWidth() {
		return pixelWidth;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getStartOnGenome()
	public int getStartOnGenome() {
		return startIndexOnGenome;
	}

	// @see org.utgenome.gwt.utgb.client.track.TrackWindow#getEndOnGenome()
	public int getEndOnGenome() {
		return endIndexOnGenome;
	}

	public int getSequenceLength() {
		if (startIndexOnGenome <= endIndexOnGenome)
			return endIndexOnGenome - startIndexOnGenome;
		else
			return startIndexOnGenome - endIndexOnGenome;
	}

	public int convertToGenomePosition(int xOnWindow) {
		if (getStartOnGenome() <= getEndOnGenome()) {
			double genomeLengthPerBit = (double) (endIndexOnGenome - startIndexOnGenome) / (double) pixelWidth;
			return (int) (startIndexOnGenome + xOnWindow * genomeLengthPerBit);
		}
		else {
			// reverse strand
			double genomeLengthPerBit = (double) (startIndexOnGenome - endIndexOnGenome) / (double) pixelWidth;
			return (int) (endIndexOnGenome + (pixelWidth - xOnWindow) * genomeLengthPerBit);
		}
	}

	public boolean equals(TrackWindow window) {
		return sameRangeWith(window) && (this.pixelWidth == window.getPixelWidth());
	}

	public boolean sameRangeWith(TrackWindow window) {
		return this.startIndexOnGenome == window.getStartOnGenome() && this.endIndexOnGenome == window.getEndOnGenome();
	}

	public boolean isReverseStrand() {
		return getStartOnGenome() > getEndOnGenome();
	}

	public TrackWindow newWindow(int newStartOnGenome, int newEndOnGenome) {
		return new TrackWindowImpl(this.pixelWidth, newStartOnGenome, newEndOnGenome);
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
		return this.getPixelWidth() == other.getPixelWidth() && this.getSequenceLength() == other.getSequenceLength();
	}

	public TrackWindow mask(TrackWindow mask) {

		int s, e, pixelWidth;
		if (this.getStartOnGenome() < mask.getStartOnGenome()) {
			s = this.getStartOnGenome();
			e = mask.getStartOnGenome();
			pixelWidth = convertToPixelX(e);
		}
		else {
			s = mask.getEndOnGenome();
			e = this.getEndOnGenome();
			pixelWidth = this.getPixelWidth() - convertToPixelX(s);
		}

		return new TrackWindowImpl(pixelWidth, s, e);
	}

}
