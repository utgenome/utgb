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
// FastqRename.java
// Since: 2010/10/06
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
import java.io.Reader;
import java.io.Writer;

import org.utgenome.UTGBException;
import org.utgenome.format.fastq.FastqRead;
import org.utgenome.format.fastq.FastqReader;
import org.utgenome.util.StandardInputStream;
import org.utgenome.util.StandardOutputStream;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

public class FastqRename extends UTGBShellCommand {

	@Option(symbol = "g", longName = "readgroup", varName = "RG", description = "read group name (e.g., HG0001.PE001.L3)")
	private String readGroup;

	@Argument(index = 0, name = "input")
	private String input = "-";

	@Argument(index = 1, name = "input")
	private String output = "-";

	@Override
	public String name() {
		return "rename-fastq";
	}

	@Override
	public void execute(String[] args) throws Exception {

		if (readGroup == null)
			throw new UTGBException("-g (read group prefix) must be specified");

		Reader in = null;
		if ("-".equals(input))
			in = new BufferedReader(new InputStreamReader(new StandardInputStream()));
		else
			in = new BufferedReader(new FileReader(input));

		Writer out = null;
		if ("-".equals(output))
			out = new BufferedWriter(new OutputStreamWriter(new StandardOutputStream()));
		else
			out = new BufferedWriter(new FileWriter(output));

		int readCount = 0;

		FastqReader fastq = new FastqReader(in);
		for (FastqRead read; (read = fastq.next()) != null; readCount++) {
			read.seqname = String.format("%s.%d", readGroup, readCount + 1);
			out.append(read.toFASTQString());
		}

		out.flush();
		out.close();

		in.close();
	}

	@Override
	public String getOneLinerDescription() {
		return "rename fastq reads";
	}

}
