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
// Illumina2Fastq.java
// Since: Jun 14, 2010
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

import org.utgenome.format.illumina.Seq2Fastq;

public class Illumina2Fastq extends UTGBShellCommand {

	/**
	 * 
	 */
	private String in = "-";
	private String out = "-";

	@Override
	public void execute(String[] args) throws Exception {

		BufferedReader input = null;
		BufferedWriter output = null;
		if ("-".equals(in)) {
			input = new BufferedReader(new InputStreamReader(System.in));
		}
		else {
			input = new BufferedReader(new FileReader(in));
		}

		if ("-".equals(out)) {
			output = new BufferedWriter(new OutputStreamWriter(System.out));
		}
		else {
			output = new BufferedWriter(new FileWriter(out));
		}

		Seq2Fastq.convert(input, output);

	}

	@Override
	public String getOneLinerDescription() {
		return "converting Illumina's raw read (*_sequence.txt) into fastq format";
	}

	@Override
	public String name() {
		return "illumina2fastq";
	}

}
