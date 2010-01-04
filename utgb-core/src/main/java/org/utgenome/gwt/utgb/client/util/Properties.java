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
// Properties.java
// Since: Jul 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is an wrapper of the {@link HashMap}, which makes easier to convert associated values with keys into
 * primitive types, such as int, boolean, String etc.
 * 
 * @author leo
 * 
 */
public class Properties extends HashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Properties() {

	}

	public Properties(Map<String, String> property) {
		this.putAll(property);
	}

	public void add(String key, String value) {
		super.put(key, value);
	}

	public void add(String key, int value) {
		super.put(key, Integer.toString(value));
	}

	public void add(String key, long value) {
		super.put(key, Long.toString(value));
	}

	public void add(String key, float value) {
		super.put(key, Float.toString(value));
	}

	public void add(String key, boolean value) {
		super.put(key, Boolean.toString(value));
	}

	public String get(String key) {
		return (String) super.get(key);
	}

	/**
	 * Gets the String value associated with the given key. If any corresponding value is not found, returns the default
	 * value.
	 * 
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the string value
	 */
	public String get(String key, String defaultValue) {
		if (containsKey(key))
			return (String) get(key);
		else
			return defaultValue;
	}

	public int getInt(String key) {
		return Integer.parseInt((String) super.get(key));
	}

	/**
	 * Gets the integer value associated with the given key. If any corresponding value is not found, returns the given
	 * default value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInt(String key, int defaultValue) {
		return containsKey(key) ? getInt(key) : defaultValue;
	}

	public float getFloat(String key) {
		return Float.parseFloat((String) super.get(key));
	}
	/**
	 * Gets the float value associated with the given key. If any corresponding value is not found, returns the given
	 * default value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float getFloat(String key, float defaultValue) {
		return containsKey(key) ? getFloat(key) : defaultValue;
	}

	public boolean getBoolean(String key) {
		String value = get(key);
		if (value != null) {
			if (value.equals("true"))
				return true;
		}
		return false;
	}

	/**
	 * Gets the boolean value associated with the given key. If any corresponding value is not found, returns the given
	 * default value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		return containsKey(key) ? getBoolean(key) : defaultValue;
	}

}
