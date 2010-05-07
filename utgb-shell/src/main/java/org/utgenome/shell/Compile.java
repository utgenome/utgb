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
// Compile.java
// Since: Jan 15, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.xerial.util.FileUtil;
import org.xerial.util.StringUtil;

/**
 * 
 * A UTGBShell sub command for compiling java source codes with Maven
 * 
 * @author leo
 * 
 */
public class Compile extends UTGBShellCommand {

	@Override
	public void execute(String[] args) throws Exception {

		if (!isInProjectRoot())
			throw new UTGBShellException("not in the project root");

		// create war/utgb folder
		FileUtil.mkdirs(new File(getProjectRoot(), "war/utgb"));

		// run mvn compile
		String commandLine = "compile " + StringUtil.join(args, " ");
		maven(commandLine);
	}

	@Override
	public String name() {
		return "compile";
	}

	public String getOneLinerDescription() {
		return "complie java source codes";
	}

}
