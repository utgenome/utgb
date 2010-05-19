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
// SAM2SilkReader.java
// Since: Mar 15, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.sam;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecord.SAMTagAndValue;
import net.sf.samtools.util.CloseableIterator;

import org.apache.tools.ant.util.ReaderInputStream;
import org.utgenome.format.FormatConversionReader;
import org.xerial.silk.SilkWriter;

/**
 * Converting SAM into Silk
 * 
 * @author leo
 * 
 */
public class SAM2SilkReader extends FormatConversionReader {

	public SAM2SilkReader(InputStream input) throws IOException {
		super(input, new Converter());
	}

	public SAM2SilkReader(Reader input) throws IOException {
		this(new ReaderInputStream(input));
	}

	private static class Converter extends FormatConversionReader.PipeConsumer {

		public void consume(InputStream in, Writer out) throws Exception {
			if (out == null)
				return;

			SAMFileReader samReader = new SAMFileReader(in);

			SilkWriter w = new SilkWriter(out);
			w.preamble();
			w.preamble("schema record(qname, flag, rname, start, end, mapq, cigar, mrnm, mpos, isize, seq, qual, tag, vtype, tag*)");
			for (CloseableIterator<SAMRecord> it = samReader.iterator(); it.hasNext();) {
				SAMRecord rec = it.next();
				SilkWriter rw = w.node("record");
				rw.leaf("qname", rec.getReadName());
				rw.leaf("flag", rec.getFlags());
				rw.leaf("rname", rec.getReferenceName());
				rw.leaf("start", rec.getAlignmentStart());
				rw.leaf("end", rec.getAlignmentEnd());
				rw.leaf("mapq", rec.getMappingQuality());
				rw.leaf("cigar", rec.getCigarString());
				rw.leaf("mrname", rec.getMateReferenceName());
				rw.leaf("mpos", rec.getMateAlignmentStart());
				rw.leaf("isize", rec.getInferredInsertSize());
				rw.leaf("seq", rec.getReadString());
				rw.leaf("qual", rec.getBaseQualityString());
				SilkWriter tw = rw.node("tag");
				for (SAMTagAndValue each : rec.getAttributes()) {
					tw.leaf(each.tag, each.value);
				}
			}

		}

	}

}
