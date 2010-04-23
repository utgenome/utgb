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
// RibbonBrowser.java
// Since: 2010/01/27
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.ribbon.client;

import org.utgenome.gwt.ribbon.client.ui.WeaverCanvas;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class RibbonBrowser implements EntryPoint {

	public void onModuleLoad() {

		final WeaverCanvas wc = new WeaverCanvas();

		RootPanel.get("utgb-main").add(wc);

		Animation a = new Animation() {

			@Override
			protected void onUpdate(double arg0) {
				wc.move();
			}
		};

		a.run(1000);

	}
}
