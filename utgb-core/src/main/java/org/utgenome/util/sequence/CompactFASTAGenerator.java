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
// CompactFASTAGenerator.java
// Since: Feb 22, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.util.sequence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.utgenome.format.fasta.FASTA;
import org.utgenome.format.fasta.FASTAPullParser;
import org.xerial.silk.SilkWriter;
import org.xerial.util.FileType;
import org.xerial.util.log.Logger;

/**
 * @author leo
 * 
 */
public class CompactFASTAGenerator {

	private static Logger _logger = Logger.getLogger(CompactFASTAGenerator.class);

	private CompactACGTWriter compressor;
	private SilkWriter indexOut;
	private String workDir = "target";

	public CompactFASTAGenerator() {

	}

	public void packFASTA(String fastaFilePath) throws IOException {
		packFASTA(fastaFilePath, new FileInputStream(fastaFilePath));
	}

	public void packFASTA(URL fastaFile) throws IOException {
		packFASTA(fastaFile.getPath(), fastaFile.openStream());
	}

	public void packFASTA(String fastaFilePrefix, InputStream inputFASTA) throws IOException {

		String fileName = new File(fastaFilePrefix).getName();
		String baseName = FileType.removeFileExt(fileName);

		// output files
		String pacSeqFile = baseName + ".pac";
		String pacNSeqFile = baseName + ".npac";
		String pacIndexFile = baseName + ".pac.index.silk";
		_logger.info("pac file: " + pacSeqFile);
		_logger.info("pac file for N: " + pacNSeqFile);
		_logger.info("pac index file: " + pacIndexFile);

		BufferedOutputStream pacSeqOut = new BufferedOutputStream(new FileOutputStream(new File(workDir, pacSeqFile)));
		BufferedOutputStream pacNSeqOut = new BufferedOutputStream(new FileOutputStream(new File(workDir, pacNSeqFile)));
		compressor = new CompactACGTWriter(pacSeqOut, pacNSeqOut);
		indexOut = new SilkWriter(new BufferedWriter(new FileWriter(new File(workDir, pacIndexFile))));

		indexOut.preamble();
		indexOut.preamble("schema sequence(length, offset)");

		// load FASTA file
		// switch the input stream according to the file type
		switch (FileType.getFileType(fileName)) {
		case TAR_GZ:
			packFASTAInTarGZFormat(new GZIPInputStream(new BufferedInputStream(inputFASTA)));
			break;
		case GZIP:
			packFASTA(new GZIPInputStream(new BufferedInputStream(inputFASTA)));
			break;
		default:
			packFASTA(new BufferedInputStream(inputFASTA));
			break;
		}

		compressor.close();
		indexOut.close();

		_logger.info("pack done.");

	}

	private void packFASTA(InputStream in) throws IOException {
		FASTAPullParser fastaParser = new FASTAPullParser(new InputStreamReader(in));
		String description;
		while ((description = fastaParser.nextDescriptionLine()) != null) {

			String sequenceName = FASTA.pickSequenceName(description);
			_logger.info(String.format("loading %s ...", sequenceName));
			long start = compressor.getSequenceLength();

			String seq = null;
			while ((seq = fastaParser.nextSequenceLine()) != null) {
				compressor.append(seq);
			}

			long end = compressor.getSequenceLength();
			long sequenceLength = end - start;

			SilkWriter s = indexOut.node("sequence").attribute("name", sequenceName);
			s.leaf("length", Long.toString(sequenceLength));
			s.leaf("offset", Long.toString(start));
		}

	}

	public void packFASTAInTarGZFormat(InputStream in) throws IOException {
		TarInputStream tarInput = new TarInputStream(in);
		TarEntry nextEntry = null;

		while ((nextEntry = tarInput.getNextEntry()) != null) {
			if (nextEntry.isDirectory())
				continue;

			packFASTA(tarInput);
		}
		tarInput.close();
	}

}
