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
// GWTCompile.java
// Since: 2010/04/30
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import org.utgenome.shell.Maven.CommandExecutor;
import org.xerial.util.log.Logger;

public class GWTCompile extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(GWTCompile.class);

	@Override
	public void execute(String[] args) throws Exception {

		Maven.runMaven("-q dependency:build-classpath -Dmdep.outputFile=target/classpath");

		_logger.info("executing java task...");

		CommandExecutor exec = new CommandExecutor();
		exec.execCommand("java -version", null, null);

		_logger.info("done.");
	}

	@Override
	public String getOneLinerDescription() {
		return "(beta) compile GWT codes (Java) into JavaScript ones";
	}

	@Override
	public String name() {
		return "gwt-compile";
	}

}
