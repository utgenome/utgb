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
// UTGBShellTest.java
// Since: Oct 21, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.util.log.Logger;

public class UTGBShellTest {

	private static Logger _logger = Logger.getLogger(UTGBShellTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void commandList() throws Exception {
		Set<String> commandSet = UTGBShell.getSubCommandNameSet();

		String[] commands = new String[] { "action", "clean", "compile", "create", "deploy", "eclipse", "gwt", "import", "maven", "repair", "svn-add", "server" };
		for (String each : commands)
			assertTrue(commandSet.contains(each));
	}

	@Test
	public void displayHelp() throws Exception {
		for (String eachCommand : UTGBShell.getSubCommandNameSet()) {
			_logger.debug("test: " + eachCommand);
			UTGBShell.runCommand(new String[] { eachCommand, "--help" });
		}
	}
}
