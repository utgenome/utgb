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
// OldUTGBOperationParser.java
// Since: 2007/06/21
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.operation.EventImpl;
import org.utgenome.gwt.utgb.client.track.operation.FrameCommand;
import org.utgenome.gwt.utgb.client.track.operation.FrameOperation;
import org.utgenome.gwt.utgb.client.track.operation.LinkOperation;
import org.utgenome.gwt.utgb.client.track.operation.MenuOperation;
import org.utgenome.gwt.utgb.client.track.operation.MenuOperationItem;
import org.utgenome.gwt.utgb.client.track.operation.MouseClickEventImpl;
import org.utgenome.gwt.utgb.client.track.operation.Operation;
import org.utgenome.gwt.utgb.client.track.operation.OperationArea;
import org.utgenome.gwt.utgb.client.track.operation.OperationParser;
import org.utgenome.gwt.utgb.client.track.operation.SubOperation;
import org.utgenome.gwt.utgb.client.util.Utilities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * @author ssksn
 * 
 */
public class OldUTGBOperationParser extends OperationParser {

	private static final OldUTGBOperationParser _parser = new OldUTGBOperationParser();

	public static OperationParser getParser() {
		return _parser;
	}

	private static final String RECT_AREA_NODE_NAME = "rect_area";

	public final void parse(final Document document, final AbsolutePanel panel, final Track track) {
		final NodeList rectAreaNodeList = document.getElementsByTagName(RECT_AREA_NODE_NAME);

		for (int i = 0; i < rectAreaNodeList.getLength(); i++) { // STEP 1: for each rect_area node
			final Node rectAreaNode = rectAreaNodeList.item(i);

			final OperationArea operationArea = OperationArea.newInstance(rectAreaNode); // STEP 1-1: construct an operationArea object
			OperationArea.add(panel, operationArea); // STEP 1-2: add(register) the operationArea object to AbsolutePanel

			final NodeList eventNodeList = Utilities.getTagChildNodes(rectAreaNode);
			for (int j = 0; j < eventNodeList.getLength(); j++) { // STEP 2: for each event node
				final Node eventNode = eventNodeList.item(j);

				final EventImpl eventListener = parseEventNode(eventNode); // STEP 2-1: construct an event node

				operationArea.addEventHandler(eventListener); // STEP 2-2: add(register) the event node to the operationArea

				final NodeList operationNodeList = Utilities.getTagChildNodes(eventNode);
				for (int k = 0; k < operationNodeList.getLength(); k++) { // STEP 3: for each operation node
					final Node operationNode = operationNodeList.item(k);

					final Operation operation = parseOperationNode(operationNode, track);

					eventListener.addOperation(operation);
				}
			}
		}
	}

	protected final EventImpl parseEventNode(final Node eventNode) {
		final String name = eventNode.getNodeName();

		if (name.equals("mouseclick_event")) {
			final MouseClickEventImpl mouseClickEvent = new MouseClickEventImpl();
			return mouseClickEvent;
		}

		return null;
	}

	public final Operation parseOperationNode(final Node operationNode, final Track track) {
		final String name = operationNode.getNodeName();

		if (name.equals("link_operation")) {
			final LinkOperation linkOperation = new LinkOperation(operationNode);
			return linkOperation;
		}

		if (name.equals("menu_operation")) {
			final MenuOperation menuOperation = new MenuOperation(operationNode);

			final NodeList menuItemNodeList = Utilities.getTagChildNodes(operationNode);
			for (int i = 0; i < menuItemNodeList.getLength(); i++) {
				final Node menuItemNode = menuItemNodeList.item(i);

				final MenuOperationItem menuItem = parseMenuItemNode(menuItemNode, track);
				menuOperation.addMenuItem(menuItem);
			}
			return menuOperation;
		}

		if (name.equals("frame_operation")) {
			final FrameOperation frameOperation = new FrameOperation(operationNode, track);

			final NodeList commandNodeList = Utilities.getTagChildNodes(operationNode);
			for (int i = 0; i < commandNodeList.getLength(); i++) {
				final Node commandNode = commandNodeList.item(i);

				try {
					final FrameCommand frameCommand = parseCommandNode(commandNode, track);
					frameOperation.addCommand(frameCommand);
				}
				catch (NumberFormatException e) {
					GWT.log("invalid number", e);
				}
			}
			return frameOperation;
		}

		if (name.equals("suboperation")) {
			final SubOperation subOperation = new OldUTGBSubOperationImpl(operationNode, track);
			return subOperation;
		}

		return null;
	}

	protected final MenuOperationItem parseMenuItemNode(final Node menuItemNode, final Track track) {
		final MenuOperationItem menuItem = new MenuOperationItem(menuItemNode);
		final NodeList operationNodeList = Utilities.getTagChildNodes(menuItemNode);
		for (int i = 0; i < operationNodeList.getLength(); i++) {
			final Node operationNode = operationNodeList.item(i);

			final Operation operation = parseOperationNode(operationNode, track);

			menuItem.addOperation(operation);
		}

		return menuItem;
	}

	protected final FrameCommand parseCommandNode(final Node commandNode, final Track track) {
		final FrameCommand frameCommand = OldUTGBFrameCommandImpl.newInstance(commandNode);

		return frameCommand;
	}

}
