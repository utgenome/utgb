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
// CSS.java
// Since: Jun 26, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link Style} is a helper class to edit CSS (Cascading Style Sheet) design
 * 
 * @author leo
 * 
 */
public class Style {
	public static final String CSS_BORDER = "border";
	public static final String CSS_PADDING = "padding";
	public static final String CSS_MARGIN = "margin";
	public static final String CSS_FONT_COLOR = "color";
	public static final String CSS_FONT_WEIGHT = "fontWeight";
	public static final String CSS_FONT_SIZE = "fontSize";
	public static final String CSS_FONT_FAMILY = "fontFamily";
	public static final String CSS_WHITESPACE = "whiteSpace";
	public static final String CSS_NOWRAP = "nowrap";
	public static final String CSS_BACKGROUND_IMAGE = "backgroundImage";
	public static final String CSS_BACKGROUND_REPEAT = "backgroundRepeat";
	public static final String CSS_BACKGROUND_POSITION = "backgroundPosition";
	public static final String CSS_BACKGROUND_COLOR = "backgroundColor";
	public static final String CSS_OVERFLOW_X = "overflowX";
	public static final String CSS_OVERFLOW_Y = "overflowY";
	public static final String OVERFLOW_AUTO = "auto";
	public static final String OVERFLOW_HIDDEN = "hidden";
	public static final String CSS_Z_INDEX = "zIndex";

	public static void visibilityHidden(Widget w) {
		set(w, "visibility", "hidden");
	}

	/**
	 * set the overlap position of the widget. Widgets with the higher z-index will be front on the display
	 * 
	 * @param w
	 *            the widget
	 * @param zIndex
	 */
	public static void zIndex(Widget w, int zIndex) {
		set(w, CSS_Z_INDEX, Integer.toString(zIndex));
	}

	public static void bold(Widget w) {
		set(w, CSS_FONT_WEIGHT, "bold");
	}

	public static void normal(Widget w) {
		set(w, CSS_FONT_WEIGHT, "normal");
	}

	public static void italic(Widget w) {
		set(w, CSS_FONT_WEIGHT, "italic");
	}

	public static void fontSize(Widget w, int pixelFontSize) {
		set(w, CSS_FONT_SIZE, pixelFontSize + "px");
	}

	public static void fontFamily(Widget w, String[] fontFamily) {
		set(w, CSS_FONT_FAMILY, StringUtil.join(fontFamily, ", "));
	}

	public static void fontFamily(Widget w, String fontFamily) {
		set(w, CSS_FONT_FAMILY, fontFamily);
	}

	public static void nowrap(Widget w) {
		set(w, CSS_WHITESPACE, "nowrap");
	}

	public static void preserveWhiteSpace(Widget w) {
		set(w, CSS_WHITESPACE, "pre");
	}

	public static void overflowHidden(Widget w) {
		set(w, "overflow", OVERFLOW_HIDDEN);
	}

	public static void disableScroll(Widget w) {
		hideHorizontalScrollBar(w);
		hideVerticalScrollBar(w);
	}

	public static void hideHorizontalScrollBar(Widget w) {
		set(w, CSS_OVERFLOW_X, OVERFLOW_HIDDEN);
	}

	public static void hideVerticalScrollBar(Widget w) {
		set(w, CSS_OVERFLOW_Y, OVERFLOW_HIDDEN);
	}

	/**
	 * @param w
	 * @param imageURL
	 *            background image url relative to the GWT module base URL
	 */
	public static void backgroundImage(Widget w, String imageURL) {
		// String url = GWT.getModuleBaseURL() + imageURL;
		set(w, CSS_BACKGROUND_IMAGE, "url(" + imageURL + ")");
	}

	public static void backgroundNoRepeat(Widget w) {
		set(w, CSS_BACKGROUND_REPEAT, "no-repeat");
	}

	public static void backgroundPosition(Widget w, String value) {
		set(w, CSS_BACKGROUND_POSITION, value);
	}

	public static void backgroundRepeat(Widget w) {
		set(w, CSS_BACKGROUND_REPEAT, "repeat");
	}

	public static void backgroundRepeatY(Widget w) {
		set(w, CSS_BACKGROUND_REPEAT, "repeat-y");
	}

	public static void backgroundRepeatX(Widget w) {
		set(w, CSS_BACKGROUND_REPEAT, "repeat-x");
	}

	public static void backgroundColor(Widget w, String color) {
		set(w, CSS_BACKGROUND_COLOR, color);
	}

	public static void fullWidth(Widget w) {
		w.setWidth("100%");
	}

	public static void fullHeight(Widget w) {
		w.setHeight("100%");
	}

	public static void fullSize(Widget w) {
		w.setSize("100%", "100%");
	}

	public static final String BORDER_SOLID = "solid";
	public static final String BORDER_DASHED = "dashed";
	public static final String BORDER_INSET = "inset";
	public static final String BORDER_OUTSET = "outset";
	public static final String COLOR_WHITE = "white";
	public static final String COLOR_RED = "red";
	public static final String COLOR_BLACK = "black";
	public static final String COLOR_GRAY = "gray";
	public static final String COLOR_SKYBLUE = "#99CCFF";

