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
// Blast2SilkTest.java
// Since: 2010/09/02
//
//--------------------------------------
package org.utgenome.format.blast;

import java.io.BufferedReader;

import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class Blast2SilkTest {

	private static Logger _logger = Logger.getLogger(Blast2SilkTest.class);

	@Test
	public void testReading() throws Exception {
		Blast2Silk b2s = new Blast2Silk(FileResource.open(Blast2SilkTest.class, "sample.blast"));
		BufferedReader reader = new BufferedReader(b2s);
		for (String line; (line = reader.readLine()) != null;) {
			_logger.info(line);
		}
	}

}
