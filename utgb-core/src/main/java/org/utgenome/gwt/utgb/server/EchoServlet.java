/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// UTGBMedaka Project
//
// EchoServlet.java
// Since: Aug 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

public class EchoServlet extends HttpServlet {

	static Logger _logger = Logger.getLogger(EchoServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String action = req.getParameter("action");
		if (action == null)
			return;

		PrintWriter writer = resp.getWriter();

		if (action.equals("saveView")) {
			String viewXML = req.getParameter("view");
			resp.setContentType("application/octet-stream");
			resp.addHeader("Content-disposition", "attachment; filename=\"view.xml\"");
			writer.append(viewXML);
			writer.flush();
		}
		else if (action.equals("upload") && ServletFileUpload.isMultipartContent(req)) {
			resp.setContentType("text/plain");
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload();

			// Parse the request
			try {
				for (FileItemIterator it = upload.getItemIterator(req); it.hasNext();) {
					FileItemStream fs = it.next();
					if (fs.getFieldName().equals("file")) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(fs.openStream()));
						StringWriter buf = new StringWriter();
						String line;
						while ((line = reader.readLine()) != null) {
							buf.append(line);
							buf.append(StringUtil.newline());
						}
						buf.flush();

						// save the xml data into the session
						HttpSession session = req.getSession();
						_logger.debug("session ID=" + session.getId());
						session.setAttribute("view", buf.toString());
					}
				}
			}
			catch (FileUploadException e) {
				_logger.error(e);
			}
		}

	}

}
