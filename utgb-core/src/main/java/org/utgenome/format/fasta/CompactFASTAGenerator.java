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
package org.utgenome.format.fasta;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xerial.silk.SilkWriter;
import org.xerial.util.FileType;
import org.xerial.util.log.Logger;

/**
 * 
 * 
 * @author leo
 * 
 */
public class CompactFASTAGenerator {

	private static Logger _logger = Logger.getLogger(CompactFASTAGenerator.class);

	private CompactACGTWriter compressor;
	private SilkWriter indexOut;
	private String workDir = "target";

	public int BUFFER_SIZE = 8 * 1024 * 1024;

	public CompactFASTAGenerator() {

	}

	public void setBuffferSize(int bufferSizeInMB) {
		BUFFER_SIZE = bufferSizeInMB * 1024 * 1024;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public void setWorkDir(File workDir) {
		this.workDir = workDir.getAbsolutePath();
	}

	public String getWorkDir() {
		return workDir;
	}

	public void packFASTA(String fastaFilePath) throws IOException {
		packFASTA(fastaFilePath, new FileInputStream(fastaFilePath));
	}

	public void packFASTA(URL fastaFile) throws IOException {
		packFASTA(fastaFile.getPath(), fastaFile.openStream());
	}

	public void packFASTA(String fastaFilePrefix, InputStream in) throws IOException {

		File work = new File(workDir);
		if (!work.exists()) {
			_logger.info("create a directory: " + work);
			work.mkdirs();
		}

		String fileName = new File(fastaFilePrefix).getName();
		String baseName = fileName.endsWith(".fa") ? fileName : FileType.removeFileExt(fileName);

		// output files
		String pacSeqFile = baseName + CompactFASTA.PAC_FILE_SUFFIX;
		String pacNSeqFile = baseName + CompactFASTA.PAC_N_FILE_SUFFIX;
		String pacIndexFile = baseName + CompactFASTA.PAC_INDEX_FILE_SUFFIX;
		_logger.info("pac file: " + pacSeqFile);
		_logger.info("pac file for N: " + pacNSeqFile);
		_logger.info("pac index file: " + pacIndexFile);

		BufferedOutputStream pacSeqOut = new BufferedOutputStream(new FileOutputStream(new File(workDir, pacSeqFile)), BUFFER_SIZE);
		BufferedOutputStream pacNSeqOut = new BufferedOutputStream(new FileOutputStream(new File(workDir, pacNSeqFile)), BUFFER_SIZE);
		compressor = new CompactACGTWriter(pacSeqOut, pacNSeqOut);
		indexOut = new SilkWriter(new BufferedWriter(new FileWriter(new File(workDir, pacIndexFile))));

		indexOut.preamble();
		indexOut.schema("sequence").attribute("name").attribute("description").attribute("length").attribute("offset");

		// load FASTA file (.fa, .fa.tar.gz, ...)
		packFASTA(new FASTAPullParser(fileName, in, BUFFER_SIZE));

		compressor.close();
		indexOut.close();

		_logger.info("pack done.");

	}

	private void packFASTA(FASTAPullParser fastaParser) throws IOException {
		String description;
		while ((description = fastaParser.nextDescriptionLine()) != null) {

			String sequenceName = CompactFASTA.pickSequenceName(description);
			_logger.info(String.format("loading %s ...", sequenceName));
			long start = compressor.getSequenceLength();

			String seq = null;
			while ((seq = fastaParser.nextSequenceLine()) != null) {
				compressor.append(seq);
			}

			long end = compressor.getSequenceLength();
			long sequenceLength = end - start;

			SilkWriter s = indexOut.node("sequence").attribute("name", sequenceName);
			s.leaf("description", description);
			s.leaf("length", Long.toString(sequenceLength));
			s.leaf("offset", Long.toString(start));
		}

	}

}
