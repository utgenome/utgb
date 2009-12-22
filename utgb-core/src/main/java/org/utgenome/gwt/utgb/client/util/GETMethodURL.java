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
// GETMethodURL.java
// Since: 2007/06/19
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ssksn
 * 
 */
public class GETMethodURL {

	protected String baseURL;

	private final Map<String, String> keyAndValueMap = new HashMap<String, String>();

	public static final GETMethodURL newInstance(final String url) {
		final int questionIndex = url.indexOf('?');

		if (questionIndex == -1) {
			return new GETMethodURL(url);
		}
		else {
			final String baseURL = url.substring(0, questionIndex);

			final GETMethodURL _url = new GETMethodURL(baseURL);

			final String parameterStr = url.substring(questionIndex + 1);

			final String[] parameters = parameterStr.split("&");

			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i].length() == 0)
					continue;

				final String[] elements = parameters[i].split("=");

				if (elements.length == 2) {
					_url.put(elements[0], elements[1]);
				}
				else if (elements.length == 1) {
					if (parameters[i].endsWith("=")) {
						_url.put(elements[0], "");
					}
				}
			}

			return _url;
		}
	}

	protected GETMethodURL(final String baseURL) {
		this.baseURL = baseURL;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getValue(final String key) {
		return (keyAndValueMap.get(key));
	}

	public String put(final String key, final String value) {
		if (keyAndValueMap.containsKey(key)) {
			final String oldValue = (keyAndValueMap.get(key));

			keyAndValueMap.put(key, value);
			return oldValue;
		}
		else {
			keyAndValueMap.put(key, value);
			return null;
		}
	}

	public String getURL() {
		return getURL(new HashMap<String, String>());
	}

	public String getURL(final Map<String, String> parameterMap) {
		final StringBuffer buf = new StringBuffer(getBaseURL());

		char delimiter = '?';

		{
			final Set<Map.Entry<String, String>> defaultParameterSet = keyAndValueMap.entrySet();
			final Iterator<Map.Entry<String, String>> defaultIt = defaultParameterSet.iterator();

			while (defaultIt.hasNext()) {
				final Map.Entry<String, String> defaultEntry = defaultIt.next();

				final String defaultKey = (String) (defaultEntry.getKey());
				final String defaultValue = (String) (defaultEntry.getValue());

				String value = defaultValue;
				if (parameterMap.containsKey(defaultKey)) {
					value = (String) (parameterMap.get(defaultKey));
				}

				buf.append(delimiter);
				delimiter = '&';
				buf.append(defaultKey + "=" + value);
			}
		}
		{
			final Set<Map.Entry<String, String>> inputParameterSet = parameterMap.entrySet();
			final Iterator<Map.Entry<String, String>> inputIt = inputParameterSet.iterator();

			while (inputIt.hasNext()) {
				final Map.Entry<String, String> inputEntry = inputIt.next();

				final String inputKey = (String) (inputEntry.getKey());
				final String inputValue = (String) (inputEntry.getValue());

				if (!keyAndValueMap.containsKey(inputKey)) {
					// This key and value are not output yet.
					buf.append(delimiter);
					delimiter = '&';
					buf.append(inputKey + "=" + inputValue);
				}
			}
		}

		return buf.toString();
	}
}
