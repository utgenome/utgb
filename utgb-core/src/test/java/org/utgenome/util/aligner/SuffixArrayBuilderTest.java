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
// SuffixArrayBuilderTest.java
// Since: 2010/10/28
//
//--------------------------------------
package org.utgenome.util.aligner;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.xerial.util.log.Logger;

public class SuffixArrayBuilderTest {

	private static Logger _logger = Logger.getLogger(SuffixArrayBuilderTest.class);

	@Test
	public void sais() throws Exception {

		String s = "mmiissiissiippii";
		int[] SA = new int[s.length() + 1];
		new SuffixArrayBuilder(s).SAIS(SA);

		List<Integer> SA_v = new ArrayList<Integer>();
		for (int each : SA)
			SA_v.add(each);

		_logger.info(SA_v);

	}
}
