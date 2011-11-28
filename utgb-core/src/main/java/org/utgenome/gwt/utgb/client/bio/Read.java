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

/**
 * An interval with starnd information
 * 
 * @author leo
 * 
 */
public class Read extends Interval {

	public static enum ReadType {
		INTERVAL, BED, SAM, BAM, BSS, WIG, URI
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String color;
	private byte strand = '+';

	public Read() {
		super();
	}

	public Read(int start, int end) {
		super(start, end);
	}

	public Read(String name, int start, int end) {
		super(start, end);
		this.name = name;
	}

	protected Read(Read other) {
		super(other.start, other.end);
		this.name = other.name;
		this.color = other.color;
		this.strand = other.strand;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public char getStrand() {
		return (char) strand;
	}

	@Override
	public boolean isSense() {
		return '+' == strand;
	}

	@Override
	public boolean isAntiSense() {
		return '-' == strand;
	}

	public void setStrand(String strand) {
		if (strand != null && strand.length() > 0)
			this.strand = (byte) strand.charAt(0);
	}

	@Override
	public void accept(GenomeRangeVisitor visitor) {
		visitor.visitRead(this);
	}

	public Interval upstreamRegion(int length) {
		if (isSense()) {
			return new Interval(start - length, start);
		}
		else {
			return new Interval(end, end + length);
		}
	}

	public Interval downstreamRegion(int length) {
		if (isSense()) {
			return new Interval(end, end + length);
		}
		else {
			return new Interval(start - length, start);
		}
	}

}
