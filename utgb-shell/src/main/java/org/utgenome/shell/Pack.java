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
// Pack.java
// Since: Mar 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.net.URL;
import java.util.regex.Pattern;

import org.utgenome.UTGBException;
import org.utgenome.format.fasta.CompactFASTAGenerator;
import org.xerial.util.StopWatch;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

/**
 * creating packed FASTA
 * 
 * @author leo
 * 
 */
public class Pack extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(Pack.class);

	@Argument(index = 0)
	private String inputFile = null;

	@Option(symbol = "o", description = "output directory. default = db")
	private String outputDir = "db";

	@Override
	public void execute(String[] args) throws Exception {

		if (inputFile == null)
			throw new UTGBException("missing an input file");

		_logger.info("input file: " + inputFile);
		_logger.info("output dir: " + outputDir);

		StopWatch sw = new StopWatch();
		CompactFASTAGenerator g = new CompactFASTAGenerator();
		g.setWorkDir(outputDir);
		// is protocol?
		if (Pattern.matches("^\\w+:\\/.*", inputFile)) {
			g.packFASTA(new URL(inputFile));
		}
		else
			g.packFASTA(inputFile);

		_logger.info(String.format("elapsed time %s sec.", sw.getElapsedTime()));
	}

	@Override
	public String getOneLinerDescription() {
		return "create a pack file of the input FASTA sequences";
	}

	@Override
	public String name() {
		return "pack";
	}

}
