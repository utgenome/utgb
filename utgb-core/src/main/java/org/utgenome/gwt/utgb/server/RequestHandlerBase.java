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
// utgb-core Project
//
// RequestHandlerBase.java
// Since: 2007/11/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBException;

/**
 * Abstract base implementation of the {@link RequestHandler}
 * 
 * @author leo
 * 
 */
public abstract class RequestHandlerBase extends HttpServlet implements RequestHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	public void validate(HttpServletRequest request, HttpServletResponse response) throws ServletException, UTGBException {
		// do nothing in default
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher.setRequestParametersToHandler(this, req);
		this.handle(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
