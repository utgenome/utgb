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
// ChracteristicShapeTrack.java
// Since: Jun 29, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.scmd;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;


import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

class CharacteristicShapeImage extends Image
{
	private static String baseURL = "http://scmd.gi.k.u-tokyo.ac.jp/datamine/cellshape.png?clip=true&orf=YAL002w";
	private String orf;
	
	public CharacteristicShapeImage(String orf)
	{
		setOrf(orf);
	}
	
	public void setOrf(String orf)
	{
		this.orf = orf;
		setUrl(baseURL + this.orf);
	}

}


public class CharacteristicShapeTrack extends TrackBase {

	public static TrackFactory factory()
	{
		return new TrackFactory() {
			public Track newInstance() {
				return new CharacteristicShapeTrack();
			}
		};
	}

	private VerticalPanel _layoutPanel = new VerticalPanel();
	private TextBox _keywordBox = new TextBox();
	private AbsolutePanel _basePanel = new AbsolutePanel();
	
	
	/**
	 * 
	 */
	public CharacteristicShapeTrack() {
		super("SCMD Characteristic Shape");
		
		HorizontalPanel searchPanel = new HorizontalPanel();
		searchPanel.setStyleName("selector");
		searchPanel.add(new Label("input orf:"));
		searchPanel.add(_keywordBox);

		
		_layoutPanel.add(searchPanel);
		_layoutPanel.add(_basePanel);
	}

	
	
	public Widget getWidget() {
		return _basePanel;
	}


	public void setUp(TrackFrame trackFrame, TrackGroup group) {
	}
	
}

