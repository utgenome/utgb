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
// FrameButtonListener.java
// Since: 2007/11/29
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

/**
 * Event handler interface when frame buttons are clicked
 * @author leo
 *
 */
public interface FrameButtonListener 
{
	/**
	 * When clicked the configuration button 
	 * @param frame 
	 */
	public void onClickConfig(Frame frame);
	/**
	 * When clicked the adjust button
	 * @param frame
	 */
	public void onClickAdjust(Frame frame);
	/**
	 * When clicked the minimize button
	 * @param frame
	 */
	public void onClickMinimize(Frame frame);
	/**
	 * When clicked the close button
	 * @param frame
	 */
	public void onClickClose(Frame frame);
}