	/**
	 * @param w
	 * @param borderWidth
	 *            pixel width
	 * @param borderType
	 * @param color
	 */
	public static void border(Widget w, int borderWidth, String borderType, String color) {
		String borderStyle = StringUtil.joinWithWS(new String[] { borderWidth + "px", borderType, color });
		set(w, CSS_BORDER, borderStyle);
	}

	public static int TOP = 1;
	public static int BOTTOM = 1 << 1;
	public static int LEFT = 1 << 2;
	public static int RIGHT = 1 << 3;
	private static int[] _direction = { TOP, BOTTOM, LEFT, RIGHT };
	private static String[] _directionStr = { "Top", "Bottom", "Left", "Right" };

	/**
	 * @param w
	 * @param NEWSflag
	 * @param borderWidth
	 *            pixel width
	 * @param borderType
	 * @param color
	 */
	public static void border(Widget w, int NEWSflag, int borderWidth, String borderType, String color) {
		String borderStyle = StringUtil.joinWithWS(new String[] { borderWidth + "px", borderType, color });
		for (int i = 0; i < _direction.length; i++) {
			if ((NEWSflag & _direction[i]) != 0)
				set(w, CSS_BORDER + _directionStr[i], borderStyle);
		}
	}

	public static final String CSS_BORDER_COLLAPSE = "borderCollapse";

	public static void borderCollapse(Widget w) {
		set(w, CSS_BORDER_COLLAPSE, "collapse");
	}

	/**
	 * @param w
	 * @param paddingSize
	 */
	public static void padding(Widget w, int paddingSize) {
		set(w, CSS_PADDING, paddingSize + "px");
	}

	public static void padding(Widget w, int NEWSflag, int paddingSize) {
		for (int i = 0; i < _direction.length; i++) {
			if ((NEWSflag & _direction[i]) != 0)
				set(w, CSS_PADDING + _directionStr[i], paddingSize + "px");
		}
	}

	public static void margin(Widget w, int marginSize) {
		set(w, CSS_MARGIN, marginSize + "px");
	}

	public static void margin(Widget w, int NEWSflag, int marginSize) {
		for (int i = 0; i < _direction.length; i++) {
			if ((NEWSflag & _direction[i]) != 0)
				set(w, CSS_MARGIN + _directionStr[i], marginSize + "px");
		}
	}

	public static final String CSS_CURSOR = "cursor";
	public static final String CURSOR_MOVE = "move";
	public static final String CURSOR_AUTO = "auto";
	public static final String CURSOR_POINTER = "pointer";
	public static final String CURSOR_CROSSHAIR = "crosshair";
	public static final String CURSOR_RESIZE_E = "e-resize";
	public static final String CURSOR_RESIZE_N = "n-resize";
	public static final String CURSOR_RESIZE_SE = "se-resize";
	public static final String CURSOR_TEXT = "text";
	public static final String CURSOR_HELP = "help";
	public static final String CSS_DISPLAY = "display";
	public static final String DISPLAY_BLOCK = "block";

	public static void cursor(Widget w, String cursorType) {
		set(w, CSS_CURSOR, cursorType);
	}

	public static void fullBlock(Widget w) {
		set(w, CSS_DISPLAY, DISPLAY_BLOCK);
		fullWidth(w);
		fullHeight(w);
	}

	public static final String CSS_VERTICAL_ALIGN = "verticalAlign";

	public static void verticalAlign(Widget w, String value) {
		set(w, CSS_VERTICAL_ALIGN, value);
	}

	/**
	 * Set the position of the widget from the right border
	 * 
	 * @param w
	 * @param value
	 */
	public static void right(Widget w, String value) {
		set(w, "right", value);
	}

	/**
	 * Set the text alignment of the widget
	 * 
	 * @param w
	 * @param value
	 */
	public static void textAlign(Widget w, String value) {
		set(w, "textAlign", value);
	}

	public static void trimOverflowedText(Widget w) {
		set(w, "textOverflow", "ellipsis");
		set(w, "overflow", "hidden");
		set(w, "whiteSpace", "nowrap");
	}

	/**
	 * Make the background of the widget semi-transparent color
	 * 
	 * @param colorCode
	 *            Hex RGB color code
	 */
	public static void semiTransparentBackground(Widget w, String colorCode, float alpha) {
		backgroundImage(w, "utgb-core/transparent?color=" + colorCode + "&opacity=" + alpha);
	}

	/**
	 * Set CSS style
	 * 
	 * @param w
	 * @param cssPropertyName
	 * @param value
	 */
	public static void set(Widget w, String cssPropertyName, String value) {
		DOM.setStyleAttribute(w.getElement(), cssPropertyName, value);
	}

	/**
	 * Non-constractable
	 */
	private Style() {
	}

	public static void position(Widget w, String value) {
		set(w, "position", value);
	}

	public static void overflowAuto(Widget w) {
		set(w, CSS_OVERFLOW_X, "auto");
		set(w, CSS_OVERFLOW_Y, "auto");
	}

	public static void fontColor(Widget w, String color) {
		set(w, CSS_FONT_COLOR, color);
	}
}
