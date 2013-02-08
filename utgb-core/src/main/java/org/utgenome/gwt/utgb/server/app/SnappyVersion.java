//--------------------------------------
//
// SnappyVersion.java
// Since: 2011/06/27
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.snappy.Snappy;
import org.xerial.util.log.Logger;

/**
 * Web action: SnappyVersion
 * 
 */
public class SnappyVersion extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(SnappyVersion.class);

	/**
	 * Describe your web action parameters here. Public fields in this class will be set using the web request query
	 * parameters before calling handle().
	 */

	/**
	 * Predefined coordinate parameters for GenomeTrack. Uncomment the following lines if you want to receive these
	 * parameter values.
	 */
	// public String species;   /* human, mouse, etc. */
	// public String revision;  /* hg19, mm9 ... */
	// public String name;	    /* chr1, chr2, ... */
	// public int start;       /* start position on the genome */
	// public int end;         /* end position on the genome (inclusive) */ 
	// public int width;        /* track pixel width */

	/**
	 * Use dbGroup, dbName parameters to specify database contents to be accessed
	 */
	// public String dbGroup;   /* database group */
	// public String dbName;    /* database name in the group */ 

	public SnappyVersion() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// write your own code to generate an web page here. 
		response.getWriter().println("snappy version: " + Snappy.getNativeLibraryVersion());

		// Generate debug log messages. (log level: trace, debug, info, warn, error, fatal)
		// You can switch log level by specifying -Dloglevel=debug in the eclipse launch file, or 
		// use -l (log level) option in the utgb command.
		// _logger.debug("debug message");

	}

}
