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
// SAM2SilkReaderTest.java
// Since: Mar 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.sam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.xerial.core.XerialException;
import org.xerial.lens.SilkLens;
import org.xerial.util.FileResource;
import org.xerial.util.ObjectHandler;
import org.xerial.util.ObjectHandlerBase;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

public class SAM2SilkReaderTest {

	private static Logger _logger = Logger.getLogger(SAM2SilkReaderTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void toSilkTest() throws Exception {

		SilkLens.findFromSilk(new SAM2SilkReader(FileResource.open(SAM2SilkReaderTest.class, "chr21.sam")), "record", SAMRead.class,
				new ObjectHandler<SAMRead>() {

					int count = 0;

					public void finish() throws Exception {
						assertEquals(2, count);
					}

					public void init() throws Exception {

					}

					public void handle(SAMRead input) throws Exception {

						// read_28833_29006_6945	99	chr21	28833	20	10M1D25M	=	28993	195	AGCTTAGCTAGCTACCTATATCTTGGTCTTGGCCG	<<<<<<<<<<<<<<<<<<<<<:<9/,&,22;;<<<	MF:i:130	Nm:i:1	H0:i:0	H1:i:0	RG:Z:L1
						// read_28701_28881_323b	147	chr21	28834	30	35M	=	28701	-168	ACCTATATCTTGGCCTTGGCCGATGCGGCCTTGCA	<<<<<;<<<<7;:<<<6;<<<<<<<<<<<<7<<<<	MF:i:18	Nm:i:0	H0:i:1	H1:i:0	RG:Z:L2

						if ("read_28833_29006_6945".equals(input.qname)) {
							assertEquals(99, input.flag);
							assertEquals("chr21", input.rname);
							assertEquals(28833, input.getStart());
							assertEquals(28869, input.getEnd());
							assertEquals("10M1D25M", input.cigar);
							assertNull(input.mrnm); // when mrnm == null
							assertEquals(28993, input.mStart);
							assertEquals(195, input.iSize);
							assertEquals("AGCTTAGCTAGCTACCTATATCTTGGTCTTGGCCG", input.seq);
							assertEquals("<<<<<<<<<<<<<<<<<<<<<:<9/,&,22;;<<<", input.qual);
							assertEquals(5, input.tag.size());
							assertEquals(130, input.tag.getInt("MF"));
							assertEquals(1, input.tag.getInt("NM"));
							assertEquals(0, input.tag.getInt("H0"));
							assertEquals(0, input.tag.getInt("H1"));
							assertEquals("L1", input.tag.get("RG"));
							count++;
						}
						else if ("read_28701_28881_323b".equals(input.qname)) {
							assertEquals(147, input.flag);
							assertEquals("chr21", input.rname);
							assertEquals(28834, input.getStart());
							assertEquals(28869, input.getEnd());
							assertEquals("35M", input.cigar);
							assertNull(input.mrnm); // when mrnm == null
							assertEquals(28701, input.mStart);
							assertEquals(-168, input.iSize);
							assertEquals("ACCTATATCTTGGCCTTGGCCGATGCGGCCTTGCA", input.seq);
							assertEquals("<<<<<;<<<<7;:<<<6;<<<<<<<<<<<<7<<<<", input.qual);
							assertEquals(5, input.tag.size());
							assertEquals(18, input.tag.getInt("MF"));
							assertEquals(0, input.tag.getInt("NM"));
							assertEquals(1, input.tag.getInt("H0"));
							assertEquals(0, input.tag.getInt("H1"));
							assertEquals("L2", input.tag.get("RG"));
							count++;
						}

						_logger.info(SilkLens.toSilk(input));
					}
				});
	}

	@Test
	public void bssSAM() throws XerialException, IOException {
		StringWriter w = new StringWriter();
		BufferedReader b = new BufferedReader(new SAM2SilkReader(FileResource.open(SAM2SilkReaderTest.class, "bss-align.sam")));
		String line;
		while ((line = b.readLine()) != null) {
			w.append(line);
			w.append(StringUtil.NEW_LINE);
		}
		String silk = w.toString();
		_logger.info(silk);

		SilkLens.findFromSilk(new SAM2SilkReader(FileResource.open(SAM2SilkReaderTest.class, "bss-align.sam")), "record", SAMRead.class,
				new ObjectHandlerBase<SAMRead>() {
					public void handle(SAMRead input) throws Exception {
						_logger.info(SilkLens.toSilk(input));
					}
				});
	}
}
