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
// AXTLens.java
// Since: 2010/11/26
//
//--------------------------------------
package org.utgenome.format.axt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.xerial.lens.ObjectStreamHandler;
import org.xerial.lens.TextFormatLens;
import org.xerial.util.ArrayDeque;
import org.xerial.util.Deque;
import org.xerial.util.ObjectHandler;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * Lens for AXT format
 * 
 * @author leo
 * 
 */
public class AXTLens implements TextFormatLens {

	private static Logger _logger = Logger.getLogger(AXTLens.class);

	public static AXTAlignment lens(List<String> axtBlock) throws UTGBException {
		if (axtBlock.size() < 3) {
			throw new UTGBException(UTGBErrorCode.INVALID_FORMAT, "axtBlock.size() must be larger than 3: " + axtBlock.size());
		}

		AXTAlignment result = new AXTAlignment();
		parseSummaryLine(result, axtBlock.get(0));
		result.primaryAssembly = axtBlock.get(1);
		result.aligningAssembly = axtBlock.get(2);
		return result;
	}

	public static void parseSummaryLine(AXTAlignment obj, String summaryLine) throws UTGBException {
		String[] c = summaryLine.split("[\\s]+");

		if (c.length < 9)
			throw new UTGBException(UTGBErrorCode.INVALID_FORMAT, summaryLine);

		try {
			obj.num = Integer.parseInt(c[0]);
			obj.s_chr = c[1];
			obj.s_start = Integer.parseInt(c[2]);
			obj.s_end = Integer.parseInt(c[3]);
			obj.d_chr = c[4];
			obj.d_start = Integer.parseInt(c[5]);
			obj.d_end = Integer.parseInt(c[6]);
			obj.strand = c[7];
			obj.score = Integer.parseInt(c[8]);
		}
		catch (NumberFormatException e) {
			throw new UTGBException(UTGBErrorCode.INVALID_FORMAT, String.format("%s: %s", e.getMessage(), summaryLine));
		}
	}

	private static class AXTStream implements Iterable<AXTAlignment> {

		public AXTStream(BufferedReader in) {
			// TODO Auto-generated constructor stub
		}

		public Iterator<AXTAlignment> iterator() {
			return null;
		}

	}

	private static class AXTIteator implements Iterator<AXTAlignment> {

		private BufferedReader in;
		private Deque<AXTAlignment> cache = new ArrayDeque<AXTAlignment>();

		public AXTIteator(BufferedReader in) {
			this.in = in;
		}

		public boolean hasNext() {
			if (!cache.isEmpty())
				return true;

			return false;
		}

		public AXTAlignment next() {
			if (hasNext())
				return cache.pollFirst();

			return null;
		}

		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
	}

	public static Iterable<AXTAlignment> lens(BufferedReader in) throws UTGBException {
		return new AXTStream(in);
	}

	public static void lens(BufferedReader in, ObjectHandler<AXTAlignment> handler) throws UTGBException {

		try {
			handler.init();
			try {
				for (;;) {
					String line = in.readLine();
					if (line == null)
						break;

					if (StringUtil.isWhiteSpace(line))
						continue;

					List<String> axtBlock = new ArrayList<String>(3);

					String header = line;
					String seq1 = in.readLine();
					String seq2 = in.readLine();

					if (seq1 == null || seq2 == null) {
						break;
					}

					axtBlock.add(line);
					axtBlock.add(seq1);
					axtBlock.add(seq2);

					try {
						handler.handle(lens(axtBlock));
					}
					catch (Exception e) {
						_logger.error(e);
					}
				}
			}
			finally {
				handler.finish();
				if (in != null)
					in.close();
			}
		}
		catch (Exception e) {
			throw UTGBException.convert(e);
		}

	}

	public static void lens(File axtFile, ObjectHandler<AXTAlignment> handler) throws UTGBException {
		try {
			lens(new BufferedReader(new FileReader(axtFile)), handler);
		}
		catch (Exception e) {
			throw UTGBException.convert(e);
		}

	}

	private BufferedReader in;

	public AXTLens(BufferedReader in) {
		this.in = in;
	}

	public AXTLens(URL axt) throws IOException {
		in = new BufferedReader(new InputStreamReader(axt.openStream()));
	}

	public void convert(ObjectStreamHandler handler) throws Exception {
		try {
			try {
				for (;;) {
					String line = in.readLine();
					if (line == null)
						break;

					if (StringUtil.isWhiteSpace(line))
						continue;

					List<String> axtBlock = new ArrayList<String>(3);

					String header = line;
					String seq1 = in.readLine();
					String seq2 = in.readLine();

					if (seq1 == null || seq2 == null) {
						break;
					}

					axtBlock.add(line);
					axtBlock.add(seq1);
					axtBlock.add(seq2);

					try {
						AXTAlignment aln = lens(axtBlock);
						handler.add("axt", aln);
					}
					catch (Exception e) {
						_logger.error(e);
					}
				}
			}
			finally {
				if (in != null)
					in.close();
			}
		}
		catch (Exception e) {
			throw UTGBException.convert(e);
		}

	}

}
