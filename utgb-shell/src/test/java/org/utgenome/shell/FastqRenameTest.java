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
// FastqRenameTest.java
// Since: 2010/10/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;

import org.junit.Test;
import org.utgenome.format.fastq.FastqRead;
import org.utgenome.format.fastq.FastqReader;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;

public class FastqRenameTest {

	@Test
	public void rename() throws Exception {

		File tmpFastq = FileUtil.createTempFile(new File("target"), "input", ".fastq");
		FileUtil.copy(FileResource.openByteStream(FastqRenameTest.class, "sample.fastq"), tmpFastq);
		tmpFastq.deleteOnExit();

		File renamedFastq = FileUtil.createTempFile(new File("target"), "output", ".fastq");
		renamedFastq.deleteOnExit();

		final String readGroup = "HA0001.PE001.L1";
		UTGBShell.runCommand(String.format("rename-fastq -g %s %s %s", readGroup, tmpFastq, renamedFastq));

		FastqReader orig = new FastqReader(new FileReader(tmpFastq));
		FastqReader renamed = new FastqReader(new FileReader(renamedFastq));

		FastqRead r1, r2;
		int readCount = 0;
		while ((r1 = orig.next()) != null && (r2 = renamed.next()) != null) {
			assertEquals(r1.qual, r2.qual);
			assertEquals(r1.seq, r2.seq);
			assertEquals(r2.seqname, String.format("%s.%d", readGroup, readCount + 1));
			readCount++;
		}

		orig.close();
		renamed.close();

	}
}
