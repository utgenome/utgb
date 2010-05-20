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
// KeywordAliasReader.java
// Since: May 20, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.keyword;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.utgenome.format.keyword.GenomeKeywordEntry.KeywordAlias;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * keyword alias file is a tab-delimited file, each line of which has a keyword and its aliases.
 * 
 * <pre>
 * # comment line
 * (keyword1)	(aliases... )
 * (keyword2)	(aliases... )
 * ...
 * </pre>
 * 
 * @author leo
 * 
 */
public class KeywordAliasReader {

	private static Logger _logger = Logger.getLogger(KeywordAliasReader.class);

	private BufferedReader reader;
	private int lineCount = 0;

	public KeywordAliasReader(String file) throws FileNotFoundException {
		this(new FileReader(file));
	}

	public KeywordAliasReader(Reader in) {
		this.reader = new BufferedReader(in);
	}

	/**
	 * read a next keyword and its aliases
	 * 
	 * @return KeywordAlias or null if no further entry exists
	 * @throws IOException
	 */
	public KeywordAlias next() throws IOException {
		String line = reader.readLine();
		lineCount++;
		if (line == null)
			return null;

		if (line.startsWith("#"))
			return next();

		String[] split = line.split("[\t ]+");
		if (split.length < 2) {
			_logger.warn(String.format("line %d has no alias: %s", lineCount, line));
			return next();
		}
		else {
			ArrayList<String> keywords = new ArrayList<String>();
			for (int i = 1; i < split.length; ++i) {
				keywords.add(split[i]);
			}
			return new KeywordAlias(split[0], StringUtil.join(keywords, " "));
		}

	}

}
