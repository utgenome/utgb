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
// Since: 2009/04/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Genome Read data
 * 
 * @author leo
 * 
 */
public class SAMRead {
	public static enum Strand {
		FORWARD, REVERSE
	}

	public static final int FLAG_PAIRED_READ = 0x001;
	public static final int FLAG_MAPPED_IN_A_PROPER_PAIR = 0x002;
	public static final int FLAG_QUERY_IS_UNMAPPED = 0x004;
	public static final int FLAG_MATE_IS_UNMAPPED = 0x008;
	public static final int FLAG_STRAND_OF_QUERY = 0x0010;
	public static final int FLAG_STRAND_OF_MATE = 0x0020;
	public static final int FLAG_IS_FIRST_READ = 0x0040;
	public static final int FLAG_IS_SECOND_READ = 0x0080;
	public static final int FLAG_NOT_PRIMARY = 0x0100;
	public static final int FLAG_FAILS_QUALITY_CHECK = 0x0200;
	public static final int FLAG_PCR_OR_OPTICAL_DUPLICATE = 0x0400;

	public Strand strand;

	public String seq;
	public String cigar;
	public int pos;
	public int mPos;

	public int isize;

	public SAMRead() {

	}

}
