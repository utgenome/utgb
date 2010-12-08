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
// ParameterMap.java
// Since: Dec 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.egt;

import java.util.ArrayList;

import org.xerial.util.Pair;
import org.xerial.xml.XMLGenerator;

/**
 * @author leo
 * 
 */
public class ParameterMap implements Parameter {
	ArrayList<Pair<String, Parameter>> map = new ArrayList<Pair<String, Parameter>>();

	/**
	 * @param map
	 */
	public ParameterMap() {

	}

	public void put(String key, Parameter value) {
		map.add(new Pair<String, Parameter>(key, value));
	}

	/**
	 * @param key
	 * @return value if found. If not found, it returns null
	 */
	public Parameter get(String key) {
		for (Pair<String, Parameter> p : map) {
			if (p.equals(key))
				return p.getSecond();
		}
		return null;
	}

	public void toXML(String elementName, XMLGenerator xout) {
		xout.startTag(elementName);
		for (Pair<String, Parameter> e : map) {
			Parameter p = e.getSecond();
			p.toXML(e.getFirst(), xout);
		}
		xout.endTag();
	}

	public void toXML(XMLGenerator xout) {
		for (Pair<String, Parameter> e : map) {
			Parameter p = e.getSecond();
			p.toXML(e.getFirst(), xout);
		}
	}

}
