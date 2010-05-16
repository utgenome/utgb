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
// OperationParser.java
// Since: 2007/06/13
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.operation;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.lib.old.OldUTGBOperationParser;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

/**
 * 
 * @author ssksn
 * @since GWT 1.4
 * @version 0.1
 */
public abstract class OperationParser {
	private static OperationParser _parser = new OldUTGBOperationParser();

	public static OperationParser getParser() {
		return _parser;
	}

	public final void parse(final String operationXMLStr, final AbsolutePanel panel, final Track track) {
		final Document document = XMLParser.parse(operationXMLStr);

		parse(document, panel, track);
	}

	public abstract void parse(final Document document, final AbsolutePanel panel, final Track track);

	protected abstract EventImpl parseEventNode(final Node eventNode);

	public abstract Operation parseOperationNode(final Node operationNode, final Track track);

	protected abstract MenuOperationItem parseMenuItemNode(final Node menuItemNode, final Track track);

	protected abstract FrameCommand parseCommandNode(final Node commandNode, final Track track);

}
