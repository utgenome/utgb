//--------------------------------------
//
// KeywordSearch.java
// Since: May 19, 2010
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
 * Web action: KeywordSearch
 * 
 */
public class KeywordSearch extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(KeywordSearch.class);

	/**
	 * Describe your web action parameters here. Public fields in this class will be set using the web request query
	 * parameters before calling handle().
	 */
	public String ref; /* reference sequence name: hg19, mm9 ... */
	public int page = 0; // page number (0-origin)
	public int size = 10; // page size

	public KeywordSearch() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
