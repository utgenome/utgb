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
// WeaverCanvas.java
// Since: 2010/04/23
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.ribbon.client.ui;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.widgetideas.graphics.client.CanvasGradient;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Multi layer canvas for drawing genomic data
 * 
 * @author leo
 * 
 */
public class WeaverCanvas extends Composite {

	private final AbsolutePanel mainPanel;
	private final GWTCanvas canvas1;
	private final GWTCanvas canvas2;

	public WeaverCanvas() {

		int width = 800;
		int height = 500;
		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(width, height);
		canvas1 = new GWTCanvas(width, height);
		canvas2 = new GWTCanvas(width, height);

		canvas1.setFillStyle(new Color(0, 0, 0, 0f));
		canvas1.fillRect(0, 0, width, height);
		canvas2.setFillStyle(new Color(0, 0, 0, 0f));
		canvas2.fillRect(0, 0, width, height);

		CanvasGradient gr = canvas1.createRadialGradient(45, 45, 10, 52, 50, 30);
		gr.addColorStop(0, new Color(255, 0, 0));
		gr.addColorStop(1, new Color(0, 0, 255));
		canvas1.setFillStyle(gr);
		//canvas1.setFillStyle(new Color(200, 150, 150));
		canvas1.rect(10, 50, 100, 200);
		canvas1.fill();

		canvas2.setFillStyle(new Color(100, 100, 200, 0.8f));
		canvas2.rect(30, 20, 200, 150);
		canvas2.fill();

		mainPanel.add(canvas1, 0, 0);
		mainPanel.add(canvas2, 0, 0);

		initWidget(mainPanel);
	}

	private int pos = 0;

	public void move() {
		mainPanel.clear();
		pos += 1;
		mainPanel.add(canvas1, 0, 0);
		mainPanel.add(canvas2, pos, pos);
	}

}
