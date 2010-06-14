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
// Seq2FastqTest.java
// Since: Jun 14, 2010
//
//--------------------------------------
package org.utgenome.format.illumina;

import java.io.StringWriter;

import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class Seq2FastqTest {

	private static Logger _logger = Logger.getLogger(Seq2FastqTest.class);

	@Test
	public void convert() throws Exception {

		StringWriter buf = new StringWriter();
		Seq2Fastq.convert(FileResource.open(Seq2FastqTest.class, "s_1_1_sequence.txt"), buf);
		_logger.info(buf.toString());

	}
}
