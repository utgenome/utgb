/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// FastqToFasta.java
// Since: 2010/10/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.utgenome.format.fastq.FastqRead;
import org.utgenome.format.fastq.FastqReader;
import org.utgenome.util.StandardInputStream;
import org.utgenome.util.StandardOutputStream;
import org.xerial.util.opt.Argument;

/**
 * fastq2fasta command
 * 
 * @author leo
 * 
 */
public class FastqToFasta extends UTGBShellCommand {

	@Argument(index = 0, name = "input")
	private String input = "-";

	@Argument(index = 1, name = "output")
	private String output = "-";

	@Override
	public void execute(String[] args) throws Exception {

		Reader in = new BufferedReader(new InputStreamReader("-".equals(input) ? new StandardInputStream() : new FileInputStream(input)));
		Writer out = new BufferedWriter(new OutputStreamWriter("-".equals(output) ? new StandardOutputStream() : new FileOutputStream(output)));

		FastqReader fastqReader = new FastqReader(in);
		for (FastqRead read; (read = fastqReader.next()) != null;) {
			out.append(">");
			out.append(read.seqname);
			out.append("\n");
			out.append(read.seq);
			out.append("\n");
		}

		out.flush();
		out.close();
		in.close();
	}

	@Override
	public String name() {
		return "fastq2fasta";
	}

	@Override
	public String getOneLinerDescription() {
		return "convert FASTQ to FASTA format";
	}

}
