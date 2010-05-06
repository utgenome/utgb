//--------------------------------------
//
// ReadView.java
// Since: 2009/04/27
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.server.WebTrackBase;
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
	public String id;

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// validating input
		if (start == -1 || end == -1 || ref == null || chr == null)
			return;

	}

	public static void overlapQuery(String dbID, ChrLoc loc) {

	}
}
