/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// FastqEntry.java
// Since: Jun 14, 2010
//
//--------------------------------------
package org.utgenome.format.illumina;

import org.xerial.util.StringUtil;

/**
 * An entry of FASTQ format
 * 
 * @author leo
 * 
 */
public class FastqRead {
	public final String seqname;
	public final String seq;
	public final String qual;

	public FastqRead(String seqname, String seq, String qual) {
		this.seqname = seqname;
		this.seq = seq;
		this.qual = qual;
	}

	public String toFASTQString() {
		StringBuilder buf = new StringBuilder();
		buf.append("@");
		buf.append(seqname);
		buf.append(StringUtil.NEW_LINE);
		buf.append(seq);
		buf.append(StringUtil.NEW_LINE);
		buf.append("+");
		buf.append(StringUtil.NEW_LINE);
		buf.append(qual);
		buf.append(StringUtil.NEW_LINE);
		return buf.toString();
	}

}
