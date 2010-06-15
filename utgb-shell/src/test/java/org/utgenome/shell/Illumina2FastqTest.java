/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// utgb-shell Project
//
// Illumina2FastqTest.java
// Since: Jun 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;

public class Illumina2FastqTest {

	@Test
	public void fastq() throws Exception {

		File input = FileUtil.createTempFile(new File("target"), "sample_sequence", ".txt");
		FileUtil.copy(FileResource.openByteStream(Illumina2FastqTest.class, "s_1_1_sequence.txt"), input);
		UTGBShell.runCommand(String.format("illumina2fastq %s -", input.getAbsolutePath()));

	}
}
