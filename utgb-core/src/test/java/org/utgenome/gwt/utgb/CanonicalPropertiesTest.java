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
// CanonicalPropertiesTest.java
// Since: Aug 3, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.util.CanonicalProperties;

public class CanonicalPropertiesTest {

	@Test
	public void var2nat() throws Exception {
		assertEquals("hello world", CanonicalProperties.toNaturalName("helloWorld"));
		assertEquals("XML string", CanonicalProperties.toNaturalName("XML_String"));
		assertEquals("param name", CanonicalProperties.toNaturalName("paramName"));
		assertEquals("allow TAB in var name", CanonicalProperties.toNaturalName("allowTABinVarName"));
		assertEquals("wiki name like var name", CanonicalProperties.toNaturalName("WikiNameLikeVarName"));

		assertEquals("wiki name like var name", CanonicalProperties.toNaturalName("Wiki Name Like Var Name"));

		assertEquals("var arg01", CanonicalProperties.toNaturalName("var_arg01"));
		assertEquals("para1", CanonicalProperties.toNaturalName("para1"));

		assertEquals("tip and dale", CanonicalProperties.toNaturalName("tip_andDale"));

		assertEquals("action package", CanonicalProperties.toNaturalName("actionPackage"));

	}

}
