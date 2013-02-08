/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// ColorUtil.java
// Since: 2010/10/08
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import com.google.gwt.widgetideas.graphics.client.Color;

/**
 * Color code generator
 * 
 * @author leo
 * 
 */
public class ColorUtil {

	public static Color toColor(String hex) {
		if (hex.startsWith("#"))
			hex = hex.substring(1);

		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		Color c = new Color(r, g, b);
		return c;
	}

	public static Color toColor(String hex, float alpha) {
		if (hex.startsWith("#"))
			hex = hex.substring(1);

		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		return new Color(r, g, b, alpha);
	}

}
