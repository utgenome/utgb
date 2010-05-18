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

import org.utgenome.gwt.utgb.client.canvas.ReadVisitor;
import org.utgenome.gwt.utgb.client.util.Properties;

/**
 * Locus on a genome sequence.
 * 
 * The interval (start, end) in this class is ensured to be start <= end.
 * 
 * The range specified in this Locus is [start, end) (inclusive, exclusive). For example, when start = 1, end = 5, [1,
 * 5), this locus contains 1, 2, 3 and 4.
 * 
 * @author leo
 * 
 */
public class Locus extends Read implements Serializable, Comparable<Locus> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name;
	String color = null;
	public Properties properties;

	public boolean hasProperties() {
		if (properties == null)
			return false;
		else
			return !properties.isEmpty();
	}

	@Override
	public String toString() {
		return "name:" + name + ", start:" + start + ", end:" + end + ", strand=" + strand;
	}

	public Locus() {
	}

	public Locus(Locus other) {
		this.name = other.name;
		this.start = other.start;
		this.end = other.end;
		this.strand = other.strand;
		this.color = other.color;
	}

	public Locus(int start, int end) {
		this(null, start, end);
	}

	public Locus(String name, int start, int end) {
		this.name = name;
		this.start = start;
		this.end = end;
		correctInterval();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int compareTo(Locus other) {
		long diff = this.getStart() - other.getEnd();
		if (diff == 0) {
			return this.hashCode() - other.hashCode();
		}
		else
			return diff < 0 ? -1 : 1;
	}

	public boolean hasOverlap(Locus other) {
		long s1 = getStart();
		long e1 = getEnd();
		long s2 = other.getStart();
		long e2 = other.getEnd();

		return s1 <= e2 && s2 <= e1;
	}

	@Override
	public void accept(ReadVisitor visitor) {
		visitor.visitLocus(this);
	}

}
