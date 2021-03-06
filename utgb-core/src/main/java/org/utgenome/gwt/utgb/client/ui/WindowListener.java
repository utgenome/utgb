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
// WindowListener.java
// Since: Jun 27, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

public interface WindowListener {

	/**
	 * An event handler when a window buton is clicked 
	 * @param window sender 
	 * @param buttonType one of {@link WindowBox#BUTTON_CONFIG}, {@link WindowBox#BUTTON_PACK}, {@link WindowBox#BUTTON_MINIMIZE}, {@link WindowBox#BUTTON_CLOSE}
	 */
	public void onButtonClick(WindowBox window, int buttonType);
	
	public void onResizeWindow(WindowBox window, int newWindowSize);

}




