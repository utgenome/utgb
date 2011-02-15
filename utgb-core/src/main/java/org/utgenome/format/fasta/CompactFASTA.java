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
package org.utgenome.format.fasta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

import org.utgenome.UTGBException;
import org.xerial.core.XerialException;

/**
 * CompactFASTA is a packed FASTA file, supporting random accesses to sub sequences of a specified chromosome.
 * 
 * @author leo
 * 
 */
public class CompactFASTA {

	public final static String PAC_FILE_SUFFIX = ".pac";
	public final static String PAC_N_FILE_SUFFIX = ".pacn";
	public final static String PAC_INDEX_FILE_SUFFIX = ".i.silk";

	private final LinkedHashMap<String, CompactFASTAIndex> indexTable = new LinkedHashMap<String, CompactFASTAIndex>();

	private final String fastaFilePrefix;
	private final PacFileAccess access;

	/**
	 * @param fastaFilePrefix
	 * @throws XerialException
	 * @throws IOException
	 */
	public CompactFASTA(String fastaFile) throws XerialException, IOException {
		this(fastaFile, false);
	}

	private CompactFASTA(String fastaFile, boolean loadIntoMemory) throws XerialException, IOException {
		this.fastaFilePrefix = fastaFile;

		File indexFile = new File(fastaFilePrefix + PAC_INDEX_FILE_SUFFIX);
		for (CompactFASTAIndex each : CompactFASTAIndex.load(new BufferedReader(new FileReader(indexFile)))) {
			indexTable.put(each.name, each);
		}

		File pacFile = new File(fastaFilePrefix + PAC_FILE_SUFFIX);
		File pacNFile = new File(fastaFilePrefix + PAC_N_FILE_SUFFIX);
		if (!loadIntoMemory)
			access = new OnDiskAccess(pacFile, pacNFile);
		else
			access = new OnMemoryBuffer(pacFile, pacNFile);
	}

	/**
	 * Get the set of chromosome names
	 * 
	 * @return
	 */
	public Set<String> getChrSet() {
		return Collections.unmodifiableSet(indexTable.keySet());
	}

	/**
	 * Test the specified chromosome name is in this FASTA
	 * 
	 * @param chr
	 * @return
	 */
	public boolean containsChr(String chr) {
		return indexTable.containsKey(chr);
	}

	public int getSequenceLength(String chr) {
		if (!indexTable.containsKey(chr))
			return -1;
		CompactFASTAIndex index = indexTable.get(chr);
		return index.length;
	}

	public static CompactFASTA loadIntoMemory(String fastaFilePrefix) throws XerialException, IOException {
		return new CompactFASTA(fastaFilePrefix, true);
	}

	public void close() throws IOException {
		access.close();
	}

	/**
	 * Retrieves a genome sequence of the specified range [start, end)
	 * 
	 * @param chr
	 *            sequence name
	 * @param start
	 *            start position on the genome (0-origin)
	 * @param end
	 *            end position on genome (0-origin, exclusive)
	 * @return genome sequence of the specified range, or null if no entry found for the given sequence name
	 * @throws IOException
	 * @throws UTGBException
	 */
	public CompactACGT getSequence(String chr, int start, int end) throws IOException, UTGBException {
		if (!indexTable.containsKey(chr))
			return null;
		CompactFASTAIndex index = indexTable.get(chr);
		return getSequence(index, start, end);
	}

	CompactACGT getSequence(CompactFASTAIndex index, int start, int end) throws IOException, UTGBException {
		if (index == null)
			throw new IllegalArgumentException("index must not be null");

		if (start > end) {
			int tmp = end;
			end = start;
			start = tmp;
		}

		int length = end - start;
		if (length > index.length)
			length = (index.length - start);

		long bStart = start + index.offset;
		long bEnd = bStart + length;
		long pac_lowerBound = bStart / 4;
		long pac_upperBound = bEnd / 4 + (bEnd % 4 != 0 ? 1 : 0);
		long pacN_lowerBound = bStart / 8;
		long pacN_upperBound = bEnd / 8 + (bEnd % 8 != 0 ? 1 : 0);

		//     s-------e 
		// |--------|------]
		byte[] seqBuf = new byte[(int) (pac_upperBound - pac_lowerBound)];
		byte[] seqNBuf = new byte[(int) (pacN_upperBound - pacN_lowerBound)];

		access.readSeq(pac_lowerBound, seqBuf);
		access.readNSeq(pacN_lowerBound, seqNBuf);
		return new CompactACGT(seqBuf, seqNBuf, length, (int) bStart % 4);

	}

	public GenomeSequence getSequence(String chr, int start) throws IOException, UTGBException {
		if (!indexTable.containsKey(chr))
			return null;
		CompactFASTAIndex index = indexTable.get(chr);
		return getSequence(index, start, index.length);
	}

	public GenomeSequence getSequence(String chr) throws IOException, UTGBException {
		return getSequence(chr, 0);
	}

	public static String pickSequenceName(String descriptionLine) {
		int begin = 0;
		if (descriptionLine.length() > 0 && descriptionLine.charAt(0) == '>')
			begin++;

		// skip leading white spaces
		for (; begin < descriptionLine.length(); ++begin) {
			char c = descriptionLine.charAt(begin);
			if (!(c == ' ' | c == '\t'))
				break;
		}
		int end = begin + 1;
		for (; end < descriptionLine.length(); ++end) {
			char c = descriptionLine.charAt(end);
			if (c == ' ' | c == '\t') {
				break;
			}
		}
		return descriptionLine.substring(begin, end);
	}

	public interface PacFileAccess {
		public void readSeq(long cursor, byte[] buf) throws IOException;

		public void readNSeq(long cursor, byte[] buf) throws IOException;

		public void close() throws IOException;
	}

	public class OnDiskAccess implements PacFileAccess {
		private final RandomAccessFile packedFASTA;
		private final RandomAccessFile packedFASTA_N;

		public OnDiskAccess(File pacFile, File pacNFile) throws FileNotFoundException {
			packedFASTA = new RandomAccessFile(pacFile, "r");
			packedFASTA_N = new RandomAccessFile(pacNFile, "r");
		}

		public void close() throws IOException {
			if (packedFASTA != null)
				packedFASTA.close();
			if (packedFASTA_N != null)
				packedFASTA_N.close();
		}

		public void readNSeq(long cursor, byte[] buf) throws IOException {
			packedFASTA_N.seek(cursor);
			packedFASTA_N.read(buf);
		}

		public void readSeq(long cursor, byte[] buf) throws IOException {
			packedFASTA.seek(cursor);
			packedFASTA.read(buf);
		}

	}

	public class OnMemoryBuffer implements PacFileAccess {
		byte[] pac;
		byte[] nPac;

		public OnMemoryBuffer(File pacFile, File pacNFile) throws IOException {
			long pacSize = pacFile.length();
			long nPacSize = pacNFile.length();

			// maximum: 4 * 2GB = 8G bases  
			pac = new byte[(int) pacSize];
			nPac = new byte[(int) nPacSize];

			// read sequences
			FileInputStream f = new FileInputStream(pacFile);
			f.read(pac);
			f.close();

			FileInputStream fn = new FileInputStream(pacNFile);
			fn.read(nPac);
			fn.close();
		}

		public void readSeq(long cursor, byte[] buf) throws IOException {
			System.arraycopy(pac, (int) cursor, buf, 0, buf.length);
		}

		public void readNSeq(long cursor, byte[] buf) throws IOException {
			System.arraycopy(nPac, (int) cursor, buf, 0, buf.length);
		}

		public void close() throws IOException {
			// nothing to do
		}

	}

}
