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
// WIG2SilkReader.java
// Since: Aug 28, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-shell/src/main/java/org/utgenome/shell/db/wig/WIG2SilkReader.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.format.wig;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.utgenome.UTGBException;
import org.utgenome.format.FormatConversionReader;
import org.xerial.util.log.Logger;

/**
 * Stream reader of the WIG data converted to Silk
 * 
 * @author leo
 * 
 */
public class WIG2SilkReader extends FormatConversionReader {

	private static Logger _logger = Logger.getLogger(WIG2SilkReader.class);

	public WIG2SilkReader(Reader wigReader) throws IOException {
		super(wigReader, new PipeWorker(wigReader));
	}

	private static class PipeWorker extends PipeConsumer {

		private final WIG2Silk wig2silk;

		public PipeWorker(Reader in) throws IOException {
			wig2silk = new WIG2Silk(in);
		}

		@Override
		public void consume(Reader in, Writer out) throws Exception {
			if (out == null)
				return;
			try {
				wig2silk.toSilk(new PrintWriter(out));
				out.close();
			}
			catch (IOException e) {
				_logger.error(e);
			}
			catch (UTGBException e) {
				_logger.error(e);
			}
		}

	}

}
