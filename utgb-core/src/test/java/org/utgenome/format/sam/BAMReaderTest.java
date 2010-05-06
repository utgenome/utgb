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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecord.SAMTagAndValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class BAMReaderTest {
	private static Logger _logger = Logger.getLogger(BAMReaderTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRead() throws Exception {
		_logger.info("read test");
		SAMFileReader reader = new SAMFileReader(FileResource.find(BAMReaderTest.class, "bss-align-sorted.bam").openStream());
		for (SAMRecord each : reader)
			_logger.info(each.format());
	}
	
	@Test
	public void queryTest() throws Exception {
		_logger.info("query test");
		ArrayList<SAMRead> readDataList = new ArrayList<SAMRead>();

		Iterator<SAMRecord> iterator = new SAMFileReader(new File("db/bss-align-sorted.bam"), new File("db/bss-align-sorted.bam.bai")).query("chr13", 0, 0, true);
		while (iterator.hasNext()){
			SAMRecord each = iterator.next();
			_logger.info(each.format());
			
			SAMRead read = new SAMRead();
			read.qname = each.getReadName();
			read.flag = each.getFlags();
			read.rname = each.getReferenceName();
			read.start = each.getAlignmentStart();
			read.end = each.getAlignmentEnd();
			read.mapq = each.getMappingQuality();
			read.cigar = each.getCigarString();
			read.mrnm = each.getMateReferenceName(); // mate reference name
			read.iSize = each.getInferredInsertSize();
			read.seq = each.getReadString();
			read.qual = each.getBaseQualityString();
			read.tag = new Properties();
			for (SAMTagAndValue tag : each.getAttributes()) {
				read.tag.add(tag.tag, String.valueOf(tag.value));
			}
			_logger.info(read);
			
			readDataList.add(read);

		}
	}
}
