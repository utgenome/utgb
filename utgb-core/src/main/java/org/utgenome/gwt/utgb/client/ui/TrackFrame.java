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
// TrackFrame.java
// Since: Nov 29, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import org.utgenome.gwt.utgb.client.track.Design;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author leo
 *
 */
public class TrackFrame extends Composite implements Frame {

	private final HorizontalPanel layoutFrame = new HorizontalPanel();
	private final FrameBorder frameBorder = new FrameBorder("999999", 2, FrameBorder.NORTH | FrameBorder.SOUTH | FrameBorder.WEST);
	private final AbsolutePanel titleFrame = new AbsolutePanel();
	private final HorizontalPanel iconFrame = new HorizontalPanel(); 
	
	private Icon iconConfig = new Icon(Design.getIconImage(Design.ICON_CONFIG));
	private Icon iconAdjust = new Icon(Design.getIconImage(Design.ICON_PACK));
	private Icon iconMinmize = new Icon(Design.getIconImage(Design.ICON_HIDE));
	private Icon iconClose = new Icon(Design.getIconImage(Design.ICON_CLOSE));
	
	private FixedWidthLabel titleLabel = new FixedWidthLabel("Window Title Message (this is a sample message)", 150);
	
	private boolean isConfigurable = true;
	private boolean isAdjustable = true;
	private boolean isMinimizable = true;
	private boolean isClosable = true;
	
	public TrackFrame()
	{
		buildGUI();
		initWidget(layoutFrame);
	}
	
	protected void buildGUI()
	{
		frameBorder.setPixelSize(150, 20);
		titleFrame.setSize("100%", "100%");
		frameBorder.setWidget(titleFrame);
		
		iconFrame.setSpacing(0);
		iconFrame.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
				
		if(isConfigurable)
		{
			iconFrame.add(iconConfig);
		}
		if(isAdjustable)
			iconFrame.add(iconAdjust);
		if(isMinimizable)
			iconFrame.add(iconMinmize);
		if(isClosable)
			iconFrame.add(iconClose);
		
		titleFrame.add(titleLabel);
		titleFrame.add(iconFrame, 50, 0);
		
		layoutFrame.add(frameBorder);
		
		
		
		
	}
	
	
	public Widget getTitleBar() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAdjustable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isClosable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isConfigurable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isHorizontallyResizable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMinimizable() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isVerticallyResizable() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAdjustable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setClosable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setConfigurable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setHeight(int height) {
		// TODO Auto-generated method stub
		
	}

	public void setHorizontallyRisizable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setMinimizable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void setVerticallyRisizable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public void setWidth(int width) {
		// TODO Auto-generated method stub
		
	}

	
	
}




