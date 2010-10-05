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
// OverlappingKmerIterator.java
// Since: 2010/10/05
//
//--------------------------------------
package org.utgenome.util.kmer;

import org.utgenome.format.fasta.CompactACGT;

/**
 * For walking overlapping k-mer integers
 * 
 * @author leo
 * 
 */
public interface OverlappingKmerIterator {

	public CompactACGT nextKmer();

	//	private final KmerIntegerFactory kmerGen;
	//	private final CompactACGT seq;
	//	private int pos = 0;
	//
	//	public OverlappingKmerIterator(String seq, int K) throws IOException, UTGBException {
	//		this.seq = CompactACGT.createFromString(seq);
	//		this.kmerGen = new KmerIntegerFactory(K);
	//	}
	//
	//	public int nextKMer() {
	//		// TODO
	//		return -1;
	//	}

}
