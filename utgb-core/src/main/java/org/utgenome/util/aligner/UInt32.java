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
// LongArray.java
// Since: 2010/10/27
//
//--------------------------------------
package org.utgenome.util.aligner;

/**
 * Uint32 value
 * 
 * @author leo
 * 
 */
public class UInt32 implements Comparable<UInt32> {

	private final byte[] value;

	public UInt32(long v) {
		value = new byte[4];
		value[0] = (byte) ((v >> 24) & 0xFF);
		value[1] = (byte) ((v >> 16) & 0xFF);
		value[2] = (byte) ((v >> 8) & 0xFF);
		value[3] = (byte) (v & 0xFF);
	}

	public int compareTo(UInt32 o) {
		for (int i = 0; i < 4; ++i) {
			int left = value[i] & 0xFF;
			int right = o.value[i] & 0xFF;
			int diff = left - right;
			if (diff != 0)
				return diff;
		}
		return 0;
	}

	public long toLong() {
		long v = 0;
		for (int i = 0; i < 3; i++) {
			v |= value[i] & 0xFF;
			v <<= 8;
		}
		v |= value[3] & 0xFF;
		return v;
	}

	@Override
	public String toString() {
		return Long.toString(toLong());
	}

}
