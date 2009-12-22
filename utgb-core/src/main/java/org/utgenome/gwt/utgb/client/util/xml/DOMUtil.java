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
// DOMUtil.java
// Since: Jul 23, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util.xml;


import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Utilities to traverse DOM elements
 * @author leo
 *
 */
public class DOMUtil {

	/**
	 * Non constractable
	 */
	private DOMUtil()
	{}
	
	public static String getAttributeValue(Node node, String attributeName)
	{
		Node attributeValue = node.getAttributes().getNamedItem(attributeName);
		return attributeValue != null ? attributeValue.getNodeValue() : null;
	}
	
	public static String getAttributeValue(Node node, String attributeName, String defaultValue)
	{
		String value = getAttributeValue(node, attributeName);
		return value != null ? value : defaultValue;
	}
	
	public static boolean getBooleanAttributeValue(Node node, String attributeName, boolean defaultValue)
	{
		String value = getAttributeValue(node, attributeName);
		return value != null ? (value.equals("true") ? true : false) : defaultValue;
	}
	
	public static int getIntAttributeValue(Node node, String attributeName, int defaultValue)
	{
		String value = getAttributeValue(node, attributeName);
		try
		{
			return value != null ? Integer.parseInt(value) : defaultValue;
		}
		catch(NumberFormatException e)
		{
			GWT.log("parseInt: " + value, e);
			return defaultValue;
		}
	}
	
	public static NodeList getChildNodeList(Node baseNode, String tagName)
	{
		NodeList childNodeList = baseNode.getChildNodes();
		NodeListImpl returnNodeList = new NodeListImpl();
        for ( int i = 0; i < childNodeList.getLength(); i++ ) {
            Node childNode = childNodeList.item(i);
            short nodeType = childNode.getNodeType();
            if ( nodeType != Node.TEXT_NODE && childNode.getNodeName().equalsIgnoreCase(tagName) ) {
                returnNodeList.add(childNode);
            }
        }
		return returnNodeList;
	}
	
}




