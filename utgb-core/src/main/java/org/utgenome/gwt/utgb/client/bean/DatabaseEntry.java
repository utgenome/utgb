/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// DatabaseFolder.java
// Since: Nov 19, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bean;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DatabaseEntry implements IsSerializable {

	public String path = "";
	public boolean isFile = false;

	public DatabaseEntry() {
	}

	private DatabaseEntry(String dbFolder, boolean isFile) {
		this.path = dbFolder;
		this.isFile = isFile;
	}

	public static DatabaseEntry newFolder(String dbFolder) {
		return new DatabaseEntry(dbFolder == null ? "" : dbFolder, false);
	}

	public static DatabaseEntry newFile(String dbFile) {
		return new DatabaseEntry(dbFile == null ? "" : dbFile, true);
	}

	public String leaf() {
		int pos = path.lastIndexOf("/");
		if (pos == -1)
			return path;
		else
			return path.substring(pos + 1);
	}

	public DatabaseEntry parent() {
		int pos = path.lastIndexOf("/");

		if (pos == -1)
			return null;

		return newFolder(path.substring(0, pos));
	}

	public static String parent(String databaseFolder) {
		DatabaseEntry p = newFolder(databaseFolder).parent();
		if (p == null)
			return null;
		else
			return p.path;
	}

	public static String leaf(String databaseFolder) {
		return newFolder(databaseFolder).leaf();
	}

	/**
	 * Path to the specified database file in this database folder
	 * 
	 * @param dbName
	 * @return
	 */
	public String dbPath(String dbName) {
		return path + "/" + dbName;
	}

	public boolean isFile() {
		return isFile;
	}

	public boolean isFolder() {
		return !isFile;
	}

}
