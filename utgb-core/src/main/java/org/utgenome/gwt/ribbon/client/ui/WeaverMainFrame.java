/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// WeaverFrame.java
// Since: Apr 23, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.ribbon.client.ui;

import org.utgenome.gwt.utgb.client.ui.RoundCornerFrame;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Main frame of the genome weaver
 * 
 * @author leo
 * 
 */
public class WeaverMainFrame extends Composite {

	public class TrackListPanel extends VerticalPanel {

		public TrackListPanel() {

		}

	}

	private DockPanel mainPanel = new DockPanel();
	private TrackListPanel trackListPanel = new TrackListPanel();
	private SimplePanel trackPanel = new SimplePanel();

	public WeaverMainFrame() {

		mainPanel.add(trackListPanel, DockPanel.EAST);

		RoundCornerFrame f = new RoundCornerFrame("#6699CC", 0.8f, 4);
		f.setWidgetPanel(trackPanel);
		mainPanel.add(f, DockPanel.CENTER);

		initWidget(mainPanel);
	}
}
