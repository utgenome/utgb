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
// utgb-core Project
//
// GFF3ReaderTest.java
// Since: Jul 7, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.gff3;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.util.FileResource;

public class GFF3ReaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void load() throws IOException {

		GFF3Reader reader = new GFF3Reader(FileResource.open(GFF3ReaderTest.class, "sample.gff3"));

	}

}
