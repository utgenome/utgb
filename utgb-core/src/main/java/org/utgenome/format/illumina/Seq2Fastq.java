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
// Seq2Fastq.java
// Since: Jun 14, 2010
//
//--------------------------------------
package org.utgenome.format.illumina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.format.fastq.FastqRead;
import org.xerial.util.log.Logger;

/**
 * For converting Illumina's input into Fastq format
 * 
 * @author leo
 * 
 */
public class Seq2Fastq {

	private static Logger _logger = Logger.getLogger(Seq2Fastq.class);

	public static FastqRead convertToFastq(String line) throws UTGBException {
		if (line == null)
			return null;

		String[] c = line.split(":");
		if (c.length < 7) {
			throw new UTGBException(UTGBErrorCode.PARSE_ERROR, "insufficient number of columns: " + line);
		}

		// name, lane, x, y, pair? 
		String readName = String.format("%s_%s_%s_%s_%s", sanitizeReadName(c[0]), c[1], c[2], c[3], sanitizeReadName(c[4]));
		String seq = c[5];
		String qual = c[6];
		StringBuilder phreadQualityString = new StringBuilder();
		for (int i = 0; i < qual.length(); ++i) {
			int phreadQual = qual.charAt(i) - 64;
			char phreadQualChar = (char) (phreadQual + 33);
			phreadQualityString.append(phreadQualChar);
		}

		return new FastqRead(readName, seq, phreadQualityString.toString());
	}

	public static String sanitizeReadName(String name) {
		return name.replaceAll("[^A-Za-z0-9_.:-]", "_");
	}

	public static void convert(BufferedReader illuminaSequenceFile, Writer output) throws IOException {

		int lineCount = 1;
		for (String line; (line = illuminaSequenceFile.readLine()) != null; lineCount++) {
			try {
				FastqRead r = convertToFastq(line);
				output.write(r.toFASTQString());
			}
			catch (UTGBException e) {
				_logger.warn(String.format("line %d: %s", lineCount, e));
			}

		}

	}

}
