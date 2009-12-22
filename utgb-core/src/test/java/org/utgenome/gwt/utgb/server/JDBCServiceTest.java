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
// JDBCServiceTest.java
// Since: Jan 16, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import org.junit.Test;
import org.utgenome.config.UTGBConfig.Database;
import org.xerial.db.Relation;
import org.xerial.db.sql.DatabaseAccess;

public class JDBCServiceTest {
	@Test
	public void testConnection() throws Exception {
		Database db = new Database();
		db.dbms = "sqlite";
		db.address = "db/legacy-track.db";
		DatabaseAccess dbAccess = JDBCService.getDatabaseAccess(db);
		for (String table : dbAccess.getTableNameList()) {
			Relation rel = dbAccess.getRelation(table);
		}
	}
}
