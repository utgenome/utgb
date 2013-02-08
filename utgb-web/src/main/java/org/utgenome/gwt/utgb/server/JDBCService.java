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
// JDBCService.java
// Since: Jan 16, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.File;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.utgenome.config.UTGBConfig;
import org.utgenome.config.UTGBConfig.Database;
import org.xerial.db.DBException;
import org.xerial.db.sql.ConnectionPoolImpl;
import org.xerial.db.sql.DatabaseAccess;
import org.xerial.db.sql.DatabaseAccessBase;
import org.xerial.db.sql.h2.H2Access;
import org.xerial.db.sql.mysql.MySQLAccess;
import org.xerial.db.sql.postgres.PostgresAccess;
import org.xerial.db.sql.sqlite.SQLiteAccess;

/**
 * A support class for acquiring JDBC connections.
 * 
 * @author leo
 * 
 */
public class JDBCService {
	public static DatabaseAccess getDatabaseAccess(UTGBConfig.Database db) throws UTGBException {
		return getDatabaseAccess(new File(".").getAbsolutePath(), db);
	}

	public static DatabaseAccess getDatabaseAccess(String projectRootFolder, UTGBConfig.Database db) throws UTGBException {
		try {
			if (db.dbms.equals("sqlite")) {
				if (db.address.startsWith("/"))
					return new SQLiteAccess(db.address);
				else if (db.address.startsWith("file://"))
					return new SQLiteAccess(db.address.substring("file://".length()));
				else
					return new SQLiteAccess(projectRootFolder + "/" + db.address);
			}
			else if (db.dbms.equals("postgres")) {
				return new PostgresAccess(db.address, db.user, db.pass);
			}
			else if (db.dbms.equals("mysql")) {
				return new MySQLAccess(db.address, db.user, db.pass);
			}
			else if (db.dbms.equals("h2")) {
				return new H2Access(db.address);
			}
			else if (db.equals("hsqldb")) {
				return new HSQLDBAccess(db.address, db.user, db.pass);
			}
			else
				return new GenericDBAccess(db);

		}
		catch (DBException e) {
			throw new UTGBException(UTGBErrorCode.DatabaseError, e);
		}

	}

	public static class GenericDBAccess extends DatabaseAccessBase {
		public GenericDBAccess(Database db) throws DBException {
			super(new ConnectionPoolImpl(db.driver, db.jdbcPrefix + db.address, db.user, db.pass));
		}
	}

	public static class HSQLDBAccess extends DatabaseAccessBase {
		public static final String DRIVER_NAME = "org.hsqldb.jdbcDriver";
		public static final String ADDRESS_PREFIX = "jdbc:hsqldb:";

		/**
		 * open memory database
		 * 
		 * @throws DBException
		 */
		public HSQLDBAccess() throws DBException {
			super(new ConnectionPoolImpl(DRIVER_NAME, ADDRESS_PREFIX + "."));
		}

		public HSQLDBAccess(String address) throws DBException {
			super(new ConnectionPoolImpl(DRIVER_NAME, ADDRESS_PREFIX + address));
		}

		public HSQLDBAccess(String address, String user, String pass) throws DBException {
			super(new ConnectionPoolImpl(DRIVER_NAME, ADDRESS_PREFIX + address, user, pass));
		}

	}

}
