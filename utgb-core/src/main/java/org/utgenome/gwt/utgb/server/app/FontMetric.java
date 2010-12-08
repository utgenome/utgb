//--------------------------------------
//
// FontMetric.java
// Since: 2009/07/15
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Font;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.utgenome.gwt.utgb.server.app.FontPanel.FontInfo;
import org.xerial.lens.JSONLens;
import org.xerial.util.Pair;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class FontMetric extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(FontMetric.class);

	public FontMetric() {
	}

	public int fontSize = 10;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Pair<FontInfo, Font> fontInfo = FontPanel.getFontInfo(fontSize);
		response.getWriter().println(JSONLens.toJSON(fontInfo.getFirst()));

	}

}
