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
// Eclipse.java
// Since: Feb 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

/**
 * A sub command to generate Eclipse project files
 * 
 * @author leo
 * 
 */
public class Eclipse extends UTGBShellCommand {

	@Override
	public void execute(String[] args) throws UTGBShellException {
		Maven.runMaven("eclipse:eclipse");
	}

	@Override
	public String name() {
		return "eclipse";
	}

	public String getOneLinerDescription() {
		return "create (or updates) Eclipse project files";
	}

}
