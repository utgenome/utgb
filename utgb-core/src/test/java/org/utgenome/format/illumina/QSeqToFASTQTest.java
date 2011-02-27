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
// QSeqToFASTQTest.java
// Since: Jul 20, 2010
//
//--------------------------------------
package org.utgenome.format.illumina;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.utgenome.format.fastq.FastqRead;
import org.utgenome.format.fastq.FastqReader;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class QSeqToFASTQTest {

	private static Logger _logger = Logger.getLogger(QSeqToFASTQTest.class);

	@Test
	public void sample() throws Exception {

		StringWriter buf = new StringWriter();

		final String prefix = "HG0001SE:L8";
		QSeqToFASTQ converter = new QSeqToFASTQ(prefix, true);
		converter.convert(FileResource.open(QSeqToFASTQTest.class, "qseq_sample.txt"), buf);
		_logger.info(buf.toString());

		int readCount = 0;
		FastqReader fr = new FastqReader(new StringReader(buf.toString()));
		for (FastqRead read; (read = fr.next()) != null; readCount++) {
			assertTrue(read.seqname.startsWith(prefix));
		}
		assertEquals(4, readCount);

	}

	@Test
	public void filter() throws Exception {
		StringWriter buf = new StringWriter();

		final String prefix = "HG0001SE:L8";
		QSeqToFASTQ converter = new QSeqToFASTQ(prefix, false);
		converter.convert(FileResource.open(QSeqToFASTQTest.class, "qseq_sample.txt"), buf);
		_logger.info(buf.toString());

		int readCount = 0;
		FastqReader fr = new FastqReader(new StringReader(buf.toString()));
		for (FastqRead read; (read = fr.next()) != null; readCount++) {
			assertTrue(read.seqname.startsWith(prefix));
		}
		assertEquals(2, readCount);

	}

	@Test
	public void suffix() throws Exception {
		StringWriter buf = new StringWriter();

		final String prefix = "HG0001SE:L8";
		QSeqToFASTQ converter = new QSeqToFASTQ(prefix, false);
		converter.setReadNameSuffix("/1");
		converter.convert(FileResource.open(QSeqToFASTQTest.class, "qseq_sample.txt"), buf);
		_logger.info(buf.toString());

		int readCount = 0;
		FastqReader fr = new FastqReader(new StringReader(buf.toString()));
		for (FastqRead read; (read = fr.next()) != null; readCount++) {
			assertTrue(read.seqname.startsWith(prefix));
			assertTrue(read.seqname.endsWith("/1"));
		}
		assertEquals(2, readCount);

	}

}
