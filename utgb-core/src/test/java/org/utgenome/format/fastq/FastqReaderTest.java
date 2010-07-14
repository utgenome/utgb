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
// FastqReaderTest.java
// Since: Jul 5, 2010
//
//--------------------------------------
package org.utgenome.format.fastq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.xerial.util.FileResource;

public class FastqReaderTest {

	@Test
	public void reader() throws Exception {
		FastqReader reader = new FastqReader(FileResource.open(FastqReaderTest.class, "s_1_1_sequence.fastq"));
		FastqRead r1 = reader.next();
		FastqRead r2 = reader.next();
		FastqRead empty = reader.next();
		assertNotNull(r1);
		assertNotNull(r2);
		assertNull(empty);

		assertEquals("SAMPLE001:1:1:1011:10192#0/1", r1.seqname);
		assertEquals("NTAGTGGATATATATCTAGGAAAAAAAGTACAGACTCTAAATATTGAAAGGTAAAACTGAAAAGCTAAAAATATTTGAAGAAATCACTGGAAAATTATTC", r1.seq);
		assertEquals("#)))),,,/.AAAAAA7AAAAA7AA/--038303:AA7AAAAA7A548778488:75848)'&%)&))'&55225//003AA2AA4332488775AAAA7", r1.qual);

		assertEquals("SAMPLE001:1:1:1011:11390#0/1", r2.seqname);
		assertEquals("NTTTTCATGTAAACAAACGACTTTCGAAGAAAGAATAGGAAACATTTCCTCTTCATCTGTACTTCTCTTTCTTTGCCCAGATGTAGATGGTGCTTATTTC", r2.seq);
		assertEquals("#*++*)..++AAAA7A27AA888770,303AAAAA27588AAA77857885555888:55A7AAAAAAAAAA027(+++(..0..01110AA7AAAA7A5", r2.qual);

	}
}
