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
// SubOperation.java
// Since: 2007/06/18
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.operation;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.util.GETMethodURL;
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
public class SubOperation extends OperationImpl {
	protected final GETMethodURL url;

	public SubOperation(final Node subOperationNode, final Track track) {
		super(track);

		this.url = GETMethodURL.newInstance(Utilities.getAttributeValue(subOperationNode, "url"));
	}

	public void execute(Widget sender, int x, int y) {
		final String fullURL = url.getURL();

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

		public void onSuccess(String result) {
			if (result.length() <= 0) {
				return;
			}
			{
				final Document dom = XMLParser.parse(result);

				final NodeList suboperation_layerNodeList = dom.getElementsByTagName("suboperation_layer");
				if (suboperation_layerNodeList.getLength() != 1) {
					throw new AssertionError();
				}

				final Node suboperation_layerNode = suboperation_layerNodeList.item(0);

				final NodeList operationNodeList = Utilities.getTagChildNodes(suboperation_layerNode);
				for (int k = 0; k < operationNodeList.getLength(); k++) {
					final Node operationNode = operationNodeList.item(k);

					final OperationParser parser = OperationParser.getParser();
					final Operation operation = parser.parseOperationNode(operationNode, track);

					operation.execute(sender, x, y);
				}
			}
		}

	}

}
