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
// BurrowsWheelerTransform.java
// Since: 2011/02/09
//
//--------------------------------------
package org.utgenome.util.aligner;

import java.io.StringWriter;
import java.util.Arrays;

import org.xerial.util.log.Logger;

public class BurrowsWheelerTransform {

	private static Logger _logger = Logger.getLogger(BurrowsWheelerTransform.class);

	public static class ByteWrap implements RandomAccess {

		private final byte[] input;

		public ByteWrap(byte[] input) {
			this.input = input;
		}

		public int get(long index) {
			return input[(int) index];
		}

		public void set(long index, int value) {
			input[(int) index] = (byte) (value & 0xFF);
		}

		public long size() {
			return input.length;
		}

	}

	public static char[] build(RandomAccess input) {
		int[] SA = new SuffixArrayBuilder(input, (int) input.size(), 127).SAIS();
		_logger.info("\n" + Arrays.toString(SA));
		char[] bwt = new char[SA.length];

		for (int i = 0; i < SA.length; ++i) {
			if (SA[i] == 0) {
				bwt[i] = 0;
			}
			else
				bwt[i] = (char) (input.get(SA[i] - 1) & 0xFFFF);
		}

		return bwt;
	}

	public static String toString(char[] c) {
		StringWriter w = new StringWriter();
		for (int i = 0; i < c.length; ++i) {
			w.append(c[i]);
		}
		return w.toString();
	}

}
