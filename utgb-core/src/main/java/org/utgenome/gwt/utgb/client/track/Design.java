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
// Design.java
// Since: Jun 20, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

import java.util.HashMap;

import org.utgenome.gwt.utgb.client.ui.IconImage;

import com.google.gwt.user.client.ui.Image;

/**
 * Design class gathers image file names
 * 
 * @author leo
 * 
 */
public class Design {

	public static int ICON_UNKNOWN = 0;
	public static int ICON_CONFIG = 1;
	public static int ICON_PACK = 2;
	public static int ICON_UNPACK = 3;
	public static int ICON_HIDE = 4;
	public static int ICON_SHOW = 5;
	public static int ICON_CLOSE = 6;
	public static int TRACK_BORDER_V = 101;
	public static int TRACK_BORDER_H = 102;

	public static String PREFIX = "theme/image/";

	public static String IMAGE_NOT_AVAILABLE = PREFIX + "na.png";
	public static String IMAGE_RULER_TICK = PREFIX + "ruler-tick.gif";
	public static String IMAGE_TRANSPARENT = PREFIX + "transparent.gif";
	public static String IMAGE_DELETE_BUTTON = PREFIX + "tree_closed.gif";
	public static String IMAGE_NOW_LOADING = PREFIX + "nowloading.gif";

	private static HashMap<Integer, IconImage> _iconTable = new HashMap<Integer, IconImage>();

	static {
		// prefetch images
		Image.prefetch(IMAGE_NOT_AVAILABLE);
		Image.prefetch(IMAGE_RULER_TICK);
		Image.prefetch(IMAGE_TRANSPARENT);
		Image.prefetch(IMAGE_DELETE_BUTTON);
		Image.prefetch(IMAGE_NOW_LOADING);

		// track frame icon
		addIcon(ICON_UNKNOWN, new IconImage(PREFIX + "transparent.gif", PREFIX + "transparent.gif"));
		addIcon(ICON_CONFIG, new IconImage(PREFIX + "track-config.gif", PREFIX + "track-config-w.gif"));
		addIcon(ICON_PACK, new IconImage(PREFIX + "track-pack.gif", PREFIX + "track-pack-w.gif"));
		addIcon(ICON_UNPACK, new IconImage(PREFIX + "track-unpack.gif", PREFIX + "track-unpack.gif"));
		addIcon(ICON_HIDE, new IconImage(PREFIX + "track-hide.gif", PREFIX + "track-hide-w.gif"));
		addIcon(ICON_SHOW, new IconImage(PREFIX + "track-open.gif", PREFIX + "track-open-w.gif"));
		addIcon(ICON_CLOSE, new IconImage(PREFIX + "track-close.gif", PREFIX + "track-close-w.gif"));

		// track frame
		addIcon(TRACK_BORDER_V, new IconImage(PREFIX + "dragbar.png", PREFIX + "dragbar_active.png"));
		addIcon(TRACK_BORDER_H, new IconImage(PREFIX + "resizebar.png", PREFIX + "resizebar_active.png"));
	}

	public static void addIcon(int iconType, IconImage icon) {
		_iconTable.put(new Integer(iconType), icon);
	}

	/**
	 * Get the icon image URL
	 * 
	 * @param iconType
	 * @return the {@link IconImage} class
	 */
	public static IconImage getIconImage(int iconType) {
		Integer key = new Integer(iconType);
		if (_iconTable.containsKey(key))
			return _iconTable.get(key);
		else
			return _iconTable.get(new Integer(ICON_UNKNOWN));

	}

	/**
	 * Non constructable
	 */
	private Design() {
	}
}
