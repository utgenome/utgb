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
// XMLWriter.java
// Since: Jul 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util.xml;

import java.util.ArrayList;

public class XMLWriter {

	private StringBuffer _out = new StringBuffer();
	private ArrayList<String> _tagStack = new ArrayList<String>();
	private int _currentLevel = 0;

	public XMLWriter() {

	}

	@Override
	public String toString() {
		return _out.toString();
	}

	private void padding(int level) {
		for (int i = 0; i < level; i++)
			_out.append("\t");
	}

	public XMLWriter start(String tagName) {
		return start(tagName, null);
	}

	public XMLWriter start(String tagName, String attribute, String attributeValue) {
		return start(tagName, new XMLAttribute(attribute, attributeValue));
	}

	public XMLWriter start(String tagName, XMLAttribute attribute) {
		padding(_currentLevel);
		if (attribute == null)
			_out.append("<" + tagName + ">");
		else
			_out.append("<" + tagName + " " + attribute.toString() + ">");
		_out.append("\n");
		pushTag(tagName);
		return this;
	}

	private void pushTag(String tagName) {
		_tagStack.add(tagName);
		_currentLevel++;
	}

	private void popTag() {
		_tagStack.remove(_tagStack.size() - 1);
		_currentLevel--;
	}

	public XMLWriter text(String text) {
		_out.append(text);
		return this;
	}

	public XMLWriter element(String tagName, XMLAttribute attribute, String elementContent) {
		padding(_currentLevel);
		pushTag(tagName);
		_out.append("<" + tagName + " " + attribute.toString() + ">");
		_out.append(escape(elementContent));
		_out.append("</" + tagName + ">\n");
		popTag();
		return this;
	}

	public XMLWriter element(String tagName, XMLAttribute attribute) {
		padding(_currentLevel);
		pushTag(tagName);
		_out.append("<" + tagName + " " + attribute.toString() + "/>");
		_out.append("\n");
		popTag();
		return this;
	}

	public static String escape(String text) {
		if (text == null)
			return text;

		String value = text;
		value = value.replaceAll("&", "&amp;");
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		return value;
	}

	public XMLWriter end() {
		if (_tagStack.size() == 0)
			throw new IllegalStateException("no more tag to close");

		padding(_currentLevel - 1);
		String tagName = _tagStack.get(_tagStack.size() - 1);
		_out.append("</" + tagName + ">");
		_out.append("\n");

		popTag();
		return this;
	}

	public XMLWriter endDocument() {
		for (int i = 0; i < _tagStack.size(); i++) {
			end();
		}
		return this;
	}

}
