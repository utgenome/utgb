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
// BED2SilkReader.java
// Since: May 26, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-shell/src/main/java/org/utgenome/shell/db/bed/BED2SilkReader.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.format.bed;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.utgenome.UTGBException;
import org.utgenome.format.FormatConversionReader;
import org.xerial.lens.Lens;

/**
 * BED2SilkReader translates the input BED data into a Silk format
 * 
 * @author leo
 * 
 */
public class BED2SilkReader extends FormatConversionReader {

	public BED2SilkReader(Reader bedReader) throws IOException {
		super(bedReader, new PipeConsumer() {
			@Override
			public void consume(Reader in, Writer out) throws Exception {
				BED2Silk converter = new BED2Silk(in);
				PrintWriter pout = new PrintWriter(out);
				converter.toSilk(pout);
			}
		});

	}

	public static BEDQuery scan(Reader input, BEDQuery query) throws UTGBException {

		try {
			BED2SilkReader in = new BED2SilkReader(input);
			Lens.loadSilk(query, in);
			return query;
		}
		catch (Exception e) {
			throw UTGBException.convert(e);
		}
	}

}
