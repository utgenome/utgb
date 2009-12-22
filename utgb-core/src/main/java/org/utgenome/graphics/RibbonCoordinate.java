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
// RibbonCoordinate.java
// Since: Feb 17, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.graphics;

import java.util.ArrayList;

import org.utgenome.gwt.utgb.client.bio.ReferenceSequence;

/**
 * Coordinate systems for handling insertion and deletions of genome sequences
 * 
 * @author leo
 * 
 */
public class RibbonCoordinate {

	/**
	 * list of insertion positions on the genome display. Each insertion position is an offset from the start value.
	 */
	private ArrayList<Integer> insertionPosition = new ArrayList<Integer>();
	/**
	 * list of deletion positions on the genome display. Each deletion position is an offset from the start value.
	 */
	private ArrayList<Integer> deletionPosition = new ArrayList<Integer>();

	/**
	 * start position of the coordinate on the genome sequence
	 */
	private long start;
	/**
	 * coordinate length
	 */
	private int length;

	/**
	 * Given a reference sequence with indicators of insertion and deletion, create a new RibbonCoordinate instance.
	 * 
	 * <li>sequence: e.g. AC--GT <li>gap (insertion): ---- < <li>deletion
	 * 
	 * 
	 * @param start
	 * @param referenceSequence
	 * 
	 * @return
	 */
	public static RibbonCoordinate createRibbonFromReferenceSequence(long start, long end, String referenceSequence, String strand) {

		int length = referenceSequence.length();
		for (int i = 0; i < length; ++i) {
			char base = referenceSequence.charAt(i);
			switch (base) {
			case '{':
				break;
			case '}':
				break;
			case '[':
				break;
			case ']':
				break;
			case '-':
				break;
			}

		}

		return null;
	}

	public static RibbonCoordinate createRibbonFromReferenceSequence(ReferenceSequence ref, int numBases) {
		return createRibbonFromReferenceSequence(ref.getStart(), ref.getStart() + numBases, ref.getSequence(), ref.getStrand());
	}

}
