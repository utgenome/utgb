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
// TrackFrameState.java
// Since: Jul 23, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

public class TrackFrameState {
	private boolean isPacked = true;
	private boolean isMinimized = false;
	private int minFrameHeight = DEFAULT_MIN_TRACKFRAME_HEIGHT;
	private int previousFrameHeight = DEFAULT_MIN_TRACKFRAME_HEIGHT;
	private int currentHeight;
		
	public static final int	DEFAULT_MIN_TRACKFRAME_HEIGHT	= 18;

	public boolean isPacked() {
		return isPacked;
	}

	public void setPacked(boolean isPacked) {
		this.isPacked = isPacked;
	}

	public boolean isMinimized() {
		return isMinimized;
	}

	public void setMinimized(boolean isMinimized) {
		this.isMinimized = isMinimized;
	}

	public int getMinFrameHeight() {
		return minFrameHeight;
	}

	public void setMinFrameHeight(int minFrameHeight) {
		this.minFrameHeight = Math.max(minFrameHeight, DEFAULT_MIN_TRACKFRAME_HEIGHT);
	}

	public int getPreviousFrameHeight() {
		return previousFrameHeight;
	}

	public void setPreviousFrameHeight(int previousFrameHeight) {
		this.previousFrameHeight = Math.max(minFrameHeight, previousFrameHeight);
	}
	
	
	public int resizeFrameHeight(int height)
	{
		int newHeight = height;
		if(isMinimized()) 
		{
			if(height < DEFAULT_MIN_TRACKFRAME_HEIGHT)
				newHeight = DEFAULT_MIN_TRACKFRAME_HEIGHT;
		}
		else
		{
			if(height < minFrameHeight)
				newHeight = minFrameHeight;
		}
		return newHeight;
	}

	
}




