/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// CanvasChain.java
// Since: Jun 4, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.track.TrackWindow;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

/**
 * A chain of canvases for supporting GoogleMap-style graphic drawing.
 * 
 * <pre>
 *          (view window) 
 *     |----------------------|
 * |--------||--------||--------||--------|
 *    (w1)      (w2)      (w3)     (w4:prefetch)
 *                                
 * After scrolling to right:                               
 * 
 *                   (view window) 
 *             |------------------------|
 * |--------||--------||--------||--------||--------|
 *    (w1)      (w2)      (w3)     (w4)       (w5:prefetch)
 * </pre>
 * 
 * @author leo
 * 
 */
public class CanvasChain extends Composite {

	private AbsolutePanel layoutPanel = new AbsolutePanel();
	private TrackWindow viewWindow;

	private List<TrackWindow> childWindow = new ArrayList<TrackWindow>();

	public CanvasChain() {

		initWidget(layoutPanel);
	}

	public void setViewWindow(TrackWindow view) {
		this.viewWindow = view;
	}

	public void setViewPixelSize(int width, int height) {
		layoutPanel.setPixelSize(width, height);
	}

}
