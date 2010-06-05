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
// DataType.java
// Since: 2007/04/13
//
// $Date$
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db.datatype;

import com.google.gwt.json.client.JSONValue;

/**
 * A common interface for data types of configuration parameters, database contents etc.
 * 
 * @author leo
 * 
 */
public interface DataType {

	/**
	 * parameter name of this type
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get a user interface for retrieving the data of this data type
	 * 
	 * @return
	 */
	public InputForm getInputForm();

	/**
	 * 
	 * @param json
	 * @return
	 */
	public String toString(JSONValue json);

	public String getTypeName();

	public boolean isPrimaryKey();

	public boolean isNotNull();
}
