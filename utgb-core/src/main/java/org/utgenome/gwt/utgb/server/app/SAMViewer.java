//--------------------------------------
//
// SAMViewer.java
// Since: Nov 26, 2009
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public long start; /* start position on the genome */
	public long end; /* end position on the genome (inclusive) */
	public int width; /* track pixel width */

	/**
	 * Use dbGroup, dbName parameters to specify database contents to be accessed
	 */
	public String dbGroup; /* database group */
	public String dbName; /* database name in the group */

	public SAMViewer() {
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// write your own code to generate an web page here. 

		// Generate debug log messages. (log level: trace, debug, info, warn, error, fatal)
		// You can switch log level by specifying -Dloglevel=debug in the eclipse launch file, or 
		// use -l (log level) option in the utgb command.
		// _logger.debug("debug message");

	}

}
