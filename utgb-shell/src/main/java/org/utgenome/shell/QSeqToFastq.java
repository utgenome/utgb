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
// QSeqToFastq.java
// Since: Jul 20, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.utgenome.format.illumina.QSeqToFASTQ;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

public class QSeqToFastq extends UTGBShellCommand {

	@Option(symbol = "g", longName = "readgroup", varName = "RG", description = "read group name (e.g., HG0001SE:L3)")
	private String readGroup;

	@Option(symbol = "s", description = "read name suffix (e.g., /1 or /2 for paired-end runs")
	private String suffix = "";

	@Argument(index = 0, name = "input")
	private String input = "-";
	@Argument(index = 1, name = "output")
	private String output = "-";

	@Option(longName = "nofilter", description = "do not apply quality filter (default: false)")
	private boolean disableQualityFilter = false;

	@Override
	public void execute(String[] args) throws Exception {

		QSeqToFASTQ converter = readGroup == null ? new QSeqToFASTQ(disableQualityFilter) : new QSeqToFASTQ(readGroup, disableQualityFilter);
		converter.setReadNameSuffix(suffix);

		boolean closeIn = false;
		boolean closeOut = false;

		BufferedReader inputReader = null;
		BufferedWriter outputWriter = null;

		try {
			if ("-".equals(input)) {
				inputReader = new BufferedReader(new InputStreamReader(System.in));
			}
			else {
				inputReader = new BufferedReader(new FileReader(input));
				closeIn = true;
			}

			if ("-".equals(output)) {
				outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));
			}
			else {
				outputWriter = new BufferedWriter(new FileWriter(output));
				closeOut = true;
			}

			converter.convert(inputReader, outputWriter);
		}
		finally {
			if (closeIn && inputReader != null)
				inputReader.close();

			if (closeOut && outputWriter != null)
				outputWriter.close();
		}
	}

	@Override
	public String getOneLinerDescription() {
		return "convert Illumina's qseq format into FASTQ";
	}

	@Override
	public String name() {
		return "qseq2fastq";
	}

}
