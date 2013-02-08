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
// DataTypeBase.java
// Since: 2007/04/13
//
// $Date$
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.db.datatype;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


public abstract class DataTypeBase implements DataType
{
	protected String name;
	protected boolean isPrimaryKey = false;
	protected boolean isNotNull = false;
	
	public DataTypeBase(String name)
	{
		this.name = name;
	}
	
	public DataTypeBase(String name, boolean isPrimaryKey)
	{
		this.name = name;
		this.isPrimaryKey = isPrimaryKey;
	}

	// hide the default constructor
	private DataTypeBase() {}
	
	public DataTypeBase(String name, boolean isPrimaryKey, boolean isNotNull) {
		this.name = name;
		this.isPrimaryKey = isPrimaryKey;
		this.isNotNull = isNotNull;
	}

	public String getName() {
		return name;
	}
	
	public String toString(JSONValue value)
	{
		JSONString s = value.isString();
		if(s != null)
			return s.stringValue();
		else
			return value.toString();
	}
	

	public void setAsPrimaryKey()
	{
		this.isPrimaryKey = true;
	}
	
	public boolean isPrimaryKey()
	{
		return isPrimaryKey;
	}
	
	public void setNotNull()
	{
		this.isNotNull = true;
	}
	public boolean isNotNull()
	{
		return isNotNull;
	}

}




