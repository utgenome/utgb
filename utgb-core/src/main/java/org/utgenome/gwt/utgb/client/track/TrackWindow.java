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
// TrackWindow.java
// Since: Jun 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.io.Serializable;

import org.utgenome.gwt.utgb.client.bio.GenomeRange;

/**
 * {@link TrackWindow} manages a track-window size and a region on the genome displayed in the window. An instance of
 * TrackWindow is immutable, so you can safely copy a reference of TrackWindow to preserve a current track window state.
 * 
 * @author leo
 * 
 */
public class TrackWindow implements Serializable, Comparable<TrackWindow> {

	private static final long serialVersionUID = 1L;

	private final int pixelWidth;
	private final int startIndexOnGenome;
	private final int endIndexOnGenome;

	public TrackWindow() {
		this.pixelWidth = -1;
		this.startIndexOnGenome = -1;
		this.endIndexOnGenome = -1;
	}

	public TrackWindow(int pixelWidth, int startIndexOnGenome, int endIndexOnGenome) {
		this.pixelWidth = pixelWidth;
		this.startIndexOnGenome = startIndexOnGenome;
		this.endIndexOnGenome = endIndexOnGenome;
	}

	public TrackWindow(TrackWindow other) {
		this(other.pixelWidth, other.getStartOnGenome(), other.getEndOnGenome());
	}

	/**
	 * calculate the X position (pixel address) in a track window of a given index on genome.
	 * 
	 * @param indexOnGenome
	 * @return relative X position in a window (0 origin).
	 */
	public int convertToPixelX(int indexOnGenome) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) pixelWidth;
		double v2 = v / (endIndexOnGenome - startIndexOnGenome + 1);
		if (!isReverseStrand())
			return (int) (v2 + 0.5d);
		else
			return (int) (pixelWidth - v2 + 0.5d);
	}

	public double convertToPixelXDouble(int indexOnGenome) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) pixelWidth;
		double v2 = v / (endIndexOnGenome - startIndexOnGenome + 1);
		if (!isReverseStrand())
			return v2;
		else
			return (pixelWidth - v2);
	}

	/**
	 * @param xOnWindow
	 *            calculate the genome index of a given x position in the current window
	 * @return genome position
	 */
	public int convertToGenomePosition(int xOnWindow) {
		if (!isReverseStrand()) {
			double genomeLengthPerBit = (double) (endIndexOnGenome - startIndexOnGenome) / (double) pixelWidth;
			return (int) (startIndexOnGenome + xOnWindow * genomeLengthPerBit);
		}
		else {
			// reverse strand
			double genomeLengthPerBit = (double) (startIndexOnGenome - endIndexOnGenome) / (double) pixelWidth;
			return (int) (endIndexOnGenome + (pixelWidth - xOnWindow) * genomeLengthPerBit);
		}
	}

	public int convertToPixelLength(int rangeOnGenome) {
		return (int) ((pixelWidth * rangeOnGenome) / (double) (endIndexOnGenome - startIndexOnGenome));
	}

	/**
	 * @return pixel length / (genome end - genome start);
	 */
	public double getPixelLengthPerBase() {
		return (double) pixelWidth / (double) (endIndexOnGenome - startIndexOnGenome);
	}

	/**
	 * @return the window size
	 */
	public int getPixelWidth() {
		return pixelWidth;
	}

	/**
	 * @return the sequence width
	 */
	public int getSequenceLength() {
		if (startIndexOnGenome <= endIndexOnGenome)
			return endIndexOnGenome - startIndexOnGenome;
		else
			return startIndexOnGenome - endIndexOnGenome;
	}

	public int getViewStartOnGenome() {
		if (isReverseStrand())
			return endIndexOnGenome;
		else
			return startIndexOnGenome;
	}

	public int getViewEndOnGenome() {
		if (isReverseStrand())
			return startIndexOnGenome;
		else
			return endIndexOnGenome;
	}

	/**
	 * @return start position on the genome currently displayed in the window
	 */
	public int getStartOnGenome() {
		return startIndexOnGenome;
	}

	/**
	 * @return end position on the genome currently displayed in the window
	 */
	public int getEndOnGenome() {
		return endIndexOnGenome;
	}

	public boolean sameRangeWith(TrackWindow window) {
		return this.startIndexOnGenome == window.getStartOnGenome() && this.endIndexOnGenome == window.getEndOnGenome();
	}

	public boolean isReverseStrand() {
		return getStartOnGenome() > getEndOnGenome();
	}

	public boolean isPositiveStrand() {
		return !isReverseStrand();
	}

	public TrackWindow newWindow(int newStartOnGenome, int newEndOnGenome) {
		return new TrackWindow(this.pixelWidth, newStartOnGenome, newEndOnGenome);
	}

	public TrackWindow scroll(int startDiff) {
		return new TrackWindow(this.pixelWidth, this.startIndexOnGenome + startDiff, this.endIndexOnGenome + startDiff);
	}

	public TrackWindow newPixelWidthWindow(int pixelSize) {
		return new TrackWindow(pixelSize, this.startIndexOnGenome, this.endIndexOnGenome);
	}

	public boolean contains(TrackWindow other) {
		int s1 = getViewStartOnGenome();
		int e1 = getViewEndOnGenome();
		int s2 = other.getViewStartOnGenome();
		int e2 = other.getViewEndOnGenome();
		return s1 <= s2 && e2 <= e1;
	}

	public boolean overlapWith(GenomeRange g) {
		int s1 = getStartOnGenome();
		int e1 = getEndOnGenome();
		int s2 = g.getStart();
		int e2 = g.getEnd();

		return s1 <= e2 && s2 <= e1;
	}

	public boolean overlapWith(TrackWindow other) {
		int s1 = getStartOnGenome();
		int e1 = getEndOnGenome();
		int s2 = other.getStartOnGenome();
		int e2 = other.getEndOnGenome();

		return s1 < e2 && s2 < e1;
	}

	public boolean hasSameScaleWith(TrackWindow other) {
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
		return new TrackWindow(pixelWidth, s, e);
	}

	public int compareTo(TrackWindow o) {
		return this.getViewStartOnGenome() - o.getViewStartOnGenome();
	}

	// @see java.lang.Object#equals(java.lang.Object)
	@Override
	public boolean equals(Object o) {
		if (o instanceof TrackWindow) {
			TrackWindow window = (TrackWindow) o;
			return sameRangeWith(window) && (this.pixelWidth == window.getPixelWidth());
		}
		else
			return false;
	}

	public int center() {
		return (int) ((startIndexOnGenome + endIndexOnGenome) / 2.0 + 0.5);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash += 137 * startIndexOnGenome;
		hash += 137 * endIndexOnGenome;
		hash += 137 * pixelWidth;
		return hash / 1987;
	}

	@Override
	public String toString() {
		return "[" + getStartOnGenome() + ", " + getEndOnGenome() + ") pixel width: " + pixelWidth;
	}
}
