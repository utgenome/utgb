/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// UTGB Common Project
//
// FASTAPullParser.java
// Since: Jun 4, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.xerial.util.FileType;
import org.xerial.util.log.Logger;

import java.io.*;
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;
//import org.codehaus.plexus.archiver.tar.TarInputStream;
//import org.codehaus.plexus.archiver.tar.TarEntry;

/**
 * A pull parser for FASTA format files
 * 
 * @author leo
 * 
 */
public class FASTAPullParser {
	private static Logger _logger = Logger.getLogger(FASTAPullParser.class);

	private static enum TokenType {
		DescriptionLine, SequenceLine
	}

	private static class Token {
		private TokenType type;
		private String data;

		public Token(TokenType type, String data) {
			this.type = type;
			this.data = data.trim();
		}

		public TokenType getType() {
			return type;
		}

		public String getData() {
			return data;
		}
	}

	private LinkedList<Token> tokenStack = new LinkedList<Token>();
	private final FASTAReader fastaReader;
	private int lineCount = 0;

	private static int DEFAULT_BUFFER_SIZE = 4 * 1024 * 1024;

	/**
	 * Create a pull parser from a given text reader
	 * 
	 * @param reader
	 */
	public FASTAPullParser(Reader reader) {
		fastaReader = new DefaultFASTAReader(new BufferedReader(reader));
	}

//	public static FASTAPullParser newTARFileReader(InputStream tarFileInput) throws IOException {
//		return new FASTAPullParser(new TarFASTAReader(new BufferedInputStream(tarFileInput, DEFAULT_BUFFER_SIZE), DEFAULT_BUFFER_SIZE));
//	}
//
//	public static FASTAPullParser newTARGZFileReader(InputStream targzInput) throws IOException {
//		return new FASTAPullParser(new TarFASTAReader(new GZIPInputStream(new BufferedInputStream(targzInput)), DEFAULT_BUFFER_SIZE));
//	}

	public static FASTAPullParser newGZIPFileReader(InputStream gzInput) throws IOException {
		return new FASTAPullParser(new DefaultFASTAReader(new GZIPInputStream(new BufferedInputStream(gzInput)), DEFAULT_BUFFER_SIZE));
	}

	private FASTAPullParser(FASTAReader reader) {
		this.fastaReader = reader;
	}

	/**
	 * Create a pull parse for the given FASTA file. The file can be text (.fa, .fasta, etc.), tar.gz or gz format.
	 * 
	 * @param fastaFile
	 * @throws IOException
	 */
	public FASTAPullParser(File fastaFile) throws IOException {
		this(fastaFile.getName(), new FileInputStream(fastaFile), DEFAULT_BUFFER_SIZE);
	}

	public FASTAPullParser(File fastaFile, int bufferSize) throws IOException {
		this(fastaFile.getName(), new FileInputStream(fastaFile), bufferSize);
	}

	protected FASTAPullParser(String fastaFile, InputStream in, int bufferSize) throws IOException {
		final int BUFFER_SIZE = bufferSize;

		FileType fileType = FileType.getFileType(fastaFile);
		switch (fileType) {
//		case TAR:
//			fastaReader = new TarFASTAReader(new BufferedInputStream(in, BUFFER_SIZE), BUFFER_SIZE);
//			break;
//		case TAR_GZ:
//			fastaReader = new TarFASTAReader(new GZIPInputStream(new BufferedInputStream(in)), BUFFER_SIZE);
//			break;
		case GZIP:
			fastaReader = new DefaultFASTAReader(new GZIPInputStream(new BufferedInputStream(in)), BUFFER_SIZE);
			break;
		case FASTA:
		default:
			fastaReader = new DefaultFASTAReader(in, BUFFER_SIZE);
			break;
		}
	}

	private static interface FASTAReader {
		public String nextLine() throws IOException;

		public void close() throws IOException;
	}

	private static class DefaultFASTAReader implements FASTAReader {

