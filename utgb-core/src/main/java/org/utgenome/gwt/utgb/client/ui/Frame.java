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
// Window.java
// Since: 2007/11/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.user.client.ui.Widget;

/**
 * Frame interface for Window, Track, Tab, etc.
 * @author leo
 *
 */
public interface Frame {

    public static int BUTTON_CONFIG = 1;
    public static int BUTTON_ADJUST = 1 << 1;
    public static int BUTTON_MINIMIZE = 1 << 2;
    public static int BUTTON_CLOSE = 1 << 3;

    public void setConfigurable(boolean enable);
	public void setAdjustable(boolean enable);
	public void setMinimizable(boolean enable);
	public void setClosable(boolean enable);
	public void setVerticallyRisizable(boolean enable);
	public void setHorizontallyRisizable(boolean enable);
	
	public boolean isConfigurable();
	public boolean isAdjustable();
	public boolean isMinimizable();
	public boolean isClosable();
	public boolean isVerticallyResizable();
	public boolean isHorizontallyResizable();
	
	public void setSize(int width, int height);
	public void setWidth(int width);
	public void setHeight(int height);
	
	public void setVisible(boolean visible);
	
	public Widget getTitleBar();

	
}




