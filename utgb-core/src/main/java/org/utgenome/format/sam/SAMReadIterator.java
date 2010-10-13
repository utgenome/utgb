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
// SAMReadIterator.java
// Since: 2010/10/13
//
//--------------------------------------
package org.utgenome.format.sam;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecordIterator;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.SAMRead;

/**
 * Iterator for SAM entries
 * 
 * @author leo
 * 
 */
public class SAMReadIterator implements Iterator<SAMRead> {

	private final SAMFileReader sam;
	private SAMRecordIterator it;

	public SAMReadIterator(File bamFile, String chr) {
		this(bamFile, chr, 0, 0);
	}

	public SAMReadIterator(File bamFile, String chr, int start, int end) {
		sam = new SAMFileReader(bamFile, SAMReader.getBamIndexFile(bamFile));
		it = sam.queryOverlapping(chr, start, end);
	}

	public boolean hasNext() {
		if (it == null)
			return false;

		boolean hasNext = it.hasNext();
		if (hasNext == false) {
			it.close();
			sam.close();
			it = null;
		}
		return hasNext;
	}

	public SAMRead next() {
		SAMRecord r = (it != null) ? it.next() : null;
		if (r == null)
			throw new NoSuchElementException();

		return SAM2SilkReader.convertToSAMRead(r);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove");
	}

	public static Iterator<SAMRead> getIteratorOnChr(File bamFile, String chr) {
		return new SAMReadIterator(bamFile, chr);
	}

	public static Iterator<SAMRead> getOverlappingIterator(File bamFile, ChrLoc loc) {
		return new SAMReadIterator(bamFile, loc.chr, loc.start, loc.end);
	}

}
