/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// Frame.java
// Since: Feb 6, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bean.track;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Track frame settings
 * 
 * @author leo
 *
 */
public class Frame implements IsSerializable {
	private String style = "horizontal";
	private int height = 150;
	private boolean autoAdjust = false;
	
	public Frame() {
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isAutoAdjust() {
		return autoAdjust;
	}

	public void setAutoAdjust(boolean autoAdjust) {
		this.autoAdjust = autoAdjust;
	}
	
	
}




