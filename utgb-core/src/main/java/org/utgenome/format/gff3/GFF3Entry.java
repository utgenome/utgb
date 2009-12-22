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
// GFF3Entry.java
// Since: Jul 7, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.gff3;

import java.util.HashMap;

public class GFF3Entry {

	private String seqId;
	private String soruce;
	private String type;
	private long start;
	private long end;
	private double score;
	private String strand;
	private String phase;
	private HashMap<String, String> attributes = new HashMap<String, String>();

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}

	public String getSoruce() {
		return soruce;
	}

	public void setSoruce(String soruce) {
		this.soruce = soruce;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getStrand() {
		return strand;
	}

	public void setStrand(String strand) {
		this.strand = strand;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void addAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

}
