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
// VariationAnnotatorTest.java
// Since: 2010/10/20
//
//--------------------------------------
package org.utgenome.util.sv;

import java.io.File;

import org.junit.Test;
import org.utgenome.format.fasta.CompactFASTAGenerator;
import org.utgenome.util.TestHelper;

public class VariationAnnotatorTest {

	@Test
	public void annotation() throws Exception {

		File chr21 = TestHelper.createTempFileFrom(VariationAnnotatorTest.class, "test_refGene1102.fa");
		File bed = TestHelper.createTempFileFrom(VariationAnnotatorTest.class, "refgene_chr21_offset2.bed");
		File var = TestHelper.createTempFileFrom(VariationAnnotatorTest.class, "var_offset.silk");

		CompactFASTAGenerator g = new CompactFASTAGenerator();
		g.packFASTA(chr21.getAbsolutePath());

		VariationAnnotator.main(new String[] { chr21.getAbsolutePath(), bed.getAbsolutePath(), var.getAbsolutePath() });

	}
}
