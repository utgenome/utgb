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
// WIG2Silk.java
// Since: 2009/05/07
//
//--------------------------------------
package org.utgenome.format.wig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.utgenome.UTGBException;
import org.xerial.core.XerialException;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

/**
 * @author yoshimura
 * 
 */
public class WIG2Silk {

	private static Logger _logger = Logger.getLogger(WIG2Silk.class);

	private final BufferedReader reader;

	public static class WIGHeaderDescription {
		String name;
		ArrayList<WIGHeaderAttribute> attributes = new ArrayList<WIGHeaderAttribute>();

		public void setName(String name) {
			this.name = name;
		}

		public void addAttribute(WIGHeaderAttribute attribute) {
			attributes.add(attribute);
		}

		@Override
		public String toString() {
			return String.format("name=%s, attributes=%s", name, attributes.toString());
		}
	}

	public static class WIGHeaderAttribute {
		String name;
		String value;

		public void setName(String name) {
			this.name = name;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.format("{name=%s, value=%s}", name, value);
		}
	}

	public WIG2Silk(File wigFile) throws IOException {
		this(new FileReader(wigFile));
	}

	/**
	 * 
	 * @param wigFile
	 * @throws IOException
	 */
	public WIG2Silk(Reader wigFile) throws IOException {

		// track = new WIGHeaderDescription();
		// genes = new ArrayList<String[]>();

		this.reader = new BufferedReader(wigFile);

	}

	/**
	 * 
	 * @param out
	 * @throws IOException
	 * @throws UTGBShellException
	 */

	public void toSilk(PrintWriter out) throws IOException, UTGBException {

		// print header line
		out.print("%silk(version:1.0)");
		out.flush();

		int lineNum = 1;
		for (String line; (line = reader.readLine()) != null; lineNum++) {

			try {
				if (line.startsWith("#") || line.length() == 0) {
				}
				else if (line.startsWith("browser")) {
					// this.browser = readTrackLine(line,i);
				}
				else if (line.startsWith("track")) {
					// print track line
					StringBuffer sb = new StringBuffer("\n-track(");
					sb = readHeaderLine(sb, line);
					out.println(sb.toString());
				}
				else if (line.startsWith("variableStep")) {
					StringBuffer sb = new StringBuffer(" -coordinate(stepType:variable, ");
					sb = readHeaderLine(sb, line);
					sb.append("\n  -data(start, value)|");
					out.println(sb.toString());
				}
				else if (line.startsWith("fixedStep")) {
					StringBuffer sb = new StringBuffer(" -coordinate(stepType:fixed, ");
					sb = readHeaderLine(sb, line);
					sb.append("\n  -data(value)|");
					out.println(sb.toString());
				}
				else {
					String[] lineValues = readWIGLine(line, lineNum);
					StringBuilder sb = new StringBuilder();
					for (String value : lineValues) {
						sb.append(value + "\t");
					}
					String tabData = sb.toString().trim();
					if (tabData.length() > 0)
						out.println(tabData);
				}
			}
			catch (RecognitionException e) {
				throw new UTGBException(String.format("line %d: %s", lineNum, e));
			}
			catch (XerialException e) {
				throw new UTGBException(String.format("line %d: %s", lineNum, e));
			}
			finally {
				out.flush();
			}

		}
	}

	public String toSilk() throws IOException, UTGBException {
		StringWriter out = new StringWriter();
		toSilk(new PrintWriter(out));
		return out.toString();
	}

	private static String[] readWIGLine(String line, int lineNo) {
		String[] temp = line.replace(" ", "\t").trim().split("\t+");
		// split by tab or space
		if (temp.length > 2) {
			_logger.error("Error data -> line:" + lineNo);
		}
		return temp;
	}

	private static StringBuffer readHeaderLine(StringBuffer sb, String line) throws IOException, XerialException, RecognitionException {
		WIGLexer lexer = new WIGLexer(new ANTLRReaderStream(new StringReader(line)));
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		WIGParser parser = new WIGParser(tokens);
		WIGParser.description_return ret = parser.description();

		for (WIGHeaderAttribute a : Lens.loadANTLRParseTree(WIGHeaderDescription.class, (Tree) ret.getTree(), WIGParser.tokenNames).attributes) {
			sb.append(a.name + ":");
			if ((a.value.contains(",") || a.value.contains(" ") || a.value.contains(":")) && !a.value.startsWith("\"") && !a.value.endsWith("\"")) {
				sb.append("\"" + a.value + "\", ");
			}
			else {
				sb.append(a.value + ", ");
			}
		}
		return sb.delete(sb.lastIndexOf(","), sb.length()).append(")");
	}

	private static String changeRGB2Hex(String rgb) {
		String[] temp = rgb.split(",");
		StringBuffer ret = new StringBuffer("\"#");
		if (temp.length >= 3) {
			for (int i = 0; i < 3; i++) {
				if (Integer.valueOf(temp[i]) > 255 || Integer.valueOf(temp[i]) < 0) {
					System.err.println("Warn : out of color range 0-255");
					return "";
				}
				if (Integer.toHexString(Integer.valueOf(temp[i])).length() == 1) {
					ret.append("0");
				}
				ret.append(Integer.toHexString(Integer.valueOf(temp[i])));
			}
			return ret.append("\"").toString();
		}
		else {
			return "";
		}
	}
}
