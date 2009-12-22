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
// ORFSearch.java
// Since: Jun 29, 2007
//
// $URL:http://dev.utgenome.org/svn/utgb/branches/browser/leo/window-dev-0.1/src/org/utgenome/gwt/utgb/client/track/lib/scmd/ORFSearch.java $ 
// $Author:leo $
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.scmd;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ORFSearch extends TrackBase {

	private VerticalPanel _panel = new VerticalPanel();
	private TextBox _keywordBox = new TextBox();

	public static TrackFactory factory()
	{
		return new TrackFactory() {
			public Track newInstance() {
				return new ORFSearch();
			}
		};
	}

	/**
	 * @param trackName
	 */
	public ORFSearch() {
		super("SCMD ORF Search");
		
		HorizontalPanel keywordSearchPanel = new HorizontalPanel();
		keywordSearchPanel.setStyleName("selector");
		keywordSearchPanel.add(new Label("ORF Search: "));
		keywordSearchPanel.add(_keywordBox);
		
		_panel.add(keywordSearchPanel);
	}

	public Widget getWidget() {
		return _panel;
	}

}

