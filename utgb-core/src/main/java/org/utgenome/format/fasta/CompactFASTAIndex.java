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
// CompactFASTAIndex.java
// Since: 2010/03/11
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.CytoBand;
import org.xerial.core.XerialException;
import org.xerial.lens.Lens;
import org.xerial.lens.ObjectHandler;

public class CompactFASTAIndex {

	/**
	 * sequence name (e.g., chr1, scaffold2)
	 */
	public String name;
	/**
	 * Full description written in FASTA file ( > chr1 ... ), without '>' indicator
	 */
	public String description;

	public long offset;
	public int length;

	private static class IndexHolder implements ObjectHandler<CompactFASTAIndex> {

		ArrayList<CompactFASTAIndex> index = new ArrayList<CompactFASTAIndex>();

		public void handle(CompactFASTAIndex input) throws Exception {
			index.add(input);
		}

	}

	public static List<CompactFASTAIndex> load(URL indexFile) throws XerialException, IOException {
		Reader in = new BufferedReader(new InputStreamReader(indexFile.openStream()));
		return load(in);
	}

	public static List<CompactFASTAIndex> load(Reader indexFile) throws XerialException, IOException {
		IndexHolder holder = new IndexHolder();
		Lens.findFromSilk(indexFile, "sequence", CompactFASTAIndex.class, holder);

		return holder.index;
	}

	public CytoBand toCytoBand() {
		return new CytoBand(name, 1, length, "", null);
	}

}
