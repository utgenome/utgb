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
// CompactACGTIndexTest.java
// Since: 2010/03/11
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.utgenome.format.fasta.CompactACGTIndex;
import org.utgenome.format.fasta.CompactFASTAGenerator;
import org.xerial.lens.Lens;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class CompactACGTIndexTest {

	private static Logger _logger = Logger.getLogger(CompactACGTIndexTest.class);

	public static String workDir = "target";

	@BeforeClass
	public static void setUp() throws Exception {
		CompactFASTAGenerator g = new CompactFASTAGenerator();
		g.setWorkDir(workDir);
		g.packFASTA(FileResource.find(CompactFASTAGeneratorTest.class, "sample.fa"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void load() throws Exception {
		List<CompactACGTIndex> index = CompactACGTIndex.load(new FileReader(new File(workDir, "sample.index.silk")));
		assertEquals(3, index.size());
		_logger.info(Lens.toSilk(index));

	}

}
