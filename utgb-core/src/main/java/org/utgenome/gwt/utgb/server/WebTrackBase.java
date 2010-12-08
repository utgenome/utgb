/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// WebTrackBase.java
// Since: Jan 22, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.xerial.core.XerialException;
import org.xerial.db.DBException;
import org.xerial.db.sql.DatabaseAccess;
import org.xerial.db.sql.SQLExpression;
import org.xerial.lens.JSONLens;
import org.xerial.util.FileResource;
import org.xerial.util.ObjectHandler;
import org.xerial.util.ObjectHandlerBase;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * @author leo
 * 
 */
public abstract class WebTrackBase extends RequestHandlerBase {

	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(WebTrackBase.class);

	public static String createSQLStatement(String sqlTemplate, Object... args) throws UTGBException {
		try {
			return SQLExpression.fillTemplate(sqlTemplate, args);
		}
		catch (DBException e) {
			throw new UTGBException(UTGBErrorCode.MaliciousSQLSyntax, e);
		}
	}

	/**
	 * Retrieves the database access
	 * 
	 * @param name
	 * @return
	 * @throws UTGBException
	 */
	public DatabaseAccess getDatabaseAccess(String databaseID) throws UTGBException {
		return UTGBMaster.getDatabaseAccess(databaseID);
	}

	/**
	 * Get the property written in the track-config.xml file
	 * 
	 * @param key
	 * @return
	 */
	public String getTrackConfigProperty(String key, String defaultValue) {
		return UTGBMaster.getUTGBConfig().getProperty(key, defaultValue);
	}

	/**
	 * 
	 * @param <T>
	 * @param databaseID
	 * @param sql
	 * @param classType
	 * @throws UTGBException
	 * @throws IOException
	 */
	public <T> void toJSON(String databaseID, String sql, Class<T> resultClass, HttpServletResponse response) throws UTGBException, IOException {
		DatabaseAccess db;
		try {
			db = getDatabaseAccess(databaseID);
			db.toJSON(sql, resultClass, response.getWriter());
		}
		catch (DBException e) {
			throw new UTGBException(UTGBErrorCode.DatabaseError, e);
		}
	}

	/**
	 * Load JSON data from the stream and convert the JSON data into Object of the given resultClass. Each object is
	 * passed to the result handler.
	 * 
	 * @param <T>
	 * @param jsonStream
	 * @param resultClass
	 * @param resultHandler
	 * @throws UTGBException
	 * @throws IOException
	 */
	public <T> void loadJSON(Reader jsonStream, Class<T> resultClass, ObjectHandler<T> resultHandler) throws UTGBException, IOException {
		try {
			JSONLens.loadJSON(resultClass, jsonStream, resultHandler);
		}
		catch (XerialException e) {
			throw new UTGBException(UTGBErrorCode.JSONToObjectMapping, e);
		}
	}

	private static class JSONAccumulator<T> extends ObjectHandlerBase<T> {
		private ArrayList<T> result = new ArrayList<T>();

		public void handle(T bean) throws Exception {
			result.add(bean);
		}

		public ArrayList<T> getResult() {
			return result;
		}

	}

	/**
	 * load the json data, and converts them into a list of result class instances
	 * 
	 * @param <T>
	 * @param jsonStream
	 * @param resultClass
	 * @return
	 * @throws UTGBException
	 * @throws IOException
	 */
	public <T> List<T> loadJSON(Reader jsonStream, Class<T> resultClass) throws UTGBException, IOException {
		try {
			JSONAccumulator<T> ja = new JSONAccumulator<T>();
			JSONLens.loadJSON(resultClass, jsonStream, ja);
			return ja.getResult();
		}
		catch (XerialException e) {
			throw new UTGBException(UTGBErrorCode.JSONToObjectMapping, e);
		}

	}

	/**
	 * Opens a BufferedReader that reads data from another action
	 * 
	 * @param request
	 * @param path
	 *            path to the another action. e.g. "hello"
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public BufferedReader openAction(HttpServletRequest request, String path) throws ServletException, IOException {
		return openAction(request, path, false);
	}

	/**
	 * @param request
	 * @param path
	 * @param bypassRequestParameter
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public BufferedReader openAction(HttpServletRequest request, String path, boolean bypassRequestParameter) throws ServletException, IOException {
		if (bypassRequestParameter) {
			path = path + "?" + getHTTPRequestQueryString(request);
		}

		return UTGBMaster.openServletReader(request, path);
	}

	@SuppressWarnings("unchecked")
	public static String getHTTPRequestQueryString(HttpServletRequest request) {
		ArrayList<String> argList = new ArrayList<String>();

		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String param = e.nextElement();
			argList.add(param + "=" + request.getParameter(param));
		}
		return StringUtil.join(argList, "&");
	}

	/**
	 * Create an SQL from a given file (relative to the caller class) and variable assignments.
	 * 
	 * @param sqlFileName
	 *            an SQL (text) file name, which contains a single sql expression. This SQL file can contain variables
	 *            $1, $2, ..., where each of them corresponds to the args of this method
	 * @param args
	 *            values each of them are assigned to the corresponding variable in the SQL
	 * @return the generated SQL
	 * @throws IOException
	 *             when failed to read the file
	 * @throws UTGBException
	 *             {@link UTGBErrorCode#InvalidSQLSyntax}
	 */
	public String createSQLFromFile(String sqlFileName, Object... args) throws IOException, UTGBException {
		BufferedReader reader = FileResource.open(this.getClass(), sqlFileName);
		if (reader == null) {
			throw new UTGBException(UTGBErrorCode.FileNotFound, sqlFileName);
		}
		StringBuilder sql = new StringBuilder();
		for (String line; (line = reader.readLine()) != null;) {
			sql.append(line);
			sql.append("\n");
		}

		try {
			return SQLExpression.fillTemplate(sql.toString(), args);
		}
		catch (DBException e) {
			throw new UTGBException(UTGBErrorCode.InvalidSQLSyntax, sql.toString());
		}
	}

	/**
	 * Create an SQL from a given template and variable assignments.
	 * 
	 * @param sqlTemplate
	 *            an SQL template, which can contain variables $1, $2, ..., each of them corresponds to the args of this
	 *            method
	 * @param args
	 *            variable assignments
	 * @return the generated SQL
	 * @throws UTGBException
	 *             {@link UTGBErrorCode#InvalidSQLSyntax}
	 */
	public String createSQL(String sqlTemplate, Object... args) throws UTGBException {
		try {
			return SQLExpression.fillTemplate(sqlTemplate, args);
		}
		catch (DBException e) {
			throw new UTGBException(UTGBErrorCode.InvalidSQLSyntax, sqlTemplate);
		}
	}

	/**
	 * Returns the path to the project root folder
	 * 
	 * @return
	 */
	public static String getProjectRootPath() {
		Object value = UTGBMaster.getVariable("projectRoot");
		return (value != null) ? value.toString() : "";
	}

	/**
	 * Return the action suffix attached to the request
	 * 
	 * @param request
	 * @return the action suffix. if no suffix is specified in the action, return "".
	 */
	public String getActionSuffix(HttpServletRequest request) {
		String suffix = (String) request.getAttribute("actionSuffix");
		return suffix == null ? "" : suffix;
	}

	/**
	 * Return the action prefix of the request
	 * 
	 * @param request
	 * @return
	 */
	public String getActionPrefix(HttpServletRequest request) {
		String prefix = (String) request.getAttribute("actionPrefix");
		return prefix == null ? "" : prefix;
	}

}
