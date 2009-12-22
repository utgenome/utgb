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
// DatabaseFolderTest.java
// Since: Nov 19, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bean.DatabaseEntry;

public class DatabaseFolderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDatabaseFolder() {
		DatabaseEntry d = DatabaseEntry.newFolder("org/utgenome");
		assertEquals("org/utgenome", d.path);
		assertEquals("utgenome", d.leaf());

		assertEquals("org/utgenome/hello.sqlite", d.dbPath("hello.sqlite"));
		assertEquals("org/utgenome/leo/hello.sqlite", d.dbPath("leo/hello.sqlite"));

		DatabaseEntry p = d.parent();
		assertEquals("org", p.path);
		assertEquals("org", p.leaf());
		DatabaseEntry n = p.parent();
		assertNull(n);

	}

}
