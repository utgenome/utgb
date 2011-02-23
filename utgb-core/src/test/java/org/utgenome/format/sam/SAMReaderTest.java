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
// SAMReaderTest.java
// Since: Dec 25, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.sam;

import static org.junit.Assert.assertEquals;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class SAMReaderTest {
	private static Logger _logger = Logger.getLogger(SAMReaderTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRead() throws Exception {
		SAMFileReader reader = new SAMFileReader(FileResource.find(SAMReaderTest.class, "chr21.sam").openStream());
		for (SAMRecord each : reader)
			_logger.info(each.format());
	}

	@Test
	public void trimPairedEndReadNameSuffix() throws Exception {

		assertEquals("read1", SAMReader.trimPairedEndSuffix("read1"));

		assertEquals("read1", SAMReader.trimPairedEndSuffix("read1/1"));
		assertEquals("read1", SAMReader.trimPairedEndSuffix("read1/2"));
		assertEquals("read1", SAMReader.trimPairedEndSuffix("read1#1"));
		assertEquals("read1", SAMReader.trimPairedEndSuffix("read1#2"));

	}

}
