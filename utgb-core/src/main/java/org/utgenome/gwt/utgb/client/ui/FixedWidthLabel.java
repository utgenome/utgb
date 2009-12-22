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
// FixedWidthLabel.java
// Since: Nov 30, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.user.client.ui.Label;

/**
 * A text label that fits a specified with
 * @author leo
 *
 */
public class FixedWidthLabel extends Label
{
	private int width;
	private String originalText = "";

	public FixedWidthLabel(int width) {
		this("", width);
	}
	
	public FixedWidthLabel(String text, int width)
	{
		super(text);
		this.originalText = text;
		this.width = width;
		
		CSS.nowrap(this);
	}
	
	

	protected void onLoad() {
		adjustTextSize();
	}
	

	protected void adjustTextSize()
	{
		int currentWidth = getOffsetWidth();
		if(currentWidth > width)
		{
			// retrieves the currently displayed text
			String currentText = super.getText();
			int length = currentText.length();

			length /= 2;
			if(length < 0)
				length = 0;
			String shrinkedText = currentText.substring(0, length) + "...";
			if(!shrinkedText.equals(currentText))
			{
				// change the label text
				super.setText(shrinkedText);
				adjustTextSize();
			}
		}
	}
	
	public void setText(String text) {
		originalText = text;
		super.setText(text);
		adjustTextSize();
	}
	
	public String getText() {
		return originalText;
	}
	
}




