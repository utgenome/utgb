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
// EagyGeneLine.java
// Since: Dec 10, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.egt;

import org.utgenome.UTGBException;
import org.xerial.util.Pair;
import org.xerial.util.StringUtil;
import org.xerial.xml.XMLGenerator;

public class EagyGeneLine {
	private ParameterMap parameterMap;

	public EagyGeneLine(String line) throws UTGBException {
		parameterMap = parseLine(line);
	}

	public Parameter getValue(String key) {
		return parameterMap.get(key);
	}

	/**
	 * parsing each line then convert its contents into a ParameterMap
	 * 
	 * @param line
	 * @return
	 * @throws UTGBException
	 */
	public static ParameterMap parseLine(String line) throws UTGBException {
		ParameterMap map = new ParameterMap();

		String[] element = split(line);
		if (element.length == 0)
			return map;

		ParameterMap childMap = new ParameterMap();
		for (int i = 1; i < element.length; i++) {
			try {
				Pair<String, String> pair = parse(element[i]);

				String key = pair.getFirst();
				String value = pair.getSecond();
				if (key.equals("range")) {
					setRange(value, childMap);
				}
				else if (key.equals("exon")) {
					ParameterMap rangeMap = new ParameterMap();
					setRange(value, rangeMap);
					childMap.put(key, rangeMap);
				}
				else {
					childMap.put(key, new ValueParameter(value));
				}
			}
			catch (UTGBException e) {
				// ignore the invalid parameter
				System.err.println(e);
			}
		}
		map.put(element[0], childMap);

		return map;
	}

	public static String[] split(String inputLine) {
		return inputLine.split("[\t ]+");
	}

	public static Pair<String, String> parse(String parameter) throws UTGBException {
		String[] keyAndValue = parameter.split("=");

		if (keyAndValue.length == 2) {
			return new Pair<String, String>(keyAndValue[0], StringUtil.unquote(keyAndValue[1]));
		}
		else {
			throw new UTGBException("invalid format: " + parameter);
		}

	}

	public static void setRange(String value, ParameterMap map) throws UTGBException {
		String[] range = splitByComma(value);
		if (range.length != 2)
			throw new UTGBException("invalid range: " + value);
		map.put("start", new ValueParameter(range[0]));
		map.put("end", new ValueParameter(range[1]));
	}

	public static String[] splitByComma(String value) {
		return value.split(",");
	}

	public void toXML(XMLGenerator xout) {
		parameterMap.toXML(xout);
	}

}
