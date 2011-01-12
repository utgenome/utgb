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
// SAMToFastq.java
// Since: 2011/01/12
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMFileReader.ValidationStringency;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecordIterator;

import org.utgenome.format.fastq.FastqRead;
import org.utgenome.util.StandardInputStream;
import org.xerial.util.io.StandardOutputStream;
import org.xerial.util.opt.Argument;

public class SAMToFastq extends UTGBShellCommand {

	@Override
	public String name() {
		return "sam2fastq";
	}

	@Argument(index = 0)
	private String input = "-";

	@Argument(index = 1)
	private String output = "-";

	@Override
	public void execute(String[] args) throws Exception {

		SAMFileReader sam;
		SAMFileReader.setDefaultValidationStringency(ValidationStringency.SILENT);
		if ("-".equals(input)) {
			sam = new SAMFileReader(new StandardInputStream(), false);
		}
		else {
			sam = new SAMFileReader(new File(input), false);
		}

		BufferedWriter out;
		if ("-".equals(output)) {
			out = new BufferedWriter(new OutputStreamWriter(new StandardOutputStream()));
		}
		else {
			out = new BufferedWriter(new FileWriter(output));
		}

		SAMRecordIterator it = sam.iterator();
		try {
			for (; it.hasNext();) {
				SAMRecord rec = it.next();
				FastqRead fastq = new FastqRead(rec.getReadName(), rec.getReadString(), rec.getBaseQualityString());
				out.append(fastq.toFASTQString());
			}
		}
		finally {
			out.flush();
			out.close();
			it.close();
			sam.close();
		}
	}

	@Override
	public String getOneLinerDescription() {
		return "convert sam/bam file into fastq format";
	}

}
