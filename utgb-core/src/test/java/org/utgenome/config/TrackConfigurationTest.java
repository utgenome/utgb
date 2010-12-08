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
// TrackConfigurationTest.java
// Since: Jul 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.config.UTGBConfig.Database;
import org.xerial.lens.XMLLens;
import org.xerial.util.FileResource;

public class TrackConfigurationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void loadTest() throws Exception, IOException {
		TrackConfiguration config = XMLLens.createXMLBean(TrackConfiguration.class, FileResource.open(TrackConfigurationTest.class, "track-config-sample.xml"));

		assertEquals("1.0", config.getVersion());
		assertEquals("org.utgenome", config.getGroup());
		assertEquals("utgb-core", config.getProjectName());
		assertEquals("org.utgenome.gwt.utgb.server", config.getPackage());
		DatabaseInfo dbInfo = config.getDatabase("legacy-track");
		assertNotNull(dbInfo);
		assertEquals("legacy-track", dbInfo.getId());
		List<ConnectionInfo> connectionList = dbInfo.getConnectionList();
		assertEquals(1, connectionList.size());
		ConnectionInfo c = connectionList.get(0);
		assertEquals("sqlite", c.getDbms());
		assertEquals("db/legacy-track.db", c.getAddress());

		assertEquals("/home/leo/utgb", config.getProperty("database.folder", ""));

	}

	@Test
	public void testConvert() throws Exception {
		TrackConfiguration config = XMLLens.createXMLBean(TrackConfiguration.class, FileResource.open(TrackConfigurationTest.class, "track-config-sample.xml"));

		UTGBConfig uc = config.convert();
		assertEquals("1.0", uc.version);
		assertEquals("org.utgenome", uc.group);
		assertEquals("utgb-core", uc.projectName);
		assertEquals("org.utgenome.gwt.utgb.server", uc.javaPackage);

		Database db = uc.getDatabase("legacy-track");
		assertNotNull(db);
		assertEquals("legacy-track", db.id);
		assertEquals("sqlite", db.dbms);
		assertEquals("db/legacy-track.db", db.address);

		assertEquals("/home/leo/utgb", uc.getProperty("database.folder", ""));

	}

}
