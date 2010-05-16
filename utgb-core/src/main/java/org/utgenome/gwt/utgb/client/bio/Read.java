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
// Read.java
// Since: May 16, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.io.Serializable;

import org.utgenome.gwt.utgb.client.canvas.ReadVisitor;

/**
 * A range on a genome sequence
 * 
 * @author leo
 * 
 */
public class Read implements Serializable, AcceptReadVisitor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int start = -1; // 1-origin (inclusive, -1 means undefined value)
	public int end = -1; // 1-origin (exclusive, -1 means undefined value)
	public byte strand = '+';

	public Read() {

	}

	public Read(int start, int end) {
		this.start = start;
		this.end = end;
		correctInterval();
	}

	public void adjustToOneOrigin() {
		if (start != -1)
			start += 1;
		if (end != -1)
			end += 1;
	}

	/**
	 * Get the start position of the locus. (start <= end)
	 * 
	 * @return the start position
	 */
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
		correctInterval();
	}

	protected void correctInterval() {
		// do not swap start and end when one of them is undefined
		if (start == -1 || end == -1)
			return;

		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
	}

	/**
	 * Get the end position of the locus (start <= end)
	 * 
	 * @return the end position
	 */
	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
		correctInterval();
	}

	public char getStrand() {
		return (char) strand;
	}

	public boolean isSense() {
		return '+' == strand;
	}

	public boolean isAntiSense() {
		return '-' == strand;
	}

	public void setStrand(char strand) {
		this.strand = (byte) strand;
	}

	public void accept(ReadVisitor visitor) {
		visitor.visitRead(this);
	}

}
