//--------------------------------------
//
// ReadView.java
// Since: 2009/04/27
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.SAMRecord.SAMTagAndValue;
import net.sf.samtools.util.CloseableIterator;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.Read;
import org.utgenome.gwt.utgb.client.bio.SAMRead;
import org.utgenome.gwt.utgb.client.bio.Read.ReadType;
import org.utgenome.gwt.utgb.client.util.Properties;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.silk.SilkWriter;
import org.xerial.util.log.Logger;

/**
 * Web action for querying data in a specified window in a genome
 * 
 */
public class ReadView extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(ReadView.class);

	public ReadView() {
	}

	public int start = -1;
	public int end = -1;
	public String species;
	public String ref;
	public String chr;
	public int width = 700;

	// resource ID
	public String dbID;
	public Read.ReadType type = ReadType.SAM;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// validating input
		if (start == -1 || end == -1 || ref == null || chr == null)
			return;

		List<SAMRead> readList = overlapQuery(dbID, new ChrLoc(chr, start, end));

		response.setContentType("text/html");

		// output result in Silk format
		SilkWriter w = new SilkWriter(response.getWriter());
		w.preamble();
		w.toSilk(readList);
		w.endDocument();
	}

	public static List<SAMRead> overlapQuery(String dbID, ChrLoc loc) {

		ArrayList<SAMRead> result = new ArrayList<SAMRead>();

		// TODO dbID to actual files
		// TODO switch SAM/BAM (with index) format
		SAMFileReader sam = new SAMFileReader(new File(WebTrackBase.getProjectRootPath(), dbID));
		for (CloseableIterator<SAMRecord> it = sam.queryOverlapping(loc.chr, loc.start, loc.end); it.hasNext();) {
			SAMRead r = convertToSAMRead(it.next());
			result.add(r);
		}
		return result;
	}

	/**
	 * convert a SAMRecord into a SAMRead, which can be used in GWT code.
	 * 
	 * @param record
	 * @return
	 */
	public static SAMRead convertToSAMRead(SAMRecord record) {
		SAMRead read = new SAMRead();
		if (record != null) {
			read.qname = record.getReadName();
			read.flag = record.getFlags();
			read.rname = record.getReferenceName();
			read.start = record.getAlignmentStart();
			read.end = record.getAlignmentEnd();
			read.mapq = record.getMappingQuality();
			read.cigar = record.getCigarString();
			read.mrnm = record.getMateReferenceName();
			read.iSize = record.getInferredInsertSize();
			read.seq = record.getReadString();
			read.qual = record.getBaseQualityString();
			read.tag = new Properties();
			for (SAMTagAndValue tag : record.getAttributes()) {
				read.tag.add(tag.tag, String.valueOf(tag.value));
			}
		}

		return read;
	}

}
