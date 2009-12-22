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
// OldUTGBSubOperationImpl.java
// Since: 2007/06/21
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import java.util.HashMap;
import java.util.Map;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.operation.Operation;
import org.utgenome.gwt.utgb.client.operation.OperationParser;
import org.utgenome.gwt.utgb.client.operation.SubOperation;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupProperty;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.util.Utilities;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author ssksn
 * 
 */
public class OldUTGBSubOperationImpl extends SubOperation {

	/**
	 * @param subOperationNode
	 * @param track
	 */
	public OldUTGBSubOperationImpl(Node subOperationNode, Track track) {
		super(subOperationNode, track);
	}

	public void execute(Widget sender, int x, int y) {
		final TrackGroup trackGroup = getTrack().getTrackGroup();

		final Map<String, String> parameterMap = new HashMap<String, String>();

		final String[] propertyNameArray = OldUTGBProperty.getPropertyNameArray();

		final TrackGroupProperty propertyReader = trackGroup.getPropertyReader();

		for (int i = 0; i < propertyNameArray.length; i++) {
			final String key = propertyNameArray[i];
			final String value = propertyReader.getProperty(key);

			parameterMap.put(key, value);
		}

		final TrackWindow trackWindow = trackGroup.getTrackWindow();

		final long startIndex = trackWindow.getStartOnGenome();
		final long endIndex = trackWindow.getEndOnGenome();

		parameterMap.put("start", Long.toString(startIndex));
		parameterMap.put("end", Long.toString(endIndex));
		parameterMap.put("width", Integer.toString(((OldUTGBTrack) track).getMainPanelWidth()));

		{
			final Widget absoluteParentPanel = sender.getParent();

			final int parentAbsX = absoluteParentPanel.getAbsoluteLeft();
			final int parentAbsY = absoluteParentPanel.getAbsoluteTop();

			final int selfAbsX = sender.getAbsoluteLeft();
			final int selfAbsY = sender.getAbsoluteTop();

			final int relativeX = selfAbsX - parentAbsX + x;
			final int relativeY = selfAbsY - parentAbsY + y;

			parameterMap.put("pos", Integer.toString(relativeX) + "," + Integer.toString(relativeY));
			// parameterMap.put("y", Integer.toString(relativeY));
		}

		final String fullURL = url.getURL(parameterMap);

		GenomeBrowser.getService().getHTTPContent(fullURL, new Command(sender, x, y));
	}

	private class Command implements AsyncCallback<String> {
		private final Widget sender;
		private final int x;
		private final int y;

		Command(final Widget sender, final int x, final int y) {
			this.sender = sender;
			this.x = x;
			this.y = y;
		}

		public void onFailure(Throwable caught) {
		}

		public void onSuccess(String text) {
			if (text.length() <= 0) {
				return;
			}
			{
				final Document dom = XMLParser.parse(text);

				final NodeList suboperation_layerNodeList = dom.getElementsByTagName("suboperation_layer");
				if (suboperation_layerNodeList.getLength() != 1) {
					throw new AssertionError();
				}

				final Node suboperation_layerNode = suboperation_layerNodeList.item(0);

				final NodeList operationNodeList = Utilities.getTagChildNodes(suboperation_layerNode);
				for (int k = 0; k < operationNodeList.getLength(); k++) {
					final Node operationNode = operationNodeList.item(k);

					final OperationParser parser = OldUTGBOperationParser.getParser();
					final Operation operation = parser.parseOperationNode(operationNode, track);

					operation.execute(sender, x, y);
				}
			}
		}

	}

}
