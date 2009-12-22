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
// RelationTest.java
// Since: Jun 15, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.web.client.db;

import org.utgenome.gwt.web.client.UTGBClientException;
import org.utgenome.gwt.web.client.db.datatype.DataType;

import com.google.gwt.junit.client.GWTTestCase;


public class RelationTest extends GWTTestCase {


    public String getModuleName()
    {
        return "org.utgenome.gwt.web.Web";
    }
    
    public void setUp()
    {
 
    }
    
    public void testJSONRead() throws UTGBClientException
    {
    	String json = "{\"relation\":[[\"id\",\"integer\"], [\"name\", \"string\"]]}";
    	Relation r = new Relation(json);
    	
    	DataType dt = r.getDataType(0);
    	assertEquals("id", dt.getName());
    	assertEquals("integer", dt.getTypeName());
    	
    	DataType dt2 = r.getDataType(1);
    	assertEquals("name", dt2.getName());
    	assertEquals("string", dt2.getTypeName());
    }

}




