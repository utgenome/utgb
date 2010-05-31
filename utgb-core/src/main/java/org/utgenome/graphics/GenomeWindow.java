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
// GenomeWindow.java
// Since: Jan 22, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.graphics;

public class GenomeWindow {
	public final long startIndexOnGenome;
	public final long endIndexOnGenome;
	private long range;
	private boolean isReverse = false;

	public GenomeWindow(long startIndexOnGenome, long endIndexOnGenome) {
		this.startIndexOnGenome = startIndexOnGenome;
		this.endIndexOnGenome = endIndexOnGenome;
		// reverse ?
		if (startIndexOnGenome > endIndexOnGenome)
			this.isReverse = true;
		// inclusive
		range = width(endIndexOnGenome, startIndexOnGenome) + 1;
	}

	public int toGenomeLength(int pixelLength, int windowWidth) {
		double genomeLengthPerPixel = (double) (endIndexOnGenome - startIndexOnGenome) / (double) windowWidth;
		return (int) genomeLengthPerPixel * pixelLength;
	}

	public int pixelPositionOnWindow(long indexOnGenome, int windowWidth) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) windowWidth;
		double v2 = v / (double) (endIndexOnGenome - startIndexOnGenome);
		return (int) v2;
	}

	public int calcGenomePosition(int xOnWindow, int windowWidth) {
		if (startIndexOnGenome <= endIndexOnGenome) {
			double genomeLengthPerBit = (double) (endIndexOnGenome - startIndexOnGenome) / (double) windowWidth;
			return (int) (startIndexOnGenome + (double) xOnWindow * genomeLengthPerBit);
		}
		else {
			// reverse strand
			double genomeLengthPerBit = (double) (startIndexOnGenome - endIndexOnGenome) / (double) windowWidth;
			return (int) (endIndexOnGenome + (double) (windowWidth - xOnWindow) * genomeLengthPerBit);
		}
	}

	public static long width(long x1, long x2) {
		return (x1 < x2) ? x2 - x1 : x1 - x2;
	}

	public int getXPosOnWindow(long indexOnGenome, int canvasWidth) {
		double v = (indexOnGenome - startIndexOnGenome) * (double) canvasWidth;

		if (isReverse)
			v = canvasWidth - v;

		double v2 = v / (double) range;
		return (int) v2;
	}

	public long getGenomeRange() {
		return range;
	}

	public boolean getReverse() {
		return isReverse;
	}

	public boolean hasOverlap(long startOnGenome, long endOnGenome) {
		if (isReverse) {
			return startOnGenome >= this.endIndexOnGenome && endOnGenome <= this.startIndexOnGenome;
		}
		else {
			return startOnGenome <= this.endIndexOnGenome && endOnGenome >= this.startIndexOnGenome;
		}
	}
}
