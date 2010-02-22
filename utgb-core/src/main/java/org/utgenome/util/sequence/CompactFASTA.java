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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.utgenome.format.fasta.FASTAPullParser;
import org.xerial.silk.SilkWriter;

/**
 * @author leo
 * 
 */
public class CompactFASTA {

	public static void packFASTA(String file) throws IOException {
		try {
			packFASTA(new File(file).toURI().toURL());
		}
		catch (MalformedURLException e) {
			throw new IOException(e);
		}
	}

	public static void packFASTA(URL fastaFile) throws IOException {
		String fileName = fastaFile.getPath();
		String baseName = fileName;
		// switch input stream according to the file type
		InputStream in;
		if (fileName.endsWith(".gz")) {
			in = new GZIPInputStream(fastaFile.openStream());
			baseName = fileName.replaceAll(".gz$", "");
		}
		else
			in = fastaFile.openStream();

		// output files
		String pacSeqFile = baseName + ".utgb.pac";
		String pacNSeqFile = baseName + ".utgb.npac";
		String pacIndexFile = baseName + ".utgb.pacindex";
		BufferedOutputStream pacSeqOut = new BufferedOutputStream(new FileOutputStream(pacSeqFile));
		BufferedOutputStream pacNSeqOut = new BufferedOutputStream(new FileOutputStream(pacNSeqFile));
		SilkWriter indexOut = new SilkWriter(new BufferedWriter(new FileWriter(pacIndexFile)));
		CompactACGTWriter compressor = new CompactACGTWriter(pacSeqOut, pacNSeqOut);

		BufferedReader fasta = new BufferedReader(new InputStreamReader(in));
		FASTAPullParser fastaParser = new FASTAPullParser(fasta);

		String description;
		long prevSequenceOffset = 0;
		while ((description = fastaParser.nextDescriptionLine()) != null) {

			String sequenceName = description.trim();
			sequenceName.indexOf(0);

			long offset = compressor.getSequenceLength();

			String seq = null;
			while ((seq = fastaParser.nextSequenceLine()) != null) {
				compressor.append(seq);
			}
		}

	}

}
