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
// IconImage.java
// Since: Jun 21, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import com.google.gwt.user.client.ui.Image;

/**
 * IconImage image URL holder
 * @author leo
 *
 */
public class IconImage {
	private String	_defaultImage;
	private String	_onMouseImage;

	public IconImage(String defaultImage, String onMouseImage) {
		this._defaultImage = defaultImage;
		this._onMouseImage = onMouseImage;
		
		// prefetch the images (which is required especially for IE)
		Image.prefetch(_defaultImage);
		Image.prefetch(_onMouseImage);
	}

	public String getImageURL()
	{
		return _defaultImage;
	}
	
	public String getMouseOverImageURL() {
		return _onMouseImage;
	}
}



