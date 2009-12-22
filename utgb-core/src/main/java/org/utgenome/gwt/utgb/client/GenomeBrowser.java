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
// GenomeBrowser.java
// Since: Apr 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client;

import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackQueue;
import org.utgenome.gwt.utgb.client.ui.tab.TabViewer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * {@link GenomeBrowser} holds the root track group and RPC service instance
 * 
 * In order to access the web server through {@link BrowserService} interface, use the {@link #getService()} method:
 * <p>
 * Example
 * </p>
 * <code>
 * BrowserServiceAsyc service = GenomeBrowser.getService();
 * service.getHTTPContent("http://www.google.com", new AsyncCallback()
 *          public void onFailure(Throwable caught)
 *          {
 *              
 *          }
 * 
 *          public void onSuccess(Object result)
 *          {
 *             String content = (String) result;
 *             // do something with the given content
 *          }});
 * ...
 * </code>
 * 
 * @author leo
 * 
 */
public class GenomeBrowser {
	private TabViewer tabViewer = new TabViewer();
	private static BrowserServiceAsync _service = null;
	private static TrackGroup _rootGroup = new TrackGroup("root");
	private static TrackQueue _mainQueue = new TrackQueue(_rootGroup);

	public static void initServices() {
		// set up an access interface to the web service
		_service = (BrowserServiceAsync) GWT.create(BrowserService.class);
	}

	public static BrowserServiceAsync getService() {
		if (_service == null)
			throw new IllegalStateException("BrowserService is not initialized");
		return _service;
	}

	public static void hideLoadingMessage() {
		Element _loadingMessage = DOM.getElementById("loading");
		if (_loadingMessage != null) {
			RootPanel.setVisible(_loadingMessage, false);
		}
	}

	public static void showLoadingMessage() {
		Element _loadingMessage = DOM.getElementById("loading");
		if (_loadingMessage != null) {
			RootPanel.setVisible(_loadingMessage, true);
		}
	}
}
