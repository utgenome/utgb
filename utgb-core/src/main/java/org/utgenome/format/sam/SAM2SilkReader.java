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
import java.io.StringWriter;
import java.io.Writer;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecord.SAMTagAndValue;
import net.sf.samtools.SAMRecordIterator;

import org.utgenome.format.FormatConversionReader;
import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.utgenome.gwt.utgb.client.bio.SAMReadLight;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.util.ReaderInputStream;
import org.xerial.silk.SilkWriter;
import org.xerial.util.log.Logger;

/**
 * Reader for converting SAM into Silk
 * 
 * @author leo
 * 
 */
public class SAM2SilkReader extends FormatConversionReader {

	private static Logger _logger = Logger.getLogger(SAM2SilkReader.class);

	public SAM2SilkReader(InputStream input) throws IOException {
		super(input, new Converter());
	}

	public SAM2SilkReader(Reader input) throws IOException {
		this(new ReaderInputStream(input));
	}

	private static class Converter extends FormatConversionReader.PipeConsumer {

		@Override
		public void consume(InputStream in, Writer out) throws Exception {
			if (out == null)
				return;

			SAMFileReader samReader = new SAMFileReader(in, true);

			long readCount = 1;
			SilkWriter w = new SilkWriter(out);
			w.preamble();
			w.preamble("schema record(qname, flag, rname, start, end, mapq, cigar, mrnm, mpos, isize, seq, qual, tag, vtype, tag*)");
			SAMRecordIterator it = samReader.iterator();
			try {
				for (; it.hasNext(); readCount++) {
					if (readCount % 1000000 == 0) {
						_logger.info(String.format("%,d reads processed", readCount));
					}
					SAMRecord rec = it.next();
					toSilk(rec, w);
				}
			}
			finally {
				it.close();
			}

		}

	}

	/**
	 * convert a SAMRecord into a SAMRead, which can be used in GWT code.
	 * 
	 * @param record
	 * @return
	 */
	public static SAMRead convertToSAMRead(SAMRecord record) {
		SAMRead read = new SAMRead(record.getAlignmentStart(), record.getAlignmentEnd() + 1);
		if (record != null) {
			read.qname = record.getReadName();
			read.flag = record.getFlags();
			read.rname = record.getReferenceName();
			read.mapq = record.getMappingQuality();
			read.cigar = record.getCigarString();
			read.mrnm = record.getMateReferenceName();
			read.mStart = record.getMateAlignmentStart();
			read.iSize = record.getInferredInsertSize();
			read.seq = record.getReadString();
			read.qual = record.getBaseQualityString();
			read.unclippedStart = record.getUnclippedStart();
			read.unclippedEnd = record.getUnclippedEnd() + 1;
			read.tag = new Properties();
			for (SAMTagAndValue tag : record.getAttributes()) {
				read.tag.add(tag.tag, String.valueOf(tag.value));
			}
		}

		return read;
	}

	/**
	 * convert a SAMRecord into a SAMRead, which can be used in GWT code.
	 * 
	 * @param record
	 * @return
	 */
	public static SAMReadLight convertToSAMReadLight(SAMRecord record) {
		SAMReadLight read = new SAMReadLight(record.getAlignmentStart(), record.getAlignmentEnd() + 1);
		if (record != null) {
			read.qname = record.getReadName();
			read.flag = record.getFlags();
			read.cigar = record.getCigarString();
			read.unclippedStart = record.getUnclippedStart();
			read.unclippedEnd = record.getUnclippedEnd() + 1;
			read.iSize = record.getInferredInsertSize();
		}
		return read;
	}

	/**
	 * Convert an input SAMRecord into Silk format by using a given SilkWriter
	 * 
	 * @param rec
	 * @param w
	 */
	public static void toSilk(SAMRecord rec, SilkWriter w) {
		StringWriter buf = new StringWriter();
		SilkWriter rw = w.node("record");
		rw.leaf("qname", rec.getReadName());
		rw.leaf("flag", rec.getFlags());
		rw.leaf("rname", rec.getReferenceName());
		rw.leaf("start", rec.getAlignmentStart());
		rw.leaf("end", rec.getAlignmentEnd() + 1);
		rw.leaf("unclipped start", rec.getUnclippedStart());
		rw.leaf("unclipped end", rec.getUnclippedEnd() + 1);
		rw.leaf("mapq", rec.getMappingQuality());
		rw.leaf("cigar", rec.getCigarString());
		rw.leaf("mrname", rec.getMateReferenceName());
		rw.leaf("mstart", rec.getMateAlignmentStart());
		rw.leaf("isize", rec.getInferredInsertSize());
		rw.leaf("seq", rec.getReadString());
		rw.leaf("qual", String.format("\"%s\"", rec.getBaseQualityString()));
		SilkWriter tw = rw.node("tag");
		for (SAMTagAndValue each : rec.getAttributes()) {
			tw.leaf(each.tag, each.value);
		}
	}

	/**
	 * Convert an input SAMRecord into Silk format
	 * 
	 * @param rec
	 * @return
	 */
	public static String toSilk(SAMRecord rec) {
		StringWriter buf = new StringWriter();
		SilkWriter w = new SilkWriter(buf);
		toSilk(rec, w);
		w.flush();
		return buf.toString();
	}

}