		private BufferedReader in;

		public DefaultFASTAReader(BufferedReader r) {
			this.in = r;
		}

		public DefaultFASTAReader(InputStream in, int bufferSize) {
			this.in = new BufferedReader(new InputStreamReader(in), bufferSize);
		}

		public String nextLine() throws IOException {
			return in.readLine();
		}

		public void close() throws IOException {
			if (in != null)
				in.close();
		}
	}

//	private static class TarFASTAReader implements FASTAReader {
//
//		TarInputStream tarIn;
//		BufferedReader reader = null;
//		int bufferSize;
//
//		public TarFASTAReader(InputStream in, int bufferSize) throws IOException {
//			this.tarIn = new TarInputStream(in);
//			this.bufferSize = bufferSize;
//		}
//
//		public String nextLine() throws IOException {
//
//			if (reader != null) {
//				String line = reader.readLine();
//				if (line == null) {
//					reader = null;
//					return nextLine();
//				}
//				else
//					return line;
//			}
//
//			while (true) {
//				TarEntry currentEntry = tarIn.getNextEntry();
//				if (currentEntry == null)
//					return null;
//
//				if (currentEntry.isDirectory()) {
//					continue;
//				}
//
//				FileType fileType = FileType.getFileType(currentEntry.getName());
//				if (fileType != FileType.FASTA)
//					continue;
//
//				reader = new BufferedReader(new InputStreamReader(tarIn), bufferSize);
//				break;
//			}
//
//			return nextLine();
//		}
//
//		public void close() throws IOException {
//			if (reader != null)
//				reader.close();
//		}
//
//	}

	private boolean hasStackedToken() {
		return !tokenStack.isEmpty();
	}

	private Token popToken() {
		return tokenStack.removeLast();
	}

	private Token nextToken() throws IOException {
		if (hasStackedToken())
			return popToken();
		else {
			// read next line
			String line = fastaReader.nextLine();
			if (line == null)
				return null; // no more token
			lineCount++;
			if (line.startsWith(">"))
				return new Token(TokenType.DescriptionLine, line.substring(1));
			else
				return new Token(TokenType.SequenceLine, line);
		}
	}

	/**
	 * read the next fasta sequence;
	 * 
	 * @return the next fasta sequence, or null when there is no more sequence to read.
	 * @throws IOException
	 */
	public FASTASequence nextSequence() throws UTGBException, IOException {
		Token t = nextToken();
		if (t == null)
			return null;

		TokenType type = t.getType();
		if (type == TokenType.DescriptionLine) {
			String seq = readSequence();
			return new FASTASequence(t.getData(), seq);
		}
		else
			return null;
	}

	public String nextSequenceLine() throws IOException {
		Token t = nextToken();
		if (t == null)
			return null;

		TokenType type = t.getType();
		if (type == TokenType.SequenceLine) {
			return t.getData();
		}
		else {
			tokenStack.add(t);
			return null;
		}
	}

	public String nextDescriptionLine() throws IOException {
		Token t = nextToken();
		if (t == null)
			return null;
		if (t.getType() == TokenType.DescriptionLine) {
			return t.getData();
		}
		else {
			tokenStack.add(t);
			return null;
		}
	}

	private String readSequence() throws UTGBException, IOException {
		Token t = nextToken();
		if (t == null)
			throw new UTGBException(UTGBErrorCode.INVALID_FORMAT, "sequence is null: " + lineInfo());
		TokenType type;
		StringBuilder builder = new StringBuilder();
		while ((type = t.getType()) == TokenType.SequenceLine) {
			builder.append(t.getData());
			t = nextToken();
			if (t == null) {
				return builder.toString();
			}
		}
		tokenStack.add(t);
		return builder.toString();

	}

	private String lineInfo() {
		return "line=" + lineCount;
	}

	public void close() throws IOException {
		if (fastaReader != null)
			fastaReader.close();
	}

}
