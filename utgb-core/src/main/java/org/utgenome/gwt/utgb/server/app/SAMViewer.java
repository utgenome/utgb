//--------------------------------------
//
// SAMViewer.java
// Since: Nov 26, 2009
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import net.sf.samtools.util.CloseableIterator;

import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.util.log.Logger;

/**
 * Web action: SAMViewer
 * 
 */
public class SAMViewer extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(SAMViewer.class);

	/**
	 * Describe web action parameters here. Public fields defined in this web action class will be set using the web
	 * request query parameters.
	 */

	/**
	 * Predefined coordinate parameters for GenomeTrack. Uncomment the following lines if you want to receive these
	 * parameter values described in the request URL
	 */
	public String species; /* human, mouse, etc. */
	public String revision; /* hg19, mm9 ... */
	public String name; /* chr1, chr2, ... */
	public int start; /* start position on the genome */
	public int end; /* end position on the genome (inclusive) */
	public int width; /* track pixel width */

	public SAMViewer() {
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// swap
		if (start < end) {
			int tmp = start;
			start = end;
			end = tmp;
		}

		SAMFileReader reader = new SAMFileReader(new File(""));
		CloseableIterator<SAMRecord> queryOverlapping = reader.queryOverlapping(name, start, end);

		for (; queryOverlapping.hasNext();) {
			SAMRecord read = queryOverlapping.next();

		}

	}

}
