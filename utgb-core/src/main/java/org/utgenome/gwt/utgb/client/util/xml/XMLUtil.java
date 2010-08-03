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
// XMLUtil.java
// Since: Jul 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util.xml;

import java.util.Iterator;
import java.util.Map;

import org.utgenome.gwt.utgb.client.util.CanonicalProperties;

public class XMLUtil {

	/**
	 * Non-constractable
	 */
	private XMLUtil() {
	}

	public static void toXML(Map<String, String> map, XMLWriter xmlWriter) {
		if (map == null)
			return;
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Object value = map.get(key);
			xmlWriter.element("property", new XMLAttribute("key", key), value != null ? value.toString() : "");
		}
	}

	public static void toCanonicalXML(CanonicalProperties map, XMLWriter xmlWriter) {
		if (map == null)
			return;

		for (String cKey : map.keySet()) {
			Object value = map.get(cKey);
			String nKey = CanonicalProperties.toNaturalName(cKey);
			xmlWriter.element("property", new XMLAttribute("key", nKey), value != null ? value.toString() : "");
		}
	}

}
