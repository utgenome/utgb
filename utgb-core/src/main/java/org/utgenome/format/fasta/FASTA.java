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
// FASTA.java
// Since: 2010/10/21
//
//--------------------------------------
package org.utgenome.format.fasta;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.utgenome.UTGBException;

public class FASTA {

	Map<String, RawStringWrapper> chrToSequence = new HashMap<String, RawStringWrapper>();

	public FASTA(File fastaFile) throws IOException, UTGBException {
		loadFASTA(fastaFile);
	}

	public static class RawStringWrapper implements GenomeSequence {

		public final String seq;

		public RawStringWrapper(String seq) {
			this.seq = seq;
		}

		public int length() {
			return seq.length();
		}

		public char charAt(int index) {
			return seq.charAt(index);
		}

	}

	public String getRawSequence(String chr) {
		return getSequence(chr).seq;
	}

	public RawStringWrapper getSequence(String chr) {
		return chrToSequence.get(chr);
	}

	void loadFASTA(File fastaFile) throws IOException, UTGBException {
		FASTAPullParser fastaPullParser = new FASTAPullParser(fastaFile);
		for (FASTASequence seq; (seq = fastaPullParser.nextSequence()) != null;) {
			chrToSequence.put(seq.getSequenceName(), new RawStringWrapper(seq.getSequence()));
		}
		fastaPullParser.close();
	}

}
