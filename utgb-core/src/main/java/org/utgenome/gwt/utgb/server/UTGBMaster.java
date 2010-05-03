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
// GenomeBrowser Project
//
// UTGB.java
// Since: May 29, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.BasicConfigurator;
import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.config.UTGBConfig;
import org.utgenome.config.UTGBConfig.Database;
import org.utgenome.config.UTGBConfig.WebAction;
import org.xerial.core.XerialException;
import org.xerial.db.DBException;
import org.xerial.db.sql.DatabaseAccess;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

/**
 * UTGB Master loads the configuration files, and set up shared variables, database access, etc.
 * 
 * 
 * @author leo
 * 
 */
public class UTGBMaster implements ServletContextListener {
	private static Logger _logger = Logger.getLogger(UTGBMaster.class);
	private static UTGBMaster _instance = new UTGBMaster();

	private UTGBConfig config = null;
	private HashMap<String, Object> _glovalVariableHolder = new HashMap<String, Object>();
	private HashMap<String, DatabaseAccess> _dbAccessTable = new HashMap<String, DatabaseAccess>();

	public static UTGBMaster getInstance() {
		return _instance;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getVariable(String name) {
		return (T) _instance._glovalVariableHolder.get(name);
	}

	public static void setVariable(String name, Object value) {
		_instance._glovalVariableHolder.put(name, value);
	}

	public static UTGBConfig getUTGBConfig() {
		UTGBMaster master = getInstance();
		if (master.config != null)
			return master.config;

		try {
			master.config = loadUTGBConfig();
		}
		catch (UTGBException e) {
			_logger.error(e);
			// use the default configuration
			master.config = new UTGBConfig();
		}

		assert (master.config != null);
		return master.config;
	}

	public static String getProjectRootFolder() throws UTGBException {
		String projectRootFolder = (String) getVariable("projectRoot");
		if (projectRootFolder == null)
			throw new UTGBException("not in the project root folder, or a file 'config/common.silk' is not found");
		return projectRootFolder;
	}

	protected static UTGBConfig loadUTGBConfig() throws UTGBException {

		// load the configuration file
		String projectRootFolder = getProjectRootFolder();

		String env = (String) getVariable("environment");
		if (env == null) {
			_logger.warn("no environment (development, test, production) is specified. Use develoment.silk as a default");
			env = "development";
		}
		String configFile = String.format("config/%s.silk", env);
		;
		try {
			_logger.info(String.format("loading %s", configFile));
			UTGBConfig config = Lens.loadSilk(UTGBConfig.class, new File(projectRootFolder, configFile).toURI().toURL());
			return config;
		}
		catch (XerialException e) {
			throw new UTGBException(String.format("syntax error in %s file: %s", configFile, e.getMessage()));
		}
		catch (IOException e) {
			throw new UTGBException(String.format("failed to load %s: %s", configFile, e.getMessage()));
		}
	}

	protected static String getContextProperty(String key, String defaultValue) {
		String value = null;
		try {
			// read the environment variable from the JNDI,
			// the parmeters of which are configured in META-INF/context.xml in
			// the Tomcat server.
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			value = (String) envContext.lookup(key);
			System.setProperty(key, value);
			return value;
		}
		catch (NamingException e) {
			if (!RequestDispatcher.isGWTHostedMode()) {
				_logger.warn("error while reading an environment context: " + key + ". " + e.getMessage());
				_logger.warn("using the default value: " + defaultValue);
			}
			System.setProperty(key, defaultValue);
			return defaultValue;
		}
	}

	public static DatabaseAccess getDatabaseAccess(String databaseID) throws UTGBException {
		return getInstance().getDatabaseAccessInternal(databaseID);
	}

	protected DatabaseAccess getDatabaseAccessInternal(String databaseID) throws UTGBException {
		if (_dbAccessTable.containsKey(databaseID))
			return _dbAccessTable.get(databaseID);

		String projectRootFolder = (String) getVariable("projectRoot");
		if (projectRootFolder == null)
			throw new UTGBException("not in the project root folder, or a file 'config/track-config.xml' is not found");

		if (config == null) {
			config = getUTGBConfig();

		}

		Database dbInfo = config.getDatabase(databaseID);
		if (dbInfo == null)
			throw new UTGBException(UTGBErrorCode.DatabaseError, "no database ID " + databaseID + " was found");

		DatabaseAccess dbAccess = JDBCService.getDatabaseAccess(projectRootFolder, dbInfo);
		_dbAccessTable.put(databaseID, dbAccess);
		return dbAccess;
	}

	public void contextDestroyed(ServletContextEvent event) {

		// dispose database connections

		for (String key : _dbAccessTable.keySet()) {
			DatabaseAccess query = _dbAccessTable.get(key);
			if (query != null)
				try {
					// close JDBC connections
					query.dispose();
				}
				catch (DBException e) {
					_logger.warn(e);
				}
		}
	}

	// @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	public void contextInitialized(ServletContextEvent event) {

		// start-up codes

		// initialize log4j
		BasicConfigurator.configure();

		setVariable("query", new HashMap<String, DatabaseAccess>());
		// scan resource folder
		String configFolderName = getContextProperty("projectRoot", new File(".").getAbsolutePath());
		setVariable("projectRoot", configFolderName);
		_logger.info("project root folder: " + configFolderName);

		String environment = getContextProperty("environment", System.getProperty("utgb.env", "development"));
		setVariable("environment", environment);
		_logger.info("environment: " + environment);

		loadProject();
	}

	private void loadProject() {
		// load track config
		config = getUTGBConfig();

		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

		// load the default action package
		DefaultRequestMap.loadActionPackage(contextClassLoader, config.javaPackage + ".app", "");

		// load the imported packages

		for (WebAction each : config.webAction) {
			String actionPackage = each.javaPackage;
			String actionPrefix = each.alias;

			_logger.info(String.format("import %s (alias = %s)", actionPackage, actionPrefix));
			DefaultRequestMap.loadActionPackage(contextClassLoader, actionPackage, actionPrefix);
		}

		// search database resources specified in the track-config.xml

		// search SQLite database files
		for (Database dbInfo : config.database) {
			_logger.info("-database(" + dbInfo + ")");
		}
	}

	public static BufferedReader openServletReader(HttpServletRequest request, String path) throws ServletException, IOException {
		if (!path.startsWith("/"))
			path = "/" + path;

		String servletURL = request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath() + path;
		URL url;
		try {
			url = new URL("http://" + servletURL);
			return new BufferedReader(new InputStreamReader(url.openStream()));
		}
		catch (MalformedURLException e) {
			throw new ServletException(e);
		}
	}

}
