/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// UTGB Common Project
//
// FASTAPullParserTest.java
// Since: Jun 4, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.format.InvalidFormatException;
import org.xerial.util.FileResource;

public class FASTAPullParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void pullParsing() throws IOException, InvalidFormatException {
		FASTAPullParser parser = new FASTAPullParser(new StringReader(">1|mySeq\nACGCTT\nACCC\n>2\nCCGGA"));
		FASTASequence s1 = parser.nextSequence();
		FASTASequence s2 = parser.nextSequence();

		assertNotNull(s1);
		assertNotNull(s2);
		assertEquals("ACGCTTACCC", s1.getSequence());
		assertEquals("CCGGA", s2.getSequence());

		String[] description1 = s1.getDescriptionLine().split("\\|");
		String[] description2 = s2.getDescriptionLine().split("\\|");

		assertEquals(2, description1.length);
		assertEquals("1", description1[0]);
		assertEquals("mySeq", description1[1]);
		assertEquals(1, description2.length);
		assertEquals("2", description2[0]);

		FASTASequence s3 = parser.nextSequence();
		assertNull(s3);
	}

	@Test
	public void pullParsing2() throws IOException, InvalidFormatException {
		FASTAPullParser parser = new FASTAPullParser(new StringReader(">1|mySeq\nACGCTT\nACCC\n>2\nCCGGA"));

		assertEquals("1|mySeq", parser.nextDescriptionLine());
		assertEquals("ACGCTT", parser.nextSequenceLine());
		assertEquals("ACCC", parser.nextSequenceLine());
		assertNull(parser.nextSequenceLine());

		assertEquals("2", parser.nextDescriptionLine());
		assertEquals("CCGGA", parser.nextSequenceLine());
		assertNull(parser.nextSequenceLine());

	}

	@Test
	public void readTarFile() throws Exception {
		FASTAPullParser parser = FASTAPullParser.newTARGZFileReader(FileResource.openByteStream(FASTAPullParserTest.class, "sample-archive.fa.tar.gz"));
		ArrayList<String> chrList = new ArrayList<String>();
		for (FASTASequence seq; (seq = parser.nextSequence()) != null;) {
			String name = seq.getSequenceName();
			chrList.add(name);
			if (name.equals("chr1")) {
				assertEquals("NNNNNNACGGATTCTTGCTATATANTTACTTACCCGTAGTCTAGAGATCTTTCCAATATCGTCT", seq.getSequence());
			}
			else if (name.equals("chr2")) {
				assertEquals("ACCGGTATCTCTAAAAAAAAAAAGGG", seq.getSequence());
			}
			else if (name.equals("chr3")) {
				assertEquals("CGGTCTGTCGTCGTCAACGTCGGCCTTTCGCGCGCGGGGCCTAAATTAATTATAATTAAAAATCCTCT", seq.getSequence());
			}
			else {
				fail("unknown sequence: " + name);
			}

		}

		for (String e : new String[] { "chr1", "chr2", "chr3" }) {
			assertTrue(chrList.contains(e));
		}

	}

}
