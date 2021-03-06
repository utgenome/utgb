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
// utgb-core Project
//
// ValueParameter.java
// Since: Dec 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.egt;

import org.xerial.xml.XMLGenerator;

public class ValueParameter implements Parameter {
	String value;

	/**
	 * @param value
	 */
	public ValueParameter(String value) {
		this.value = value;
	}

	public void toXML(String elementName, XMLGenerator xout) {
		xout.element(elementName, value);
	}
}
