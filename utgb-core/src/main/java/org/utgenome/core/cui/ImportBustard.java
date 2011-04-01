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
// utgb-core Project
//
// ImportBustard.java
// Since: 2011/03/24
//
//--------------------------------------
package org.utgenome.core.cui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.utgenome.shell.UTGBShellException;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

public class ImportBustard extends UTGBCommandBase {

	private static Logger _logger = Logger.getLogger(ImportBustard.class);

	@Argument(index = 0)
	private File bustardFolder;

	@Option(symbol = "g", description = "read name (group) prefix")
	private String readNameprefix;

	@Option(symbol = "L", description = "lane number to import (default = all)")
	private List<Integer> lane = new ArrayList<Integer>();

	@Override
	public void execute(String[] args) throws Exception {
		if (bustardFolder == null) {
			throw new UTGBShellException("no input Bustard folder");
		}

		if (!bustardFolder.exists()) {
			throw new UTGBShellException(bustardFolder + " is not found");
		}

	}

	@Override
	public String getOneLineDescription() {
		return "import Bustard (base caller) results";
	}

	@Override
	public String name() {
		return "import-bustard";
	}

}
