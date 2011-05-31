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
// utgb-core Project
//
// FastqToSilkReader.java
// Since: 2010/07/08
//
//--------------------------------------
package org.utgenome.format.fastq;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.utgenome.format.FormatConversionReader;
import org.xerial.util.StopWatch;
import org.xerial.util.log.Logger;

/**
 * Read the FASTQ file input from the given reader as if it were a Silk
 * 
 * @author leo
 * 
 */
public class FastqToSilkReader extends FormatConversionReader {

	private static Logger _logger = Logger.getLogger(FastqToSilkReader.class);

	public FastqToSilkReader(Reader fastqInput) throws IOException {
		super(fastqInput, new PipeConsumer() {
			@Override
			public void consume(Reader in, Writer out) throws Exception {
				FastqReader reader = new FastqReader(in);
				StopWatch sw = new StopWatch();
				int count = 0;
				int prevCount = 0;
				double prevTime = sw.getElapsedTime();
				for (FastqRead read = null; (read = reader.next()) != null;) {
					out.append(read.toSilk());
					count++;
					if (count % 100000 == 0) {
						double t = sw.getElapsedTime();
						double lapTime = t - prevTime;
						int numReadInLap = count - prevCount;
						_logger.debug(String.format("processed %,d reads: %.2f sec. %,d reads / sec", count, t, (int) (numReadInLap / lapTime)));

						prevTime = t;
						prevCount = count;
					}
				}
			}
		});

	}
}
