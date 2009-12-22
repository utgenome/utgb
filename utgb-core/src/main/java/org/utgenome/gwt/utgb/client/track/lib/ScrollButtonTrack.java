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
// ScrollButtonTrack.java
// Since: Jun 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackWindow;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ScrollButtonTrack has several move buttons that relocate a TrackWindow.
 * 
 * @author leo
 * 
 */
public class ScrollButtonTrack extends TrackBase {
	public static TrackFactory factory() {
		return new TrackFactory() {
			public Track newInstance() {
				return new ScrollButtonTrack();
			}
		};
	}

	class ScrollButtonSet extends Composite {
		HorizontalPanel _panel = new HorizontalPanel();

		class WindowScrollButton extends Button implements ClickHandler {
			int movePercentageOnWindow;

			public WindowScrollButton(String label, int movePercentageOnWindow) {
				super(label);
				setStyleName("scrollbutton");
				addClickHandler(this);
				this.movePercentageOnWindow = movePercentageOnWindow;
			}

			public void onClick(ClickEvent e) {
				TrackWindow window = getTrackGroup().getTrackWindow();
				long genomeRange = window.getEndOnGenome() - window.getStartOnGenome();
				if (genomeRange < 0)
					genomeRange = -genomeRange;

				long offset = (long) (genomeRange * ((double) movePercentageOnWindow / 100.0));
				getTrackGroup().getPropertyWriter().setTrackWindow(window.getStartOnGenome() + offset, window.getEndOnGenome() + offset);
			}
		}

		public ScrollButtonSet() {
			_panel.add(new WindowScrollButton("<<<< ", -95));
			_panel.add(new WindowScrollButton("<<< ", -50));
			_panel.add(new WindowScrollButton("<< ", -25));
			_panel.add(new WindowScrollButton("< ", -10));
			_panel.add(new WindowScrollButton("> ", 10));
			_panel.add(new WindowScrollButton(">> ", 25));
			_panel.add(new WindowScrollButton(">>> ", 50));
			_panel.add(new WindowScrollButton(">>>> ", 95));

			initWidget(_panel);
		}

	}

	private ScrollButtonSet _buttonSet = new ScrollButtonSet();

	public ScrollButtonTrack() {
		super("Scroll Button");
	}

	public int getDefaultWindowHeight() {
		return 20;
	}

	public Widget getWidget() {
		return _buttonSet;
	}

	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		trackFrame.disablePack();
	}

}
