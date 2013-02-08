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
// Utilities.java
// Since: 2007/06/12
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.Iterator;
import java.util.Set;

import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.util.xml.NodeListImpl;

import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Utilitiy methods. No constructor is provided for the class.
 * 
 * @author ssksn
 * @since GWT1.4
 * @version 0.1
 */
public final class Utilities {

	/**
	 * Non constractable 
	 */
	private Utilities() {
		throw new AssertionError();
	}

	/**
	 * Gets the key and value string
	 * 
	 * @param key
	 *            key
	 * @param connector
	 * @param value
	 *            value
	 * @return key + connector + value
	 */
	public static final String getKeyAndValueString(final String key, final String connector, final String value) {
		return key + connector + value;
	}

	/**
	 * Gets the key and value string
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return "key=value"
	 * @see #getKeyAndValueString(String, String, String)
	 */
	public static final String getKeyAndValueString(final String key, final String value) {
		return getKeyAndValueString(key, "=", value);
	}

	public static final String getAttributeValue(final Node node, final String attributeName) {
		final NamedNodeMap attributeMap = node.getAttributes();
		final Node attributeNode = attributeMap.getNamedItem(attributeName);
		if (attributeNode == null)
			return null;
		else
			return attributeNode.getNodeValue();
	}

	public static final String getAttributeValue(final Node node, final String attributeName, final String defaultValue) {
		String value = getAttributeValue(node, attributeName);
		return value != null ? value : defaultValue;
	}

	public static final NodeList getTagChildNodes(final Node parent) {
		final NodeList originalNodeList = parent.getChildNodes();

		final NodeListImpl tagNodeList = new NodeListImpl();

		final int SIZE = originalNodeList.getLength();
		for (int i = 0; i < SIZE; i++) {
			final Node originalNode = originalNodeList.item(i);

			final short nodeType = originalNode.getNodeType();
			if (nodeType != Node.TEXT_NODE) {
				tagNodeList.add(originalNode);
			}
		}

		return tagNodeList;
	}

	public static final String getPropertyXMLTag(final String key, final String value) {
		return "<property key=\"" + key + "\">" + value + "</property>";
	}

	public static final String convertTrackGroupPropertyToXML(final TrackGroupProperty trackGroupProperty) {
		final Set<String> keySet = trackGroupProperty.keySet();
		if (keySet.size() == 0)
			return null;

		final StringBuffer buf = new StringBuffer();

		final Iterator<String> keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			final String key = (keyIterator.next());

			final String value = (trackGroupProperty.getProperty(key));

			if (buf.length() > 0)
				buf.append('\n');
			buf.append(getPropertyXMLTag(key, value));
		}

		return buf.toString();
	}

	public static final String convertTrackGroupPropertyToXML(final TrackGroupProperty trackGroupProperty, final Set<String> outputKeySet) {
		final Set<String> keySet = trackGroupProperty.keySet();
		if (keySet.size() == 0)
			return null;

		final StringBuffer buf = new StringBuffer();

		final Iterator<String> keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			final String key = (keyIterator.next());

			if (outputKeySet.contains(key)) {
				final String value = (trackGroupProperty.getProperty(key));

				if (buf.length() > 0)
					buf.append('\n');
				buf.append(getPropertyXMLTag(key, value));
			}
		}

		if (buf.length() == 0)
			return null;
		else
			return buf.toString();
	}
}
