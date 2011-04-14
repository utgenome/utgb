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
// Convert.java
// Since: 2011/03/22
//
//--------------------------------------
package org.utgenome.core.cui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.utgenome.format.bed.BED2SilkReader;
import org.utgenome.format.fastq.FastqToSilkReader;
import org.utgenome.format.sam.SAM2SilkReader;
import org.utgenome.format.wig.WIG2SilkReader;
import org.utgenome.util.StandardOutputStream;
import org.xerial.util.io.StandardInputStream;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;

public class Convert extends UTGBCommandBase {

	private static Logger _logger = Logger.getLogger(Convert.class);

	@Override
	public String name() {
		return "convert";
	}

	@Override
	public String getOneLineDescription() {
		return "(BETA) text format converter";
	}

	@Argument
	private String inputFile;

	@Override
	public void execute(String[] args) throws Exception {
		_logger.info("convert");

		InputStream in = "-".equals(inputFile) ? new StandardInputStream() : new BufferedInputStream(new FileInputStream(inputFile));

		OutputStreamWriter out = new OutputStreamWriter(new StandardOutputStream());
		Reader silkInput = null;
		if (inputFile.endsWith(".fastq")) {
			silkInput = new FastqToSilkReader(new InputStreamReader(in));
		}
		else if (inputFile.endsWith(".bed")) {
			silkInput = new BED2SilkReader(new InputStreamReader(in));
		}
		else if (inputFile.endsWith(".sam") || inputFile.endsWith(".bam")) {
			silkInput = new SAM2SilkReader(in);
		}
		else if (inputFile.endsWith(".wig")) {
			silkInput = new WIG2SilkReader(new InputStreamReader(in));
		}
		else {
			return;
		}

		char[] buf = new char[4 * 1024 * 1024];
		for (int readBytes = 0; (readBytes = silkInput.read(buf)) != -1;) {
			out.write(buf, 0, readBytes);
		}

		out.close();
		in.close();

	}

}
