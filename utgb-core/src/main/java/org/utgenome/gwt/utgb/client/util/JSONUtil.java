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
// JSONUtil.java
// Since: Jul 23, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONUtil {

	/**
	 * Non constractable
	 */
	private JSONUtil() {
	}

	/**
	 * Create a JSONArray string from the given iterable list
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T> String toJSONArray(List<T> iterable) {
		JSONArray array = new JSONArray();
		int index = 0;
		for (Iterator<T> it = iterable.iterator(); it.hasNext();) {
			array.set(index++, new JSONString(it.next().toString()));
		}
		return array.toString();
	}

	public static ArrayList<String> parseJSONArray(String jsonArray) {
		if (jsonArray == null)
			return new ArrayList<String>();

		if (!jsonArray.startsWith("[") || !jsonArray.endsWith("]"))
			throw new IllegalArgumentException("invalid json array data");

		ArrayList<String> elementList = new ArrayList<String>();
		JSONValue v = JSONParser.parseLenient(jsonArray);
		JSONArray array = v.isArray();
		if (array != null) {
			for (int i = 0; i < array.size(); i++)
				elementList.add(toStringValue(array.get(i)));
		}
		return elementList;
	}

	/**
	 * Get a string value (without double quotation) of JSONString, or other JSON types.
	 * 
	 * @param value
	 * @return
	 */
	public static String toStringValue(JSONValue value) {
		if (value == null)
			return "";

		JSONString str = value.isString();
		if (str != null)
			return str.stringValue();
		else
			return value.toString();
	}

}
