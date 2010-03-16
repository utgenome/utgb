/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// SAM2SilkReaderTest.java
// Since: Mar 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.sam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.core.XerialException;
import org.xerial.lens.Lens;
import org.xerial.lens.ObjectHandler;
import org.xerial.util.FileResource;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

public class SAM2SilkReaderTest {

	private static Logger _logger = Logger.getLogger(SAM2SilkReaderTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void toSilkTest() throws Exception {
		Lens.findFromSilk(new SAM2SilkReader(FileResource.open(SAM2SilkReaderTest.class, "chr21.sam")), "record", SAMEntry.class,
				new ObjectHandler<SAMEntry>() {
					public void handle(SAMEntry input) throws Exception {
						_logger.info(Lens.toSilk(input));
					}
				});
	}

	@Test
	public void bssSAM() throws XerialException, IOException {
		StringWriter w = new StringWriter();
		BufferedReader b = new BufferedReader(new SAM2SilkReader(FileResource.open(SAM2SilkReaderTest.class, "bss-align.sam")));
		String line;
		while ((line = b.readLine()) != null) {
			w.append(line);
			w.append(StringUtil.NEW_LINE);
		}
		_logger.info(w.toString());

		Lens.findFromSilk(new SAM2SilkReader(FileResource.open(SAM2SilkReaderTest.class, "bss-align.sam")), "record", SAMEntry.class,
				new ObjectHandler<SAMEntry>() {
					public void handle(SAMEntry input) throws Exception {
						_logger.info(Lens.toSilk(input));
					}
				});
	}
}
