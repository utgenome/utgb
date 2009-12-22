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
// TrackConfiguration.java
// Since: Jan 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.utgenome.config.UTGBConfig.Database;
import org.utgenome.config.UTGBConfig.WebAction;
import org.xerial.util.log.Logger;

/**
 * This class corresponds track-config.xml
 * 
 * @author leo
 * 
 */
public class TrackConfiguration {
	private static Logger _logger = Logger.getLogger(TrackConfiguration.class);

	private String version;
	private String packageName;
	private String projectName;
	private String group;
	private ArrayList<ActionInfo> actionList = new ArrayList<ActionInfo>();
	private ArrayList<DatabaseInfo> dbInfoList = new ArrayList<DatabaseInfo>();
	private ArrayList<ImportStmt> importList = new ArrayList<ImportStmt>();
	private Properties properties = new Properties();

	public TrackConfiguration() {
	}

	public DatabaseInfo getDatabase(String databaseID) {
		for (DatabaseInfo dbInfo : dbInfoList) {
			if (dbInfo.getId().equals(databaseID))
				return dbInfo;
		}
		return null;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackage() {
		return packageName;
	}

	public void setPackage(String packageName) {
		this.packageName = packageName;
	}

	public List<ActionInfo> getActionList() {
		return actionList;
	}

	public void addAction(ActionInfo action) {
		actionList.add(action);
	}

	public List<DatabaseInfo> getDatabaseList() {
		return dbInfoList;
	}

	public void addDatabase(DatabaseInfo db) {
		dbInfoList.add(db);
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void addImport(ImportStmt importStatement) {
		this.importList.add(importStatement);
	}

	public List<ImportStmt> getImportList() {
		return this.importList;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public void putProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	public void put(String key, String value) {
		properties.setProperty(key, value);
	}

	public UTGBConfig convert() {
		UTGBConfig config = new UTGBConfig();
		config.version = version;
		config.group = group;
		config.javaPackage = packageName;
		config.projectName = projectName;
		// import statements
		for (ImportStmt each : importList) {
			WebAction ac = new WebAction();
			ac.alias = each.getAlias();
			ac.javaPackage = each.getActionPackage();
			config.webAction.add(ac);
		}
		//  database info
		for (DatabaseInfo each : dbInfoList) {
			Database db = new Database();
			db.id = each.getId();
			if (each.getConnectionList().isEmpty())
				continue;
			else if (each.getConnectionList().size() > 1) {
				_logger.warn(String.format("db connection settings (id=%s) except the first entry will be ignored: ", each.getId()));
			}

			ConnectionInfo ci = each.getConnectionList().get(0);
			db.dbms = ci.getDbms();
			db.address = ci.getAddress();
			db.user = ci.getUser();
			db.pass = ci.getPass();

			config.database.add(db);
		}

		// properties
		for (Object key : properties.keySet()) {
			config.put(key.toString(), properties.getProperty(key.toString()));
		}

		return config;

	}

}
