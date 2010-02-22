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
// SmithWatermanAlignment.java
// Since: Feb 22, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.util.aligner;

import org.utgenome.util.sequence.GenomeSequence;

/**
 * Simple Smith-Waterman based aligner
 * 
 * @author leo
 * 
 */
public class SmithWatermanAligner {

	public static class Alignment {
		public final String cigar;
		public final int score;
		public final String a1;
		public final int pos; // 1-based leftmost position of the clipped sequence
		public final String a2;

		public Alignment(String cigar, int score, String a1, int pos, String a2) {
			this.cigar = cigar;
			this.score = score;
			this.a1 = a1;
			this.pos = pos;
			this.a2 = a2;
		}
	}

	private enum Trace {
		NONE, DIAGONAL, LEFT, UP
	};

	public static class Config {
		public int MATCH_SCORE = 1;
		public int MISMATCH_PENALTY = 3;
		public int GAPOPEN_PENALTY = 5;
	}

	private final Config config;

	public SmithWatermanAligner() {
		this(new Config());
	}

	public SmithWatermanAligner(Config config) {
		this.config = config;
	}

	public static class StringWrapper implements GenomeSequence {

		public final String seq;

		public StringWrapper(String seq) {
			this.seq = seq;
		}

		public char charAt(int index) {
			return seq.charAt(index);
		}

		public int length() {
			return seq.length();
		}

	}

	public Alignment align(String seq1, String seq2) {
		return align(new StringWrapper(seq1), new StringWrapper(seq2));
	}

	public <Seq extends GenomeSequence> Alignment align(Seq seq1, Seq seq2) {

		final int N = seq1.length() + 1;
		final int M = seq2.length() + 1;

		// prepare the score matrix
		int score[][] = new int[N][M];
		Trace trace[][] = new Trace[N][M];

		score[0][0] = 0;
		trace[0][0] = Trace.NONE;

		// set the first row and column.  
		for (int x = 1; x < N; ++x) {
			score[x][0] = 0;
			trace[x][0] = Trace.NONE;
		}
		for (int y = 1; y < M; ++y) {
			score[0][y] = 0;
			trace[0][y] = Trace.NONE;
		}

		// maximum score and its location 
		int maxScore = 0;
		int maxX = 0;
		int maxY = 0;

		// SW loop
		for (int x = 1; x < N; ++x) {
			for (int y = 1; y < M; ++y) {
				char c1 = Character.toLowerCase(seq1.charAt(x - 1));
				char c2 = Character.toLowerCase(seq2.charAt(y - 1));

				// match(S), insertion(I) to , deletion(D) from the seq1 scores
				int S, I, D;
				if (c1 == c2)
					S = score[x - 1][y - 1] + config.MATCH_SCORE;
				else
					S = score[x - 1][y - 1] - config.MISMATCH_PENALTY;

				I = score[x][y - 1] - config.GAPOPEN_PENALTY;
				D = score[x - 1][y] - config.GAPOPEN_PENALTY;

				if (S <= 0 && I <= 0 && D <= 0) {
					score[x][y] = 0;
					trace[x][y] = Trace.NONE;
					continue;
				}

				// choose the best score
				if (S >= I) {
					if (S >= D) {
						score[x][y] = S;
						trace[x][y] = Trace.DIAGONAL;
					}
					else {
						score[x][y] = D;
						trace[x][y] = Trace.LEFT;
					}
				}
				else if (I >= D) {
					score[x][y] = I;
					trace[x][y] = Trace.UP;
				}
				else {
					score[x][y] = D;
					trace[x][y] = Trace.LEFT;
				}

				// update max score
				if (score[x][y] > maxScore) {
					maxX = x;
					maxY = y;
					maxScore = score[x][y];
				}
			}
		}

		// trace back
		StringBuilder cigar = new StringBuilder();
		StringBuilder a1 = new StringBuilder();
		StringBuilder a2 = new StringBuilder();

		int leftMostPos = 0; // for seq1 

		for (int i = M - 1; i > maxY; --i) {
			cigar.append("S");
		}
		boolean toContinue = true;
		for (int x = maxX, y = maxY; toContinue;) {
			switch (trace[x][y]) {
			case DIAGONAL:
				cigar.append("M");
				a1.append(seq1.charAt(x - 1));
				a2.append(seq2.charAt(y - 1));
				leftMostPos = x;
				x--;
				y--;
				break;
			case LEFT:
				cigar.append("D");
				a1.append("-");
				a2.append(seq2.charAt(y - 1));
				leftMostPos = x;
				x--;
				break;
			case UP:
				cigar.append("I");
				a1.append(seq1.charAt(x - 1));
				a2.append("-");
				y--;
				break;
			case NONE:
				toContinue = false;
				for (int i = y; i >= 1; --i) {
					cigar.append("S");
				}
				break;
			}
		}

		// create cigar string
		String cigarStr = cigar.reverse().toString();
		char prev = cigarStr.charAt(0);
		int count = 1;
		StringBuilder compactCigar = new StringBuilder();
		for (int i = 1; i < cigarStr.length(); ++i) {
			char c = cigarStr.charAt(i);
			if (prev == c) {
				count++;
			}
			else {
				compactCigar.append(Integer.toString(count));
				compactCigar.append(prev);

				prev = c;
				count = 1;
			}
		}
		if (count > 0) {
			compactCigar.append(Integer.toString(count));
			compactCigar.append(prev);
		}

		return new Alignment(compactCigar.toString(), maxScore, a1.reverse().toString(), leftMostPos, a2.reverse().toString());
	}

}
