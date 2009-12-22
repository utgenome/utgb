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
// GWT.java
// Since: Jun 2, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.IOException;

import org.utgenome.config.UTGBConfig;
import org.xerial.util.log.Logger;

/**
 * A sub command for creating a GWT interface
 * 
 * @author leo
 * 
 */
public class GWT extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(GWT.class);

	@Override
	public void execute(String[] args) throws Exception {
		UTGBConfig config = loadUTGBConfig();

		if (!isInProjectRoot())
			throw new UTGBShellException("must be in the project root");

		String packageName = config.javaPackage;
		String projectName = config.projectName;

		ScaffoldGenerator generator = new ScaffoldGenerator(globalOption.projectDir, new Create.CreateAllScaffoldFileFilter());
		try {
			generator.createGWTModuleScaffold(config);
		}
		catch (IOException e) {
			_logger.error("failed to craate GWT module: " + e.getMessage());
		}

	}

	@Override
	public String name() {
		return "gwt";
	}

	public String getOneLinerDescription() {
		return "create a GWT interface";
	}

}
