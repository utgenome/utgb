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
// FASTA.java
// Since: Jun 4, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.utgenome.UTGBException;

public class FASTA {
	private ArrayList<FASTASequence> sequenceList = new ArrayList<FASTASequence>();

	public FASTA(InputStream fastaFormatStream) throws IOException, UTGBException {
		sequenceList = parse(fastaFormatStream);
	}

	public ArrayList<FASTASequence> getSequenceList() {
		return sequenceList;
	}

	public static String pickSequenceName(String descriptionLine) {
		int begin = 1;
		// skip leading white spaces
		for (; begin < descriptionLine.length(); ++begin) {
			char c = descriptionLine.charAt(begin);
			if (!(c == ' ' | c == '\t'))
				break;
		}
		int end = begin + 1;
		for (; end < descriptionLine.length(); ++end) {
			char c = descriptionLine.charAt(end);
			if (c == ' ' | c == '\t') {
				break;
			}
		}
		return descriptionLine.substring(begin, end);
	}

	public static ArrayList<FASTASequence> parse(InputStream fastaFormatStream) throws IOException, UTGBException {
		FASTALexer lexer = new FASTALexer(new ANTLRInputStream(fastaFormatStream));
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		FASTAParser parser = new FASTAParser(tokenStream);
		try {
			FASTAParser.fasta_return r = parser.fasta();
			CommonTreeNodeStream nodes = new CommonTreeNodeStream((CommonTree) r.getTree());
			FASTAWalker walker = new FASTAWalker(nodes);
			return walker.fasta();
		}
		catch (RecognitionException e) {
			throw new UTGBException(e);
		}
	}

	public String toString() {
		return sequenceList.toString();
	}

}
