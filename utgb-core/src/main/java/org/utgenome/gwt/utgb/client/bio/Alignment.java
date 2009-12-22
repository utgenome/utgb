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
// Alignment.java
// Since: Sep 5, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Alignment result
 * 
 * @author leo
 * 
 */
public class Alignment implements IsSerializable {

	private int numMatch;
	private int numMisMatch;
	private int numRepMatch;
	private int numN;

	// query sequence
	private String queryName;
	private int querySize;
	private long queryStart;
	private long queryEnd;

	// target sequence
	private String targetName;
	private int targetSize;
	private long targetStart;
	private long targetEnd;

	private String strand; // "+" or "-"

	// alignment blocks
	private int blockCount = 0;
	private ArrayList<Integer> blockSizeList = new ArrayList<Integer>();
	private ArrayList<Long> queryStartList = new ArrayList<Long>();
	private ArrayList<Long> targetStartList = new ArrayList<Long>();

	public int getNumMatch() {
		return numMatch;
	}

	public int getQueryLen() {
		return querySize;
	}

	public void setQueryLen(int querySize) {
		this.querySize = querySize;
	}

	public int getTargetSize() {
		return targetSize;
	}

	public void setTargetSize(int targetSize) {
		this.targetSize = targetSize;
	}

	public void setNumMatch(int numMatch) {
		this.numMatch = numMatch;
	}

	public int getNumMisMatch() {
		return numMisMatch;
	}

	public void setNumMisMatch(int numMisMatch) {
		this.numMisMatch = numMisMatch;
	}

	public int getNumRepMatch() {
		return numRepMatch;
	}

	public void setNumRepMatch(int numRepMatch) {
		this.numRepMatch = numRepMatch;
	}

	public int getNumN() {
		return numN;
	}

	public void setNumN(int numN) {
		this.numN = numN;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public long getQueryStart() {
		return queryStart;
	}

	public void setQueryStart(long queryStart) {
		this.queryStart = queryStart;
	}

	public long getQueryEnd() {
		return queryEnd;
	}

	public void setQueryEnd(long queryEnd) {
		this.queryEnd = queryEnd;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public long getTargetStart() {
		return targetStart;
	}

	public void setTargetStart(long targetStart) {
		this.targetStart = targetStart;
	}

	public long getTargetEnd() {
		return targetEnd;
	}

	public void setTargetEnd(long targetEnd) {
		this.targetEnd = targetEnd;
	}

	public String getStrand() {
		return strand;
	}

	public boolean isPlusStrand() {
		return strand.equals("+");
	}

	public void setStrand(String strand) {
		this.strand = strand;
	}

	public int getBlockCount() {
		return blockCount;
	}

	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}

	public ArrayList<Integer> getBlockSizes() {
		return blockSizeList;
	}

	public ArrayList<Long> getQueryStarts() {
		return queryStartList;
	}

	public ArrayList<Long> getTargetStarts() {
		return targetStartList;
	}

	public void addBlockSizes(int blockSize) {
		this.blockSizeList.add(blockSize);
	}

	public void addQueryStarts(long queryStart) {
		this.queryStartList.add(queryStart);
	}

	public void addTargetStarts(long targetStart) {
		this.targetStartList.add(targetStart);
	}

}
