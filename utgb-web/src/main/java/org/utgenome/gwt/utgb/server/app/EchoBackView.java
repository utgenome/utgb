//--------------------------------------
//
// EchoBackView.java
// Since: 2008/02/12
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.config.OldViewXML;
import org.utgenome.gwt.utgb.client.view.TrackView;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.core.XerialException;
import org.xerial.lens.SilkLens;
import org.xerial.lens.XMLLens;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class EchoBackView extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(EchoBackView.class);

	private String view;
	private long time = new Date().getTime();

	public EchoBackView() {
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String timeStamp = formatter.format(new Date(time));
		String fileName = "utgb-view-" + timeStamp + ".silk";
		response.setContentType("application/octet-stream");
		response.addHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");

		try {
			// convert XML based view file into Silk
			OldViewXML viewXML = XMLLens.loadXML(OldViewXML.class, new StringReader(view));
			TrackView tv = viewXML.toTrackView();

			PrintWriter writer = response.getWriter();
			writer.append(SilkLens.toSilk(tv));
			writer.flush();
		}
		catch (XerialException e) {
			_logger.error(e);
		}
	}

	public void setView(String view) {
		this.view = view;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
