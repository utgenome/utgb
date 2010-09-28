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
// Sam2WigConverterTest.java
// Since: 2010/09/28
//
//--------------------------------------
package org.utgenome.format.sam;

import java.io.OutputStreamWriter;

import org.junit.Test;
import org.xerial.util.FileResource;

public class Sam2WigConverterTest {

	@Test
	public void convert() throws Exception {

		Sam2WigConverter converter = new Sam2WigConverter();
		converter.convert(FileResource.openByteStream(Sam2WigConverterTest.class, "coverage.sam"), new OutputStreamWriter(System.err));

	}
}
