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
// GenomeBrowser Project
//
// XMLAttribute.java
// Since: Jul 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util.xml;

import java.util.ArrayList;

public class XMLAttribute {
	class Pair {
		private String name;
		private String value;

		Pair(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String toString() {
			return name + "=\"" + value + "\"";
		}
	}

	private ArrayList<Pair> attributeTable = new ArrayList<Pair>();

	public XMLAttribute() {

	}

	public XMLAttribute(String name, String value) {
		attributeTable.add(new Pair(name, value));
	}

	public XMLAttribute add(String name, String value) {
		attributeTable.add(new Pair(name, value));
		return this;
	}

	public XMLAttribute add(String name, int value) {
		attributeTable.add(new Pair(name, Integer.toString(value)));
		return this;
	}

	public XMLAttribute add(String name, long value) {
		attributeTable.add(new Pair(name, Long.toString(value)));
		return this;
	}

	public XMLAttribute add(String name, boolean value) {
		attributeTable.add(new Pair(name, value ? "true" : "false"));
		return this;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (; i < attributeTable.size() - 1; i++) {
			Pair pair = attributeTable.get(i);
			buffer.append(pair.toString());
			buffer.append(" ");
		}
		if (i < attributeTable.size())
			buffer.append(attributeTable.get(i).toString());
		return buffer.toString();
	}

}
