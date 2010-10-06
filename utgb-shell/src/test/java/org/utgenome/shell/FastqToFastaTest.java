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
// utgb-shell Project
//
// FastqToFastaTest.java
// Since: 2010/10/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;

import org.junit.Test;
import org.utgenome.format.fasta.FASTAPullParser;
import org.utgenome.format.fasta.FASTASequence;
import org.utgenome.format.fastq.FastqRead;
import org.utgenome.format.fastq.FastqReader;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;

public class FastqToFastaTest {

	@Test
	public void convert() throws Exception {

		// prepare input files
		File in = FileUtil.createTempFile(new File("target"), "input", ".fastq");
		FileUtil.copy(FileResource.openByteStream(FastqToFastaTest.class, "sample.fastq"), in);
		File out = FileUtil.createTempFile(new File("target"), "output", ".fasta");
		in.deleteOnExit();
		out.deleteOnExit();

		// run utgb fastq2fasta
		UTGBShell.runCommand(String.format("fastq2fasta %s %s", in, out));

		// confirm the output
		FastqReader fastq = new FastqReader(new FileReader(in));
		FASTAPullParser fasta = new FASTAPullParser(out);

		for (FastqRead read; (read = fastq.next()) != null;) {
			FASTASequence fastaSeq = fasta.nextSequence();
			assertEquals(read.seqname, fastaSeq.getDescriptionLine());
			assertEquals(read.seq, fastaSeq.getSequence());
		}
		fastq.close();
		fasta.close();
	}
}
