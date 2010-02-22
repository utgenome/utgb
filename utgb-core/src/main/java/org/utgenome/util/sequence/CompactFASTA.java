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
// CompactFASTA.java
// Since: Feb 22, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.util.sequence;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.utgenome.format.fasta.FASTAPullParser;

/**
 * @author leo
 * 
 */
public class CompactFASTA {

	public void loadFASTA(String file) throws IOException {
		try {
			loadFASTA(new File(file).toURI().toURL());
		}
		catch (MalformedURLException e) {
			throw new IOException(e);
		}
	}

	public void loadFASTA(URL fastaFile) throws IOException {
		String fileName = fastaFile.getFile();

		// switch input stream according to the file type
		InputStream in;
		if (fileName.endsWith(".gz"))
			in = new GZIPInputStream(fastaFile.openStream());
		else
			in = fastaFile.openStream();

		BufferedReader fasta = new BufferedReader(new InputStreamReader(in));
		FASTAPullParser fastaParser = new FASTAPullParser(fasta);

		String description;
		while ((description = fastaParser.nextDescriptionLine()) != null) {
			String seq = null;
			while ((seq = fastaParser.nextSequenceLine()) != null) {

			}
		}

	}

}
