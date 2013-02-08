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
// URLQueryArgumentTrack.java
// Since: Jun 15, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import java.util.HashMap;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.util.BrowserInfo;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class URLQueryArgumentTrack extends TrackBase {
	private final HorizontalPanel _panel = new HorizontalPanel();

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new URLQueryArgumentTrack();
			}
		};
	}

	public URLQueryArgumentTrack() {
		super("Query Argument");

		_panel.setStyleName("selector");
	}

	public Widget getWidget() {
		return _panel;
	}

	public void draw() {
		_panel.clear();

		HashMap<String, String> requestParam = BrowserInfo.getURLQueryRequestParameters();
		for (String key : requestParam.keySet()) {
			String value = (String) requestParam.get(key);

			Label l = new Label(key + "=" + value);
			l.setStyleName("selector-item");
			_panel.add(l);
		}
	}

}
