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
// FastqToBAM.java
// Since: Jul 5, 2010
//
//--------------------------------------
package org.utgenome.format.fastq;

import java.io.File;
import java.io.Reader;

import net.sf.samtools.SAMFileHeader;
import net.sf.samtools.SAMFileWriter;
import net.sf.samtools.SAMFileWriterFactory;
import net.sf.samtools.SAMReadGroupRecord;
import net.sf.samtools.SAMRecord;

import org.utgenome.UTGBException;

/**
 * Converting Illumina's FASTQ read data (single or paired-end) into BAM format, which can be used BLOAD's GATK
 * pipeline.
 * 
 * This code is migrated from
 * 
 * @author leo
 * 
 */
public class FastqToBAM {

	private String readGroupName;
	private String sampleName;
	private String readPrefix;

	private File outputFile;

	public int convert(Reader input1, Reader input2) throws UTGBException {

		FastqReader end1 = new FastqReader(input1);
		FastqReader end2 = (input2 == null) ? null : new FastqReader(input2);

		SAMReadGroupRecord srg = new SAMReadGroupRecord(readGroupName);
		srg.setSample(sampleName);

		SAMFileHeader sfh = new SAMFileHeader();
		sfh.addReadGroup(srg);
		sfh.setSortOrder(SAMFileHeader.SortOrder.queryname);

		SAMFileWriter sfw = (new SAMFileWriterFactory()).makeSAMOrBAMWriter(sfh, false, outputFile);

		int readsSeen = 0;

		FastqRead fqr1, fqr2 = null;

		while ((fqr1 = end1.next()) != null && (end2 == null || (fqr2 = end2.next()) != null)) {

			String fqr1Name = fqr1.seqname;

			SAMRecord sr1 = new SAMRecord(sfh);
			sr1.setReadName(readPrefix != null ? (readPrefix + ":" + fqr1Name) : fqr1Name);
			sr1.setReadString(fqr1.seq);
			sr1.setBaseQualityString(fqr1.qual);
			sr1.setReadUnmappedFlag(true);
			sr1.setReadPairedFlag(false);
			sr1.setAttribute("RG", readGroupName);

			SAMRecord sr2 = null;

			// paired-end read
			if (fqr2 != null) {
				sr1.setReadPairedFlag(true);
				sr1.setFirstOfPairFlag(true);
				sr1.setSecondOfPairFlag(false);
				sr1.setMateUnmappedFlag(true);

				String fqr2Name = fqr2.seqname;
				sr2 = new SAMRecord(sfh);
				sr2.setReadName(readPrefix != null ? (readPrefix + ":" + fqr2Name) : fqr2Name);
				sr2.setReadString(fqr2.seq);
				sr2.setBaseQualityString(fqr2.qual);
				sr2.setReadUnmappedFlag(true);
				sr2.setReadPairedFlag(true);
				sr2.setAttribute("RG", readGroupName);
				sr2.setFirstOfPairFlag(false);
				sr2.setSecondOfPairFlag(true);
				sr2.setMateUnmappedFlag(true);
			}

			sfw.addAlignment(sr1);
			if (fqr2 != null) {
				sfw.addAlignment(sr2);
			}
			readsSeen++;
		}

		sfw.close();
		return readsSeen;
	}
}
