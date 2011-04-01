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
// SAM2Silk.java
// Since: 2011/03/28
//
//--------------------------------------
package org.utgenome.core.cui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import org.utgenome.format.sam.SAM2SilkReader;
import org.xerial.util.io.StandardInputStream;
import org.xerial.util.io.StandardOutputStream;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;

public class SAM2Silk extends UTGBCommandBase {

	private static Logger _logger = Logger.getLogger(SAM2Silk.class);

	@Argument(index = 0)
	String input = "-";

	@Argument(index = 1)
	String output = "-";

	@Override
	public void execute(String[] args) throws Exception {

		final int BUFFER_SIZE = 4 * 1024 * 1024;

		InputStream in = null;
		if ("-".equals(input)) {
			_logger.info("input STDIN");
			in = new StandardInputStream();
		}
		else {
			_logger.info("input " + input);
			in = new BufferedInputStream(new FileInputStream(input), BUFFER_SIZE);
		}

		PrintWriter out = null;
		if ("-".equals(output)) {
			_logger.info("output STDOUT");
			out = new PrintWriter(new BufferedOutputStream(new StandardOutputStream(), BUFFER_SIZE));
		}
		else {
			_logger.info("output " + output);
			out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(output), BUFFER_SIZE));
		}

		SAM2SilkReader converter = new SAM2SilkReader(in);
		char[] buf = new char[BUFFER_SIZE];
		try {
			for (int numRead; (numRead = converter.read(buf, 0, buf.length)) != -1;)
				out.write(buf, 0, numRead);
		}
		finally {
			converter.close();
			out.close();
		}

	}

	@Override
	public String getOneLineDescription() {
		return "sam -> silk conversion";
	}

	@Override
	public String name() {
		return "sam2silk";
	}

}
