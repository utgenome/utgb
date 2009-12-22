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
	long start = -1;
	long end = -1;

	public Exon() {

	}

	public Exon(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public int compareTo(Exon other) {
		long diff = start - other.getStart();
		if (diff != 0)
			return (int) diff;
		else
			return (int) (end - other.getEnd());
	}

	public String toString() {
		return "(" + start + ", " + end + ")";
	}

}
