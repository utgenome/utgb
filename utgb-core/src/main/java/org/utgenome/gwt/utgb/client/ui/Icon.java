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
// Icon.java
// Since: Jun 21, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Image;

public class Icon extends Image implements MouseOutHandler, MouseOverHandler {
	IconImage _icon;

	public Icon(IconImage icon) {
		super(icon.getImageURL());
		_icon = icon;

		addMouseOutHandler(this);
		addMouseOverHandler(this);
	}

	public void setIcon(IconImage newIcon) {
		_icon = newIcon;
		setUrl(_icon.getImageURL());
	}

	public void onMouseOut(MouseOutEvent arg0) {
		setUrl(_icon.getImageURL());
	}

	public void onMouseOver(MouseOverEvent arg0) {
		setUrl(_icon.getMouseOverImageURL());
	}

}
