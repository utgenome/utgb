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
// CompactFASTA.java
// Since: 2010/03/12
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.util.sequence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import org.utgenome.UTGBException;
import org.xerial.core.XerialException;

/**
 * CompactFASTA is a packed FASTA file
 * 
 * @author leo
 * 
 */
public class CompactFASTA {

	public final static String PAC_FILE_SUFFIX = ".pac";
	public final static String PAC_N_FILE_SUFFIX = ".pacn";
	public final static String PAC_INDEX_FILE_SUFFIX = ".i.silk";

	private HashMap<String, CompactACGTIndex> indexTable = new HashMap<String, CompactACGTIndex>();
	private RandomAccessFile packedFASTA;
	private RandomAccessFile packedFASTA_N;

	public CompactFASTA(String fastaFile) throws XerialException, IOException {
		File f = new File(fastaFile);
		String prefix = f.getName();
		String fileDir = f.getParent();
		File indexFile = new File(fileDir, prefix + PAC_INDEX_FILE_SUFFIX);
		for (CompactACGTIndex each : CompactACGTIndex.load(new BufferedReader(new FileReader(indexFile)))) {
			indexTable.put(each.name, each);
		}

		File pacFile = new File(fileDir, prefix + PAC_FILE_SUFFIX);
		File pacNFile = new File(fileDir, prefix + PAC_N_FILE_SUFFIX);
		packedFASTA = new RandomAccessFile(pacFile, "r");
		packedFASTA_N = new RandomAccessFile(pacNFile, "r");
	}

	public void close() throws IOException {
		packedFASTA.close();
		packedFASTA_N.close();
	}

	/**
	 * Retrieves a genome sequence of the specified range
	 * 
	 * @param chr
	 *            sequence name
	 * @param start
	 *            start position on genome (0-origin)
	 * @param end
	 *            end position on genome (0-origin, exclusive)
	 * @return
	 * @throws IOException
	 * @throws UTGBException
	 */
	public GenomeSequence getSequence(String chr, int start, int end) throws IOException, UTGBException {
		if (start > end) {
			int tmp = end;
			end = start;
			start = tmp;
		}

		if (!indexTable.containsKey(chr))
			return null;

		CompactACGTIndex index = indexTable.get(chr);
		int length = end - start;
		if (length > index.length)
			length = (int) (index.length - start);

		byte[] seqBuf = new byte[length / 4 + 1];
		byte[] seqNBuf = new byte[length / 8 + 1];

		long bStart = start + index.offset;
		long pac_lowerBound = bStart / 4;
		long pacN_lowerBound = bStart / 8;
		packedFASTA.seek(pac_lowerBound);
		packedFASTA.read(seqBuf);
		packedFASTA_N.seek(pacN_lowerBound);
		packedFASTA_N.read(seqNBuf);
		return new CompactACGT(seqBuf, seqNBuf, length, start % 4);
	}

	public GenomeSequence getSequence(String chr) throws IOException, UTGBException {
		if (!indexTable.containsKey(chr))
			return null;
		CompactACGTIndex index = indexTable.get(chr);
		return getSequence(chr, 0, (int) index.length);
	}

}
