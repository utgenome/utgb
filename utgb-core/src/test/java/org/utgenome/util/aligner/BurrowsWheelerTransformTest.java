/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// BurrowsWheelerTransformTest.java
// Since: 2011/02/09
//
//--------------------------------------
package org.utgenome.util.aligner;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;
import org.xerial.util.log.Logger;

public class BurrowsWheelerTransformTest {

	private static Logger _logger = Logger.getLogger(BurrowsWheelerTransformTest.class);

	public static class StringW implements RandomAccess {
		public char[] buf;

		public StringW(String s) {
			buf = s.toCharArray();
		}

		public StringW(char[] c) {
			buf = c;
		}

		public long size() {
			return buf.length + 1;
		}

		public int get(long index) {
			if (index == buf.length)
				return 0;
			else
				return buf[(int) index];
		}

		public void set(long index, int value) {
			buf[(int) index] = (char) (value & 0xFF);
		}
	}

	@Test
	public void sais() throws Exception {

		String s = "abracadabra";
		//String s = "SIX.MIXED.PIXIES.SIFT.SIXTY.PIXIE.DUST.BOXES";

		char[] T = s.toCharArray();
		char[] B = new char[T.length];
		int[] A = new int[T.length];

		SAIS.suffixsort(T, A);
		_logger.info("\n" + Arrays.toString(A));
		int bwtIndex = SAIS.bwtransform(T, B, A);

		char[] B2 = BurrowsWheelerTransform.build(new StringW(T));

		_logger.info("IN: " + toString(T));
		_logger.info("SAIS BWT:" + toString(B));
		_logger.info("My   BWT:" + toString(B2));

		String ansBWT = "STEXYDST.E.IXXIIXXSSMPPS.B..EE..USFXDIIOIIIT";

	}

	public static String toString(char[] c) {
		StringWriter w = new StringWriter();
		for (int i = 0; i < c.length; ++i) {
			w.append(c[i]);
		}
		return w.toString();

	}

}
