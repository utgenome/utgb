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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.xerial.util.log.Logger;

public class SuffixArrayBuilderTest {

	private static Logger _logger = Logger.getLogger(SuffixArrayBuilderTest.class);

	@Test
	public void sais() throws Exception {

		String s = "mmiissiissiippii";
		int[] SA = new SuffixArrayBuilder(s).SAIS();

		List<Integer> SA_v = new ArrayList<Integer>();
		for (int each : SA)
			SA_v.add(each);

		int[] answer = { 16, 15, 14, 10, 6, 2, 11, 7, 3, 1, 0, 13, 12, 9, 5, 8, 4 };
		List<Integer> ans = new ArrayList<Integer>();
		for (int each : answer)
			ans.add(each);

		_logger.debug(SA_v);
		assertEquals(ans, SA_v);

	}

	public static class IntWrap implements RandomAccess {

		int[] array;

		public IntWrap(int[] array) {
			this.array = array;
		}

		public int get(long index) {
			return array[(int) index];
		}

		public void set(long index, int value) {
			array[(int) index] = value;
		}

	}

	@Test
	public void sais2() throws Exception {

		String s = "ATAATACGATAATAA";
		// A:0, T:1, G:2, C:3
		int[] s_i = new int[s.length() + 1];
		for (int i = 0; i < s.length(); ++i) {
			switch (s.charAt(i)) {
			case 'A':
				s_i[i] = 1;
				break;
			case 'T':
				s_i[i] = 2;
				break;
			case 'G':
				s_i[i] = 3;
				break;
			case 'C':
				s_i[i] = 4;
				break;
			}
		}
		s_i[s.length()] = 0;

		int[] SA = new SuffixArrayBuilder(new IntWrap(s_i), 16, 4).SAIS();

		List<Integer> SA_v = new ArrayList<Integer>();
		for (int each : SA)
			SA_v.add(each);

		_logger.debug(SA_v);

	}

}
