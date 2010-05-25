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
import org.utgenome.gwt.utgb.client.bio.OnGenome;
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
		if (start == -1 || end == -1 || chr == null)
			return;

		List<OnGenome> readList = overlapQuery(dbID, new ChrLoc(chr, start, end));

		response.setContentType("text/html");

		// output the result in Silk format
		SilkWriter w = new SilkWriter(response.getWriter());
		w.preamble();
		for (OnGenome each : readList) {
			w.leafObject("read", each);
		}
		w.endDocument();
	}

	public static ReadType getDBType(String dbID) {
		// TODO properly resolve actual DB type

		if (dbID.endsWith("bam")) {
			return ReadType.BAM;
		}
		else if (dbID.endsWith("bed")) {
			return ReadType.BED;
		}
		else if (dbID.endsWith("wig")) {
			return ReadType.WIG;
		}

		return ReadType.INTERVAL;
	}

	public static List<OnGenome> overlapQuery(String dbID, ChrLoc loc) {

		ArrayList<OnGenome> result = new ArrayList<OnGenome>();

		ReadType readType = getDBType(dbID);
		switch (readType) {
		case BAM: {
			// TODO properly resolve actual file names from dbID 
			File bamFile = new File(WebTrackBase.getProjectRootPath(), dbID);
			File baiFile = new File(WebTrackBase.getProjectRootPath(), dbID + ".bai");
			SAMFileReader sam = new SAMFileReader(bamFile, baiFile);
			for (CloseableIterator<SAMRecord> it = sam.queryOverlapping(loc.chr, loc.start, loc.end); it.hasNext();) {
				SAMRead r = convertToSAMRead(it.next());
				result.add(r);
			}
		}
			break;
		case BED: {
			result.addAll(BEDViewer.query(dbID, loc));
		}
			break;
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
		SAMRead read = new SAMRead(record.getAlignmentStart(), record.getAlignmentEnd());
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
			read.tag = new Properties();
			for (SAMTagAndValue tag : record.getAttributes()) {
				read.tag.add(tag.tag, String.valueOf(tag.value));
			}
		}

		return read;
	}

}
