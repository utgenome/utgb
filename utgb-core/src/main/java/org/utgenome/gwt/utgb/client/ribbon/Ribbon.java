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
// Since: 2010/05/20
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ribbon;

import java.util.ArrayList;
import java.util.List;

/**
 * Ribbon is a coordinate for representing insertions to reference and deletions to reference,
 * 
 * <pre>
 * xOffset:            1 2 3 4 5 6 7 8 9
 * normal coordinate:  1 2 3 4 5 6 7 8 9  
 *                     - - - - - - - - - 
 * [insertion]
 * xOffset:            1 2 3 4 5 6 7 8 9 10 11
 * ribbon coordinate:  1 2 3 - - - - - 4  5  6
 *                          /         \
 *                     - - -           -  -  -  
 * -insertion(pos:4, length:5)
 * 
 * 
 * 
 * [deletion] 
 * xOffset:            1 2 3 4 5 6  7  8  9 10 11
 * ribbon coordinate:  1 2 3 7 8 9 10 11 12 13 14
 *                     - - - - - -  -  -  -  -  -
 *                         / \
 *                         ---
 * -deletion(pos:4, length:3)
 * </pre>
 * 
 * @author leo
 * 
 */
public class Ribbon {

	public static enum CreaseType {
		INSERTION, DELETION
	}

	/**
	 * A crease is a point where insertion or deletion occurs.
	 * 
	 * @author leo
	 * 
	 */
	public static class Crease {
		public int x;
		public int length;
		public CreaseType type;

		private Crease(int x, int length, CreaseType type) {
			this.x = x;
			this.length = length;
			this.type = type;
		}

		public boolean overlap(int start, int end) {
			// TODO 
			return false;
		}

		public static Crease newInsertion(int x, int length) {
			return new Crease(x, length, CreaseType.INSERTION);
		}

		public static Crease newDeletion(int x, int length) {
			return new Crease(x, length, CreaseType.DELETION);
		}
	}

	private List<Crease> crease = new ArrayList<Crease>();

	public Ribbon() {

	}

	public List<Crease> getCreaseInRange(int start, int end) {
		ArrayList<Crease> result = new ArrayList<Crease>();
		for (Crease each : crease) {
			// TODO
		}
		return result;
	}

}
