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
// CompactACGT.java
// Since: Feb 22, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.util.sequence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.xerial.core.XerialException;

/**
 * Compact array for ACGT (and N) sequences
 * 
 * @author leo
 * 
 */
public class CompactACGT implements GenomeSequence {

	private byte[] sequence; // 2 bit for each char
	private byte[] sequenceMask; // 1 bit for each char: 0 for ACGT, 1 for otherwise including N
	private int length;

	private final static int BYTE = 8;
	private final static char[] ACGT = { 'A', 'C', 'G', 'T' };

	static class PacFileAccess {
		private final String fileNamePrefix;

		private Map<String, CompactACGTIndex> indexTable = new HashMap<String, CompactACGTIndex>();

		public PacFileAccess(URL fastaFile) throws XerialException, IOException {
			File f = new File(fastaFile.getPath());
			fileNamePrefix = f.getName();
			String fileDir = f.getParent();
			File indexFile = new File(fileDir, fileNamePrefix + ".index.silk");
			for (CompactACGTIndex each : CompactACGTIndex.load(new BufferedReader(new FileReader(indexFile)))) {
				indexTable.put(each.name, each);
			}
		}

	}

	static class OnMemoryPacDataAccess {

	}

	public int length() {
		return length;
	}

	public char charAt(int index) {

		int maskPos = index / 8;
		int maskOffset = index % 8;
		if ((sequenceMask[maskPos] & (0x01 << (7 - maskOffset))) != 0)
			return 'N';

		int pos = index / 4;
		int offset = index % 4;

		int c = (sequence[pos] >> ((3 - offset) * 2)) & 0x03;
		return ACGT[c];
	}

}
