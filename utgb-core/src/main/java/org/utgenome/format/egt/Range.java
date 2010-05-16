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
// utgb-core Project
//
// Read.java
// Since: Dec 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.egt;

/**
 * Read on a genome sequence
 * 
 * @author leo
 *
 */
public class Range {
	private long start = -1;
	private long end = -1;

	public Range()
	{}
	
	/**
	 * @param start
	 * @param end
	 */
	public Range(long start, long end) {
		this.start = start;
		this.end = end;
	}
	
	public Range(String start, String end) throws NumberFormatException
	{
		this.start = Long.parseLong(start);
		this.end = Long.parseLong(end);
	}

	public long getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}


}




