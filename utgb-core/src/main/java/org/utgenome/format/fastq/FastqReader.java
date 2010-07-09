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
// FastqReader.java
// Since: Jul 2, 2010
//
//--------------------------------------
package org.utgenome.format.fastq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.utgenome.UTGBException;

/**
 * FASTQ format file reader.
 * 
 * 
 * <h1>FASTQ Format Specification: http://maq.sourceforge.net/fastq.shtml</h1>
 * 
 * <h2>Introduction</h2>
 * 
 * <p>
 * FASTQ format stores sequences and Phred qualities in a single file. It is concise and compact. FASTQ is first widely
 * used in the Sanger Institute and therefore we usually take the Sanger specification and the standard FASTQ format, or
 * simply FASTQ format. Although Solexa/Illumina read file looks pretty much like FASTQ, they are different in that the
 * qualities are scaled differently. In the quality string, if you can see a character with its ASCII code higher than
 * 90, probably your file is in the Solexa/Illumina format.
 * </p>
 * 
 * <pre>
 * Example
 * 
 * @EAS54_6_R1_2_1_413_324
 * CCCTTCTTGTCTTCAGCGTTTCTCC
 * +
 * ;;3;;;;;;;;;;;;7;;;;;;;88
 * @EAS54_6_R1_2_1_540_792
 * TTGGCAGGCCAAGGCCGATGGATCA
 * +
 * ;;;;;;;;;;;7;;;;;-;;;3;83
 * @EAS54_6_R1_2_1_443_348
 * GTTGCTTCTGGCGTGGGTGGGGGGG
 * +EAS54_6_R1_2_1_443_348
 * ;;;;;;;;;;;9;7;;.7;393333
 * </pre>
 * 
 * <h2>FASTQ Format Specification</h2>
 * 
 * <h3>Notations</h3>
 * 
 * <pre>
 * <fastq>, <blocks> and so on represents non-terminal symbols.
 * Characters in red are regex-like operators.
 * '\n' stands for the Return key.
 * Syntax
 * 
 * <fastq>	:=	<block>+
 * <block>	:=	@<seqname>\n<seq>\n+[<seqname>]\n<qual>\n
 * <seqname>	:=	[A-Za-z0-9_.:-]+
 * <seq>	:=	[A-Za-z\n\.~]+
 * <qual>	:=	[!-~\n]+
 * </pre>
 * 
 * <h2>Requirements</h2>
 * 
 * The <seqname> following '+' is optional, but if it appears right after '+', it should be identical to the <seqname>
 * following '@'. The length of <seq> is identical the length of <qual>. Each character in <qual> represents the phred
 * quality of the corresponding nucleotide in <seq>. If the Phred quality is $Q, which is a non-negative integer, the
 * corresponding quality character can be calculated with the following Perl code: $q = chr(($Q<=93? $Q : 93) + 33);
 * where chr() is the Perl function to convert an integer to a character based on the ASCII table. Conversely, given a
 * character $q, the corresponding Phred quality can be calculated with: $Q = ord($q) - 33; where ord() gives the ASCII
 * code of a character. Solexa/Illumina Read Format
 * 
 * The syntax of Solexa/Illumina read format is almost identical to the FASTQ format, but the qualities are scaled
 * differently. Given a character $sq, the following Perl code gives the Phred quality $Q:
 * 
 * $Q = 10 * log(1 + 10 ** (ord($sq) - 64) / 10.0)) / log(10);
 * 
 * @author leo
 * 
 */
public class FastqReader {

	BufferedReader reader;

	public FastqReader(Reader input) {
		if (!BufferedReader.class.isInstance(input))
			this.reader = new BufferedReader(input);
		else
			this.reader = BufferedReader.class.cast(input);
	}

	/**
	 * Read the next FASTQ read entry.
	 * 
	 * @return FastqRead entry or null if the end of stream has reached
	 * @throws UTGBException
	 */
	public FastqRead next() throws UTGBException {
		return FastqRead.parse(reader);
	}

	public void close() throws IOException {
		reader.close();
	}

}
