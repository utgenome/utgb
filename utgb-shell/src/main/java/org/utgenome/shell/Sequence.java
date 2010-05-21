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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.utgenome.format.fasta.CompactFASTA;
import org.utgenome.format.fasta.GenomeSequence;
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

		Pattern p = Pattern.compile("([^:]+)(:([0-9]+)-([0-9]+))?");
		Matcher m = p.matcher(query);
		if (!m.matches())
			throw new UTGBShellException("invalid query format:" + query);
		String chr = m.group(1);
		String sStart = m.group(3);
		String sEnd = m.group(4);

		int start = 0;
		if (sStart != null)
			start = Integer.parseInt(sStart) - 1;
		int end = Integer.MAX_VALUE;
		if (sEnd != null)
			end = Integer.parseInt(sEnd);

		GenomeSequence seq = f.getSequence(chr, start, end);
		if (seq == null) {
			_logger.warn("no entry found: " + chr);
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
