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
// UTGBConfigTest.java
// Since: Aug 28, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.config.UTGBConfig.Database;
import org.utgenome.config.UTGBConfig.WebAction;
import org.xerial.lens.JSONLens;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class UTGBConfigTest {

	private static Logger _logger = Logger.getLogger(UTGBConfigTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public void validate(UTGBConfig config) {
		assertEquals("1.0", config.version);
		assertEquals("org.utgenome", config.group);
		assertEquals("myapp", config.projectName);
		assertEquals("org.utgenome.gwt.utgb.server", config.javaPackage);
		assertEquals(1, config.webAction.size());
		WebAction wa = config.webAction.get(0);
		assertEquals("utgb-core", wa.alias);
		assertEquals("org.utgenome.gwt.utgb.server.app", wa.javaPackage);

		assertEquals(3, config.database.size());
		Database db1 = config.database.get(0);
		assertEquals("legacy-track", db1.id);
		assertEquals("sqlite", db1.dbms);
		assertEquals("db/legacy-track.db", db1.address);
		assertNull(db1.user);
		assertNull(db1.pass);
		assertNull(db1.jdbcPrefix);

		Database db2 = config.database.get(1);
		assertEquals("pg", db2.id);
		assertEquals("postgres", db2.dbms);
		assertEquals("localhost", db2.address);
		assertEquals("leo", db2.user);
		assertEquals("pass", db2.pass);
		assertNull(db2.jdbcPrefix);

		Database db3 = config.database.get(2);
		assertEquals("com.mysql.jdbc.Driver", db3.driver);
		assertEquals("mysql-db", db3.id);
		assertEquals("mysql", db3.dbms);
		assertEquals("mydb", db3.address);
		assertEquals("leo2", db3.user);
		assertEquals("pass2", db3.pass);
		assertEquals("jdbc:mysql:///", db3.jdbcPrefix);

		assertEquals("/home/leo/utgb", config.getProperty("database.folder"));
		assertEquals("/usr/local/bin/sqlite3", config.getProperty("sqlite.bin"));

	}

	@Test
	public void parseTest() throws Exception {
		UTGBConfig config = UTGBConfig.parse(FileResource.find(UTGBConfigTest.class, "common-sample.silk"));
		_logger.debug(JSONLens.toJSON(config));

		validate(config);
	}

	@Test
	public void toSilkTest() throws Exception {
		UTGBConfig config = UTGBConfig.parse(FileResource.find(UTGBConfigTest.class, "common-sample.silk"));
		String silk = config.toSilk();
		_logger.debug(silk);

		UTGBConfig newConfig = UTGBConfig.parseSilk(silk);

		validate(newConfig);

	}

}
