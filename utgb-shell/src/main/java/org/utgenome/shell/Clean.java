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
// Clean.java
// Since: May 27, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.xerial.util.FileUtil;
import org.xerial.util.log.Logger;

/**
 * UTGB Shell Sub Command for cleaning target directory
 * 
 * @author leo
 * 
 */
public class Clean extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(Clean.class);

	@Override
	public void execute(String[] args) throws Exception {

		if (!isInProjectRoot())
			throw new UTGBShellException("not in the project root");

		// clean war/utgb
		_logger.info("clean war/utgb ...");
		FileUtil.rmdir(new File(getProjectRoot(), "war/utgb"));

		// clean target folder
		maven("clean");
	}

	@Override
	public String name() {
		return "clean";
	}

	@Override
	public String getOneLinerDescription() {
		return "clean the target folder";
	}

}
