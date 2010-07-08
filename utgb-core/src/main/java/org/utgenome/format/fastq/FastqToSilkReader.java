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
import org.xerial.silk.SilkWriter;

/**
 * Read the FASTQ file input from the given reader as if it were a Silk
 * 
 * @author leo
 * 
 */
public class FastqToSilkReader extends FormatConversionReader {

	public FastqToSilkReader(Reader fastqInput) throws IOException {
		super(fastqInput, new PipeConsumer() {
			@Override
			public void consume(Reader in, Writer out) throws Exception {
				FastqReader reader = new FastqReader(in);
				SilkWriter silk = new SilkWriter(out);
				for (FastqRead read = null; (read = reader.next()) != null;) {
					read.toSilk(silk);
				}
				silk.flush();
			}
		});

	}
}
