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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * CytoBand
 * 
 * @author yoshimura
 * 
 */
public class CytoBand implements IsSerializable {
	/**
	 * 
	 */
	String chrom = "chrZZZ";
	long start = -1; // (inclusive, 1-origin)
	long end = -1; // (exclusive, 1-origin)
	String name = "?";
	String gieStain = "?";

	public CytoBand() {
	}

	public CytoBand(String chrom, long start, long end, String name, String gieStain) {
		this.chrom = chrom;
		this.start = start;
		this.end = end;
		this.name = name;
		this.gieStain = gieStain;
	}

	public String getChrom() {
		return chrom;
	}

	public void setChrom(String chrom) {
		this.chrom = chrom;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGieStain() {
		return gieStain;
	}

	public void setGieStain(String gieStain) {
		this.gieStain = gieStain;
	}
}
