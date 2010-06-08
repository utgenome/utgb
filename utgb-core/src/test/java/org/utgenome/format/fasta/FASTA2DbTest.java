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
// FASTA2DbTest.java
// Since: Jul 27, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.util.FileResource;

public class FASTA2DbTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateDB() throws Exception {
		FASTADatabase p = new FASTADatabase();
		SQLiteAccess db = new SQLiteAccess();
		p.createDB(FileResource.open(FASTA2DbTest.class, "sample.fasta"), db);
	}

}
