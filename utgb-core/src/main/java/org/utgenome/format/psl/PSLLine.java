/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// UTGB Common Project
//
// PSLLine.java
// Since: Jun 5, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.psl;

public class PSLLine {
	int matches;
	int misMatches;
	int repMathces;
	int nCount;
	int qNumInsert;
	int qBaseInsert;
	int tNumInsert;
	int tBaseInsert;
	String strand;
	String qName;
	int qSize;
	int qStart;
	int qEnd;
	String tName;
	int tSize;
	int tStart;
	int tEnd;
	int blockCount;
	int[] blockSizes;
	int[] qStarts;
	int[] tStarts;
	
	public PSLLine()
	{}

	public int getBlockCount() {
		return blockCount;
	}

	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}

	public int[] getBlockSizes() {
		return blockSizes;
	}

	public void setBlockSizes(int[] blockSizes) {
		this.blockSizes = blockSizes;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public int getMisMatches() {
		return misMatches;
	}

	public void setMisMatches(int misMatches) {
		this.misMatches = misMatches;
	}

	public int getNCount() {
		return nCount;
	}

	public void setNCount(int count) {
		nCount = count;
	}

	public int getQBaseInsert() {
		return qBaseInsert;
	}

	public void setQBaseInsert(int baseInsert) {
		qBaseInsert = baseInsert;
	}

	public int getQEnd() {
		return qEnd;
	}

	public void setQEnd(int end) {
		qEnd = end;
	}

	public String getQName() {
		return qName;
	}

	public void setQName(String name) {
		qName = name;
	}

	public int getQNumInsert() {
		return qNumInsert;
	}

	public void setQNumInsert(int numInsert) {
		qNumInsert = numInsert;
	}

	public int getQSize() {
		return qSize;
	}

	public void setQSize(int size) {
		qSize = size;
	}

	public int getQStart() {
		return qStart;
	}

	public void setQStart(int start) {
		qStart = start;
	}

	public int[] getQStarts() {
		return qStarts;
	}

	public void setQStarts(int[] starts) {
		qStarts = starts;
	}

	public int getRepMathces() {
		return repMathces;
	}

	public void setRepMathces(int repMathces) {
		this.repMathces = repMathces;
	}

	public String getStrand() {
		return strand;
	}

	public void setStrand(String strand) {
		this.strand = strand;
	}

	public int getTBaseInsert() {
		return tBaseInsert;
	}

	public void setTBaseInsert(int baseInsert) {
		tBaseInsert = baseInsert;
	}

	public int getTEnd() {
		return tEnd;
	}

	public void setTEnd(int end) {
		tEnd = end;
	}

	public String getTName() {
		return tName;
	}

	public void setTName(String name) {
		tName = name;
	}

	public int getTNumInsert() {
		return tNumInsert;
	}

	public void setTNumInsert(int numInsert) {
		tNumInsert = numInsert;
	}

	public int getTSize() {
		return tSize;
	}

	public void setTSize(int size) {
		tSize = size;
	}

	public int getTStart() {
		return tStart;
	}

	public void setTStart(int start) {
		tStart = start;
	}

	public int[] getTStarts() {
		return tStarts;
	}

	public void setTStarts(int[] starts) {
		tStarts = starts;
	}
	
}




