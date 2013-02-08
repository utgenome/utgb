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
// utgb-core Project
//
// SVGPanel.java
// Since: 2007/11/26
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author leo
 * 
 */
public class SVGPanel extends Composite {
	private static final String idPrefix = "svgpanel";
	private static int panelCount = 1;

	private final SimplePanel basePanel = new SimplePanel();
	private final int panelID;

	public SVGPanel() {
		panelID = panelCount++;
		init();
	}

	public SVGPanel(String url) {
		panelID = panelCount++;
		init();

		setSVGFromURL(url);
	}

	protected void init() {
		DOM.setElementAttribute(basePanel.getElement(), "id", idPrefix + panelID);
		initWidget(basePanel);
	}

	public void setSVGFromURL(String url) {
		setSVGFromURL_internal(idPrefix + panelID, url);
	}

	protected native void setSVGFromURL_internal(String id, String url) /*-{
	      new $wnd.Ajax.Updater(id, url, { method: 'get' });
	   }-*/;

	/*
	 * { HTTPRequest.asyncGet(url, new ResponseTextHandler() { public void onCompletion(String svg) { setSVG(svg); } } ); }
	 */

}
