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
// ScreenShot.java
// Since: 2011/01/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

public class ScreenShot extends UTGBShellCommand {

	@Override
	public String name() {
		return "screenshot";
	}

	@Option(symbol = "f", longName = "file", description = "read file to query (BAM/BED, etc.)")
	private String readFile;

	@Option(longName = "outdir", description = "output folder. default is the current directory")
	private File outputFolder;

	@Option(symbol = "q", description = "query file in Silk format -region(chr, start, end)")
	private File queryFile;

	@Argument(index = 0, name = "query")
	private String query;

	@Override
	public void execute(String[] args) throws Exception {

	}

	@Override
	public String getOneLinerDescription() {
		return "take the screenshot of the specified region";
	}

}
