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
// BugReport Project
//
// DynamicFormTest.java
// Since: 2007/03/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.web.client.db;

import org.utgenome.gwt.web.client.db.datatype.BooleanType;
import org.utgenome.gwt.web.client.db.datatype.IntegerType;
import org.utgenome.gwt.web.client.db.datatype.StringType;
import org.utgenome.gwt.web.client.db.datatype.TextType;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;

public class DynamicFormTest extends GWTTestCase
{
    private Relation _relation = new Relation();
    
    public String getModuleName()
    {
        return "org.utgenome.gwt.web.Web";
    }
    
    public void setUp()
    {
        _relation.add(new StringType("name"));
        _relation.add(new IntegerType("id"));
        _relation.add(new BooleanType("check"));
        _relation.add(new TextType("note"));
    }
    
    
    public void testDataForm()
    {
        DynamicForm f = new DynamicForm();
    }

    public void testSetRelataion()
    {
        DynamicForm f = new DynamicForm();
        f.setRelataion(_relation);
    }

    public void testGetInputData()
    {
        DynamicForm f = new DynamicForm();
        f.setRelataion(_relation);
        
        JSONObject obj = f.getInputData();
        String jsonStr = obj.toString();
        
    }



}




