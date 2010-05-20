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
// GenomeKeywordEntry.java
// Since: 2010/05/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.keyword;

/**
 * Keyword associated to a genome region
 * 
 * @author leo
 * 
 */
public class GenomeKeywordEntry {

	public String ref; // reference sequence ID (e.g. hg19, ut-medaka-1.0)
	public String chr; // chromosome/contig/scaffold name
	public String text; // text containing keywords
	public int start; // start position 
	public int end; // end position

	public GenomeKeywordEntry(String ref, String chr, String text, int start, int end) {
		this.ref = ref;
		this.chr = chr;
		this.text = text;

		if (end <= start) {
			int tmp = start;
			start = end;
			end = tmp;
		}

		this.start = start;
		this.end = end;
	}

	/**
	 * Alias is a keyword
	 * 
	 * @author leo
	 * 
	 */
	public static class KeywordAlias {
		public String keyword; // the keyword to redirect
		public String alias; // aliases

		public KeywordAlias(String keyword, String alias) {
			this.keyword = keyword;
			this.alias = alias;
		}

	}

}
