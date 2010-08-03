/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// CanonicalProperties.java
// Since: Aug 3, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Using canonical parameter keys for providing case and space insensitive property map.
 * 
 * @author leo
 * 
 */
public class CanonicalProperties extends HashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static HashMap<String, String> canonicalNameTable = new HashMap<String, String>();
	private static HashMap<String, String> naturalNameTable = new HashMap<String, String>();

	public CanonicalProperties() {
		super();
	}

	public static String toCanonicalName(String key) {
		if (key == null)
			return key;

		if (!canonicalNameTable.containsKey(key)) {
			String cKey = key.replaceAll("[\\s-_]", "");
			cKey = key.toLowerCase();
			canonicalNameTable.put(key, cKey);
		}
		return canonicalNameTable.get(key);
	}

	public static String toNaturalName(String keyName) {
		if (keyName == null)
			return null;

		String nName = naturalNameTable.get(keyName);
		if (nName == null) {
			ArrayList<String> components = new ArrayList<String>();
			int start = 0;
			int cursor = 0;
			while (cursor < keyName.length()) {
				while (cursor < keyName.length() && Character.isUpperCase(keyName.charAt(cursor))) {
					cursor++;
				}
				if ((cursor - start) >= 2) {
					components.add(keyName.substring(start, cursor));
					start = cursor;
					continue;
				}
				while (cursor < keyName.length()) {
					char c = keyName.charAt(cursor);
					if (isSplitChar(c)) {
						break;
					}
					cursor++;
				}
				if (start < cursor) {
					components.add(keyName.substring(start, cursor).toLowerCase());
				}
				else
					cursor++;
				start = cursor;
			}
			nName = StringUtil.join(components, " ");
			naturalNameTable.put(keyName, nName);
		}
		return nName;
	}

	private static boolean isSplitChar(char c) {
		return Character.isUpperCase(c) || c == '_' || c == '-' || c == ' ';
	}

	public void add(String key, String value) {
		super.put(toCanonicalName(key), value);
	}

	public void add(String key, int value) {
		super.put(toCanonicalName(key), Integer.toString(value));
	}

	public void add(String key, long value) {
		super.put(toCanonicalName(key), Long.toString(value));
	}

	public void add(String key, float value) {
		super.put(toCanonicalName(key), Float.toString(value));
	}

	public void add(String key, boolean value) {
		super.put(toCanonicalName(key), Boolean.toString(value));
	}

	public String get(String key) {
		return super.get(toCanonicalName(key));
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

		String cKey = toCanonicalName(key);

		if (containsKey(cKey))
			return get(cKey);
		else
			return defaultValue;
	}

	public int getInt(String key) {
		return StringUtil.toInt(get(key));
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
		String cKey = toCanonicalName(key);
		return containsKey(cKey) ? getInt(cKey) : defaultValue;
	}

	public float getFloat(String key) {
		return Float.parseFloat(get(key));
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
		String cKey = toCanonicalName(key);
		return containsKey(cKey) ? getFloat(cKey) : defaultValue;
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
		String cKey = toCanonicalName(key);
		return containsKey(cKey) ? getBoolean(cKey) : defaultValue;
	}

}
