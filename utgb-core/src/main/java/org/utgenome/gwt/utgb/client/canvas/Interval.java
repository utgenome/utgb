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
// Interval.java
// Since: Sep 10, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

/**
 * Read
 * 
 * @author leo
 * 
 */
public class Interval {

	/**
	 * start & end values are public fields
	 */
	public int start;
	public int end;

	public Interval(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public boolean intersectsWith(Interval other) {
		if (start <= other.start)
			return other.start <= end;
		else
			return start <= other.end;
	}

	public boolean contains(Interval other) {
		return (start <= other.start) && (other.end <= end);
	}

	public int length() {
		if (start <= end)
			return end - start;
		else
			return start - end;
	}

}
