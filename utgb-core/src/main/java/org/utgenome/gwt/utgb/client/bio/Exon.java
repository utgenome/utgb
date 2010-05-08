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
// Exon.java
// Since: Jul 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;

/**
 * Exon
 * 
 * @author leo
 * 
 */
public class Exon implements Serializable, Comparable<Exon> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	int start = -1;
	int end = -1;

	public Exon() {

	}

	public Exon(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public void adjustToOneOrigin() {
		if (start != -1)
			start += 1;
		if (end != -1)
			end += 1;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int compareTo(Exon other) {
		long diff = start - other.getStart();
		if (diff != 0)
			return (int) diff;
		else
			return (int) (end - other.getEnd());
	}

	@Override
	public String toString() {
		return "(" + start + ", " + end + ")";
	}

}
