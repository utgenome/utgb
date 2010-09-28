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
// Sam2WigTest.java
// Since: 2010/09/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;

public class Sam2WigTest {

	@Test
	public void convert() throws Exception {
		File work = new File("target");
		File in = FileUtil.createTempFile(work, "input", ".sam");
		File out = FileUtil.createTempFile(work, "output", ".wig");

		FileUtil.copy(FileResource.openByteStream(Sam2WigTest.class, "test.sam"), in);

		UTGBShell.runCommand(String.format("sam2wig %s %s", in, out));

		UTGBShell.runCommand(String.format("import -w %s", out));
	}
}
