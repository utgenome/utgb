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
// GWTTest.java
// Since: Nov 26, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GWTTest {

	static String tmpDir = "target"; // System.getProperty("java.io.tmpdir");
	public static String appName = "sample";

	@Before
	public void setUp() throws Exception {

		// create temporary application

		int count = 0;
		while (new File(tmpDir, appName).exists()) {
			appName = "sample" + count++;
		}

		// create a web application scaffold
		UTGBShell.runCommand(new String[] { "create", "-d", tmpDir, appName });

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGWT() throws Exception {

		final String projectPath = new File(tmpDir, appName).getAbsolutePath();

		UTGBShell.runCommand(new String[] { "gwt", "-d", projectPath });
		UTGBShell.runCommand(new String[] { "compile", "-d", projectPath });

	}

}
