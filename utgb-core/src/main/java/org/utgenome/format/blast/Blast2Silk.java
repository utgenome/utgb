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
// Blast2Silk.java
// Since: 2010/09/02
//
//--------------------------------------
package org.utgenome.format.blast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.utgenome.format.FormatConversionReader;
import org.utgenome.gwt.utgb.client.bio.ReadList;
import org.xerial.silk.SilkWriter;
import org.xerial.util.ArrayDeque;

/**
 * Converting blast (default) format into Silk
 * 
 * @author leo
 * 
 */
public class Blast2Silk extends FormatConversionReader {

	public Blast2Silk(Reader reader) throws IOException {
		super(reader, new Blast2SilkConverter());
	}

	public static class BlastResult {
		public String header;
		public String reference;
		public String queryName;
		public long queryLetters;
		public String databaseName;

		public List<BlastAlignment> alignment = new ArrayList<BlastAlignment>();

		public static class BlastAlignment {

			public String targetName;
			public long targetLength;
			public int bitScore;
			public float eValue;
			public int matchLength;
			public int alignmentLength;

			public static enum Strand {
				Plus, Minus
			}

			public Strand queryStrand = Strand.Plus;
			public Strand targetStrand = Strand.Plus;

		}

	}

	public static class Blast2SilkConverter extends FormatConversionReader.PipeConsumer {

		Pattern queryLine = Pattern.compile("^Query=\\s*(\\S+)");
		Pattern qlenLine = Pattern.compile("^\\s+\\((\\S+)\\s+letters\\)");
		Pattern refNameLine = Pattern.compile("^>(\\S+)");
		Pattern scoreLine = Pattern.compile("Score\\s+=\\s+(\\S+)\\s+bits.+Expect(\\(\\d+\\))?\\s+=\\s+(\\S+)");
		Pattern identitiesLine = Pattern.compile("Identities\\s+=\\s+(\\S)/(\\S)\\s+\\((\\S+%)\\)");
		Pattern strandLine = Pattern.compile("Strand\\s+=\\s+(\\S+)\\s+/\\s+(\\S+)");
		Pattern queryAlignmentLine = Pattern.compile("(^Query:\\s(\\d+)\\s*)(\\S+)\\s(\\d+)");

		private PeekableReader reader;

		private void parseBlastEntry() throws IOException {

			ReadList block = new ReadList();
			int queryLength = -1;
			String rname = null;

			for (String line; (line = reader.peekNextLine()) != null; reader.readLine()) {
				Matcher m;
				if ((m = queryLine.matcher(line)).find()) {
					String querySequenceName = m.group(1);
					block.setName(querySequenceName);
					reader.readLine();
				}
				else if ((m = qlenLine.matcher(line)).find()) {
					queryLength = Integer.parseInt(m.group(1).replaceAll(",", ""));
				}
				else if ((m = refNameLine.matcher(line)).find()) {
					rname = m.group(1);
				}
				else if ((m = scoreLine.matcher(line)).find()) {
					//fragment.bitScore = (int) (Float.parseFloat(m.group(1)) + 0.5f);
					//fragment.eValue = m.group(2);
				}
				else if ((m = identitiesLine.matcher(line)).find()) {
					//fragment.identity = Integer.parseInt(m.group(1));
					//fragment.matchLength = Integer.parseInt(m.group(1));
				}
				else if ((m = strandLine.matcher(line)).find()) {
					//fragment.setStrand("Minus".equals(m.group(1)) ? "-" : "+");
				}
				else if ((m = queryAlignmentLine.matcher(line)).find()) {
					int qStart = Integer.parseInt(m.group(2));
					String qSeq = m.group(3);
					int qEnd = Integer.parseInt(m.group(4));

					int diffStringPos = m.group(1).length();
					line = reader.readLine();
					if (line == null)
						continue;
					else {
						// read diff string
						String diff = line.substring(diffStringPos);
						for (int i = 0; i < diff.length(); ++i) {

						}
					}

				}
			}

		}

		@Override
		public void consume(Reader in, Writer out) throws Exception {

			reader = new PeekableReader(new BufferedReader(in));
			SilkWriter silk = new SilkWriter(out);

			ReadList block = null;
			BlastResult fragment = null;
			int queryLength = -1;
			String rname;

			for (String line; (line = reader.peekNextLine()) != null;) {

				if (line.startsWith("BLAST")) {
					reader.readLine();
					parseBlastEntry();
				}

			}

		}
	}

	public static class PeekableReader extends BufferedReader {

		private ArrayDeque<String> lineBuffer = new ArrayDeque<String>();
		private boolean hasNoMoreLine = false;

		public PeekableReader(BufferedReader out) {
			super(out);
		}

		private void fillQueue() throws IOException {
			if (!lineBuffer.isEmpty())
				return;

			String line = super.readLine();
			if (line == null)
				hasNoMoreLine = true;
			else
				lineBuffer.push(line);
		}

		public String peekNextLine() throws IOException {
			if (!lineBuffer.isEmpty())
				return lineBuffer.peekFirst();
			else {
				if (hasNoMoreLine)
					return null;
				fillQueue();
				return peekNextLine();
			}
		}

		@Override
		public String readLine() throws IOException {
			if (!lineBuffer.isEmpty())
				return lineBuffer.pollFirst();
			else {
				if (hasNoMoreLine)
					return null;
				fillQueue();
				return readLine();
			}
		};
	}

}
