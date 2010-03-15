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
// SAMEntry.java
// Since: Mar 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.sam;

import java.util.Properties;

/**
 * SAMEntry corresponds to a line in SAM/BAM format
 * 
 * @author leo
 * 
 */
public class SAMEntry {
	//schema record(qname, flag, rname, start, end, mapq, cigar, mrnm, mpos, isize, seq, qual, tag*)
	public String qname;
	public int flag;
	public String rname;
	public int start;
	public int end;
	public int mapq;
	public String cigar;
	public String mrnm; // mate reference name
	public int iSize;
	public String seq;
	public String qual;
	public Tag tag;

	public static class Tag {
		public Properties _ = new Properties();
	}

}
