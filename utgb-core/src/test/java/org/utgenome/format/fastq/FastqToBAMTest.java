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
// FastqToBAMTest.java
// Since: Jul 12, 2010
//
//--------------------------------------
package org.utgenome.format.fastq;

import static org.junit.Assert.assertEquals;

import java.io.File;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;

public class FastqToBAMTest {

	private final static File outdir = new File("target");

	File fastq1;
	File fastq2;

	@Before
	public void setUp() throws Exception {
		fastq1 = FileUtil.createTempFile(outdir, "input1", ".fastq");
		fastq2 = FileUtil.createTempFile(outdir, "input2", ".fastq");

		FileUtil.copy(FileResource.openByteStream(FastqToBAMTest.class, "s_1_1_sequence.fastq"), fastq1);
		FileUtil.copy(FileResource.openByteStream(FastqToBAMTest.class, "s_1_2_sequence.fastq"), fastq2);
	}

	@After
	public void tearDown() throws Exception {
		if (fastq1 != null)
			fastq1.delete();

		if (fastq2 != null)
			fastq2.delete();

	}

	@Test
	public void convert() throws Exception {
		File sam = FileUtil.createTempFile(outdir, "f1-f2", ".sam");
		sam.deleteOnExit();

		FastqToBAM.execute(new String[] { fastq1.getPath(), fastq2.getPath(), "-o", sam.getPath(), "--sample=FC" });
		SAMFileReader r = new SAMFileReader(sam);
		int count = 0;
		for (SAMRecord rec : r) {
			count++;
		}
		assertEquals(4, count);
	}

	@Test
	public void convertToBAM() throws Exception {
		File bam = FileUtil.createTempFile(outdir, "f1-f2", ".bam");
		bam.deleteOnExit();

		FastqToBAM.execute(new String[] { fastq1.getPath(), fastq2.getPath(), "-o", bam.getPath(), "--sample=FC", "--readGroup=G1" });

		SAMFileReader r = new SAMFileReader(bam);
		int count = 0;
		for (SAMRecord rec : r) {
			count++;
		}
		assertEquals(4, count);
	}

	@Test
	public void convertSample() throws Exception {
		File sam = FileUtil.createTempFile(outdir, "sample", ".sam");
		sam.deleteOnExit();
		File input = FileUtil.createTempFile(outdir, "sample", ".fastq");
		FileUtil.copy(FileResource.openByteStream(FastqToBAMTest.class, "sample.fastq"), input);
		input.deleteOnExit();
		FastqToBAM.execute(new String[] { input.getPath(), "-o", sam.getPath() });
		SAMFileReader r = new SAMFileReader(sam);
		int count = 0;
		for (SAMRecord rec : r) {
			count++;
		}
		assertEquals(3, count);
	}

	@Test
	public void convertGZSample() throws Exception {
		File sam = FileUtil.createTempFile(outdir, "sample", ".sam");
		sam.deleteOnExit();
		File input = FileUtil.createTempFile(outdir, "sample", ".fastq.gz");
		FileUtil.copy(FileResource.openByteStream(FastqToBAMTest.class, "sample.fastq.gz"), input);
		input.deleteOnExit();
		FastqToBAM.execute(new String[] { input.getPath(), "-o", sam.getPath() });
		SAMFileReader r = new SAMFileReader(sam);
		int count = 0;
		for (SAMRecord rec : r) {
			count++;
		}
		assertEquals(3, count);
	}

}
