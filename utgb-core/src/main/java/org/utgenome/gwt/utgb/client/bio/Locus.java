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
// Locus.java
// Since: 2009/02/17
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;

/**
 * Locus on a genome sequence
 * 
 * @author leo
 * 
 */
public class Locus implements Serializable, Comparable<Locus> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	String name = "locus";
	long start = -1; // 1-origin
	long end = -1;
	String strand = "?";
	String color = null;

	public Locus() {
	}

	public Locus(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public Locus(String name, long start, long end) {
		this.name = name;
		this.start = start;
		this.end = end;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getViewStart() {
		if (start <= end)
			return start;
		else
			return end;
	}

	public long getViewEnd() {
		if (start <= end)
			return end;
		else
			return start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public String getStrand() {
		return strand;
	}

	public void setStrand(String strand) {
		this.strand = strand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int compareTo(Locus other) {
		long diff = this.getViewStart() - other.getViewStart();
		if (diff == 0) {
			return this.hashCode() - other.hashCode();
		}
		else
			return diff < 0 ? -1 : 1;
	}

	public boolean hasOverlap(Locus other) {
		long s1 = getViewStart();
		long e1 = getViewEnd();
		long s2 = other.getViewStart();
		long e2 = other.getViewEnd();

		return s1 <= e2 && s2 <= e1;
	}

}
