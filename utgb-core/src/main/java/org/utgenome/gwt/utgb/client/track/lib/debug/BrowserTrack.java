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
// BrowserTrack.java
// Since: Jun 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.debug;

import org.utgenome.gwt.utgb.client.GenomeBrowser;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link BrowserTrack} simply displays a URL content retrieved from the Internet
 * 
 * @author leo
 * 
 */
public class BrowserTrack extends TrackBase {
	private DockPanel panel = new DockPanel();
	private TextBox urlBox = new TextBox();
	private Button loadButton = new Button("load");
	private TextArea htmlPanel = new TextArea();

	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new BrowserTrack();
			}
		};
	}

	public BrowserTrack() {
		super("URL Content");

		urlBox.setText("http://medaka3.gi.k.u-tokyo.ac.jp/~ssksn/bac_end_tracks/v1.0/bac_end_v1.0_description.xml");

		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent e) {
				draw();
			}
		});

		urlBox.setWidth("400px");
		htmlPanel.setSize("600px", "100px");

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(urlBox);
		hp.add(loadButton);

		panel.add(hp, DockPanel.NORTH);
		panel.add(htmlPanel, DockPanel.CENTER);
	}

	public void clear() {
		htmlPanel.setText("");
	}

	public int getDefaultWindowHeight() {
		return 150;
	}

	public Widget getWidget() {
		return panel;
	}

	public void draw() {
		GenomeBrowser.getService().getHTTPContent(urlBox.getText(), new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				GWT.log("cannot retrieve: " + urlBox.getText(), caught);
			}

			public void onSuccess(String result) {
				htmlPanel.setText(result);
			}
		});
	}

}
