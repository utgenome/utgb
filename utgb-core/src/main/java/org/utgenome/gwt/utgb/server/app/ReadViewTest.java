/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// ReadViewTest.java
// Since: 2010/08/31
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReadViewTest {

	@Test
	public void testIsDescendantOf() {
		assertTrue(ReadView.isDescendant("hello.db"));
		assertTrue(ReadView.isDescendant("human/hg19.fa"));
		assertFalse(ReadView.isDescendant("dummy/../../../password"));
		assertFalse(ReadView.isDescendant("../../../etc/password"));
		assertFalse(ReadView.isDescendant("..\\..\\../etc/password"));
	}

}
