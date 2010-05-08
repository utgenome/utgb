//--------------------------------------
//
// LoadView.java
// Since: 2008/02/12
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class LoadView extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(LoadView.class);

	public LoadView() {
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
			_logger.error("not the multipart post");
			return;
		}

		response.setContentType("text/html");
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload();

		// Parse the request
		try {
			for (FileItemIterator it = upload.getItemIterator(request); it.hasNext();) {
				FileItemStream fs = it.next();
				if (fs.getFieldName().equals("file")) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(fs.openStream()));
					StringWriter buf = new StringWriter();
					String line;

					buf.append("<body><!--");

					while ((line = reader.readLine()) != null) {
						buf.append(line);
						buf.append(StringUtil.newline());
					}
					buf.append("--></body>");
					buf.flush();

					response.getWriter().append(buf.toString());
					response.getWriter().flush();
					return;
				}
			}
		}
		catch (FileUploadException e) {
			_logger.error(e);
		}

	}

}
