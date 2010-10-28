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
// SuffixArrayBuilder.java
// Since: 2010/10/27
//
//--------------------------------------
package org.utgenome.util.aligner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.xerial.util.BitVector;
import org.xerial.util.StringUtil;

/**
 * SA-IS implementation for building suffix arrays
 * 
 * @author leo
 * 
 */
public class SuffixArrayBuilder {
	private RandomAccess input;
	private final int N;
	private final int K;
	private int[] bucket;
	private BitVector typeLS;

	public SuffixArrayBuilder(RandomAccess input, final int N, final int K) {
		this.input = input;
		this.N = N;
		this.K = K;
		this.bucket = new int[K + 1];
		typeLS = new BitVector(N);
	}

	public SuffixArrayBuilder(String input) {
		this.N = input.length() + 1;
		StringWrapper w = new StringWrapper(input);
		this.input = new LightArray(w.array, 0);
		this.K = w.K;
		this.bucket = new int[K + 1];
		typeLS = new BitVector(N);
	}

	public static class StringWrapper implements RandomAccess {
		public final int[] array;
		public final int K;

		public StringWrapper(String s) {
			array = new int[s.length() + 1];

			TreeSet<Character> inputDomain = new TreeSet<Character>();
			for (int i = 0; i < s.length(); ++i) {
				char ch = s.charAt(i);
				inputDomain.add(ch);
			}
			// assign code ID
			HashMap<Character, Integer> codeTable = new HashMap<Character, Integer>();
			int codeCount = 1;
			for (char ch : inputDomain) {
				if (!codeTable.containsKey(ch)) {
					codeTable.put(ch, codeCount++);
				}
			}

			// translate to int array
			for (int i = 0; i < s.length(); ++i) {
				array[i] = codeTable.get(s.charAt(i));
			}
			array[s.length()] = 0;
			this.K = codeTable.size() + 1;
		}

		public int get(int index) {
			return array[index];
		}

		public void set(int index, int value) {
			array[index] = value;
		}
	}

	public static class LightArray implements RandomAccess {

		private final int[] orig;
		private final int offset;

		public LightArray(final int[] orig, int offset) {
			this.orig = orig;
			this.offset = offset;
		}

		public int get(int index) {
			return orig[index + offset];
		}

		public void set(int index, int value) {
			orig[index + offset] = value;
		}

		@Override
		public String toString() {
			ArrayList<Integer> v = new ArrayList<Integer>();
			for (int i = offset; i < orig.length; ++i)
				v.add(orig[i]);
			return StringUtil.join(v, ", ");

		}
	}

	public int[] SAIS() {
		int[] result = new int[N];
		SAIS(result);
		return result;
	}

	public void SAIS(int[] SA) {
		typeLS.set(N - 2, false);
		typeLS.set(N - 1, true); // the sentinel 

		// set the type of each character
		for (int i = N - 3; i >= 0; --i) {
			typeLS.set(i, input.get(i) < input.get(i + 1) || (input.get(i) == input.get(i + 1) && typeLS.get(i + 1)));
		}

		// Step 1: reduce the problem by at least 1/2 
		// sort all the S-substrings

		// create a bucket array
		findEndOfBuckets();

		// initialize the suffix array
		for (int i = 0; i < N; ++i)
			SA[i] = -1;

		for (int i = 1; i < N; ++i) {
			if (isLMS(i))
				SA[--bucket[input.get(i)]] = i;
		}

		induceSA_left(SA);
		induceSA_right(SA);

		// Compact all the sorted subtrings into the first N1 items of SA
		// 2*n1 must be not larger than N 
		int N1 = 0;
		for (int i = 0; i < N; ++i) {
			if (isLMS(SA[i]))
				SA[N1++] = SA[i];
		}

		// init the name array buffer
		for (int i = N1; i < N; ++i)
			SA[i] = -1;
		// find the lexicographic names of substrings
		int name = 0;
		int prev = -1;
		for (int i = 0; i < N1; i++) {
			int pos = SA[i];
			boolean diff = false;

			for (int d = 0; d < N; ++d) {
				if (prev == -1 || input.get(pos + d) != input.get(prev + d) || typeLS.get(pos + d) != typeLS.get(prev + d)) {
					diff = true;
					break;
				}
				else if (d > 0 && (isLMS(pos + d) || isLMS(prev + d)))
					break;
			}

			if (diff) {
				name++;
				prev = pos;
			}

			SA[N1 + (pos >> 1)] = name - 1;
		}

		for (int i = N - 1, j = N - 1; i >= N1; --i) {
			if (SA[i] >= 0)
				SA[j--] = SA[i];
		}

		// Step 2: solve the reduced problem
		int SA1[] = SA;
		RandomAccess inputS1 = new LightArray(SA, N - N1);
		if (name < N1) {
			new SuffixArrayBuilder(inputS1, N1, name - 1).SAIS(SA1);
		}
		else {
			// Generate the suffix array of inputS1 directory.
			for (int i = 0; i < N1; i++)
				SA1[inputS1.get(i)] = i;
		}

		// Step 3: Induce the result for the original problem
		findEndOfBuckets();
		for (int i = 1, j = 0; i < N; ++i) {
			if (isLMS(i))
				inputS1.set(j++, i); // get p1
		}
		for (int i = 0; i < N1; ++i) {
			SA1[i] = inputS1.get(SA1[i]);
		}
		// init SA[N1 .. N-1]
		for (int i = N1; i < N; ++i) {
			SA[i] = -1;
		}
		for (int i = N1 - 1; i >= 0; --i) {
			int j = SA[i];
			SA[i] = -1;
			SA[--bucket[input.get(j)]] = j;
		}
		induceSA_left(SA);
		induceSA_right(SA);

	}

	private void findStartOfBuckets() {
		initBuckets();
		// compute the start of the buckets
		int sum = 0;
		for (int i = 0; i <= K; ++i) {
			sum += bucket[i];
			bucket[i] = sum - bucket[i];
		}
	}

	private void findEndOfBuckets() {
		initBuckets();
		// compute the end of the buckets
		int sum = 0;
		for (int i = 0; i <= K; ++i) {
			sum += bucket[i];
			bucket[i] = sum;
		}
	}

	private void initBuckets() {
		// initialize buckets
		for (int i = 0; i <= K; ++i) {
			bucket[i] = 0;
		}
		// compute the size of each bucket
		for (int i = 0; i < N; ++i) {
			bucket[input.get(i)]++;
		}
	}

	boolean isLMS(int pos) {
		return pos > 0 && typeLS.get(pos) && !typeLS.get(pos - 1);
	}

	private void induceSA_left(int[] SA) {
		findStartOfBuckets();
		int j;
		for (int i = 0; i < N; ++i) {
			j = SA[i] - 1;
			if (j >= 0 && !typeLS.get(j))
				SA[bucket[input.get(j)]++] = j;
		}
	}

	private void induceSA_right(int[] SA) {
		findEndOfBuckets();
		int j;
		for (int i = N - 1; i >= 0; --i) {
			j = SA[i] - 1;
			if (j >= 0 && typeLS.get(j))
				SA[--bucket[input.get(j)]] = j;
		}
	}

}
