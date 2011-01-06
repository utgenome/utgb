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
// Sequence.java
// Since: Mar 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import org.utgenome.format.fasta.CompactFASTA;
import org.utgenome.format.fasta.GenomeSequence;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;

/**
 * Retrieve the genome sequence from the pack file
 * 
 * @author leo
 * 
 */
public class Sequence extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(Sequence.class);

	@Argument(index = 0, name = "(ref pac)", required = true)
	private String packFilePrefix;

	@Argument(index = 1, name = "(chr:start-end)")
	private String query;

	@Override
	public void execute(String[] args) throws Exception {

		if (query == null)
			throw new UTGBShellException("no query is given");

		CompactFASTA f = new CompactFASTA(packFilePrefix);

		ChrLoc loc = RegionQueryExpr.parse(query);
		GenomeSequence seq = f.getSequence(loc.chr, loc.start - 1, loc.end);
		if (seq == null) {
			_logger.warn("no entry found: " + loc);
		}

		int cursor = 0;
		final int step = 50;
		while (cursor < seq.length()) {
			for (int i = 0; i < step && cursor < seq.length(); ++i, ++cursor) {
				System.out.print(seq.charAt(cursor));
			}
			System.out.println();
		}

	}

	@Override
	public String getOneLinerDescription() {
		return "retrieve a genome sequence from a pack file";
	}

	@Override
	public String name() {
		return "seq";
	}

}
