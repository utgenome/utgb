/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// UTGB Common Project
//
// DatabaseCatalogTest.java
// Since: Jun 15, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.web.client.db;

import java.util.ArrayList;

import org.utgenome.gwt.web.client.UTGBClientException;
import org.utgenome.gwt.web.client.db.datatype.DataType;

import com.google.gwt.junit.client.GWTTestCase;

public class DatabaseCatalogTest extends GWTTestCase {

	public String getModuleName() {
        return "org.utgenome.gwt.web.Web";
	}

	public void testJSONread() throws UTGBClientException
	{
		String json =  "{\"table_1\":{\"relation\":[[\"id\",\"integer\"], [\"name\",\"string\"]]}, \"table_2\":{\"relation\":[[\"p_id\",\"integer\"], [\"phone\",\"string\"]]}}";
		DatabaseCatalog c = new DatabaseCatalog(json);
		
		ArrayList tableNameList = c.getTableNameList();
		assertEquals(2, tableNameList.size());
		assertEquals("table_1", tableNameList.get(0).toString());
		assertEquals("table_2", tableNameList.get(1).toString());
		
		ArrayList relationList = c.getRelationList();
		assertEquals(2, relationList.size());

		Relation r1 = (Relation) relationList.get(0);
		assertEquals(2, r1.getDataTypeList().size());
		DataType dt1_1 = r1.getDataType(0);
		assertEquals("id", dt1_1.getName());
		assertEquals("integer", dt1_1.getTypeName());
		DataType dt1_2 = r1.getDataType(1);
		assertEquals("name", dt1_2.getName());
		assertEquals("string", dt1_2.getTypeName());
		
		Relation r2 = (Relation) relationList.get(1);
		assertEquals(2, r2.getDataTypeList().size());
		DataType dt2_1 = r2.getDataType(0);
		assertEquals("p_id", dt2_1.getName());
		assertEquals("integer", dt2_1.getTypeName());
		DataType dt2_2 = r2.getDataType(1);
		assertEquals("phone", dt2_2.getName());
		assertEquals("string", dt2_2.getTypeName());

		
	}
	
}




