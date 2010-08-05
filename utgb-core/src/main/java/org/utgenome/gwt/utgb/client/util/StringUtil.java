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
// StringUtil.java
// Since: Jun 26, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utiltiles for manipulating strings in GWT client codes
 * 
 * @author leo
 * 
 */
public class StringUtil {

	/**
	 * Non constractable
	 */
	private StringUtil() {
	}

	/**
	 * Join the given element list with the specified separator
	 * 
	 * @param elementList
	 *            a list of strings to join
	 * @param separator
	 *            e.g., ",", " ", etc.
	 * @return the concatination of the strings in the elementList, separated by the separator
	 */
	public static String join(String[] elementList, String separator) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < elementList.length - 1; i++) {
			b.append(elementList[i]);
			b.append(separator); // white space
		}
		b.append(elementList[elementList.length - 1]);
		return b.toString();
	}

	public static String join(List<String> elementList, String separator) {
		if (elementList.isEmpty())
			return "";
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < elementList.size() - 1; i++) {
			b.append(elementList.get(i));
			b.append(separator); // white space
		}
		b.append(elementList.get(elementList.size() - 1));
		return b.toString();
	}

	public static String joinIterable(Iterable<String> element, String separator) {
		List<String> list = new ArrayList<String>();
		for (String each : element) {
			list.add(each);
		}
		return join(list, separator);
	}

	/**
	 * 
	 * @param elementList
	 * @return
	 */
	public static String joinWithWS(String[] elementList) {
		return join(elementList, " ");
	}

	public static String unquote(String s) {
		if (s.startsWith("\"") && s.endsWith("\""))
			return s.substring(1, s.length() - 1);
		else
			return s;
	}

	/**
	 * Convert a string representation of an integer with comma into an int value
	 * 
	 * @param sInt
	 * @return
	 */
	public static int toInt(String sInt) {
		if (sInt == null)
			throw new NullPointerException();
		String intWithoutComma = sInt.replaceAll("[ ,]", "");
		return Integer.parseInt(intWithoutComma);
	}

	/**
	 * insert commas to the given number for the readability
	 * 
	 * @param number
	 * @return
	 */
	public static String formatNumber(int number) {
		StringBuilder s = new StringBuilder();
		String intValue = Integer.toString(number);

		final int len = intValue.length();
		for (int i = 0; i < len; ++i) {
			s.append(intValue.charAt(i));

			int digit = len - i - 1;
			if (digit != 0 && (digit % 3 == 0)) {
				s.append(',');
			}
		}
		return s.toString();
	}

}
