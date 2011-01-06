/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// ScreenShotTest.java
// Since: 2011/01/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.junit.Test;
import org.utgenome.format.sam.SAMReader;
import org.utgenome.util.TestHelper;

public class ScreenShotTest {

	@Test
	public void query() throws Exception {
		File bam = TestHelper.createTempFileFrom(ScreenShotTest.class, "sample.bam");
		File bai = TestHelper.createTempFileFrom(ScreenShotTest.class, "sample.bam.bai", SAMReader.getBamIndexFile(bam));

		UTGBShell.runCommand(String.format("screenshot -i %s chr20:25000-50000 --outdir=target", bam.getAbsoluteFile()));

	}

	@Test
	public void queryFile() throws Exception {
		File bam = TestHelper.createTempFileFrom(ScreenShotTest.class, "sample.bam");
		File bai = TestHelper.createTempFileFrom(ScreenShotTest.class, "sample.bam.bai", SAMReader.getBamIndexFile(bam));
		File queryFile = TestHelper.createTempFileFrom(ScreenShotTest.class, "region.silk");
		UTGBShell.runCommand(String.format("screenshot -i %s --outdir=target -q %s", bam.getAbsoluteFile(), queryFile.getAbsoluteFile()));
	}
}
