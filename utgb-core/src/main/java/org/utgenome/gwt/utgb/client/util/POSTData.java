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
// UTGBMedaka Project
//
// POSTData.java
// Since: Aug 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import com.google.gwt.http.client.URL;

public class POSTData {
	
	int numParameter = 0;
	StringBuffer buffer = new StringBuffer();
	
	public POSTData()
	{}
	
	public void addParameter(String name, String value)
	{
		if(numParameter > 0)
			buffer.append("&");
		
		buffer.append(URL.encodeComponent(name));
		buffer.append("=");
		buffer.append(URL.encodeComponent(value));
	
		numParameter++;
	}
	
	public String getPostData()
	{
		return buffer.toString();
	}
}




