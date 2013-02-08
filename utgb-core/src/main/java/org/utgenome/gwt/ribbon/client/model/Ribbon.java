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
// Ribbon.java
// Since: 2010/01/26
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.ribbon.client.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Ribbon for representing indels on a genome sequence
 * 
 * @author leo
 * 
 */
public class Ribbon implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * A segment of the ribbon
	 * 
	 * @author leo
	 * 
	 */
	public static class RibbonSegment {

		public static enum SegmentType {
			GAP, FOLD, NORMAL
		}

		/**
		 * start position on the reference genome
		 */
		public int start;
		/**
		 * end position on the reference genome
		 */
		public int end;

		/**
		 * the length of this ribbon segment
		 */
		public int length;

		/**
		 * GAP, FOLD, or NORMAL
		 */
		public SegmentType type;
		/**
		 * true if this segment is visible to the user, otherwise false
		 */
		public boolean isVisible = true;

		public RibbonSegment(int start, int end, int length, SegmentType type) {
			this.start = start;
			this.end = end;
			this.type = type;
		}
	}

	public ArrayList<RibbonSegment> segment = new ArrayList<RibbonSegment>();

	public Ribbon() {
	}

}
