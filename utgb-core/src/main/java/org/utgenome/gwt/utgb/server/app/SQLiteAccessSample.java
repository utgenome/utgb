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
// SQLiteAccessSample.java
// Since: Oct 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBException;
import org.utgenome.gwt.utgb.server.RequestHandlerBase;
import org.utgenome.gwt.utgb.server.UTGBMaster;
import org.xerial.db.DBException;
import org.xerial.db.sql.DatabaseAccess;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * Sample class to access SQLite database
 * 
 * @author leo
 * 
 */
public class SQLiteAccessSample extends RequestHandlerBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger _logger = Logger.getLogger(SQLiteAccessSample.class);

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<String> speciesList = new ArrayList<String>();
		try {
			// this will load database file specified in config/development.silk file 
			DatabaseAccess sqlite = UTGBMaster.getDatabaseAccess("legacy-track");
			List<String> queryResult = sqlite.singleColumnQuery("select distinct species from tracks order by species", "species", String.class);
			_logger.debug("species: " + queryResult);
			speciesList.addAll(queryResult);

			response.getWriter().println(StringUtil.join(queryResult, ", "));
		}
		catch (DBException e) {
			_logger.error(e);
		}
		catch (UTGBException e) {
			_logger.error(e);
		}

	}

}
