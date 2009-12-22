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
// WebModuleTest.java
// Since: 2007/03/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.web;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.utgenome.gwt.web.client.db.DatabaseCatalogTest;
import org.utgenome.gwt.web.client.db.DynamicFormTest;
import org.utgenome.gwt.web.client.db.RelationTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({DynamicFormTest.class, RelationTest.class, DatabaseCatalogTest.class})
public class WebModuleTest
{
 

}




