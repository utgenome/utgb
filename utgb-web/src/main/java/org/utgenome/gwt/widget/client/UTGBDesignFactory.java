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
// utgb-widget Project
//
// UTGBIconFactory.java
// Since: Apr 24, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

/**
 * Icon generator.
 * 
 * Usage:
 * 
 * <pre>
 * UTGBDesignFactory iconFactory = new UTGBDesignFactory(UTGBImageBundle.class);
 * Icon icon = iconFactory.getCloseButton();
 * 
 * </pre>
 * 
 * @author leo
 * 
 */
public class UTGBDesignFactory {

	private String themeFolder = "theme/default/";

	public static final String TRACKFRAME_BACKGROUND = "trackframe-bg.png";

	private final UTGBImageBundle defaultImageBundle;

	public UTGBDesignFactory() {
		defaultImageBundle = GWT.<UTGBImageBundle> create(UTGBImageBundle.class);
	}

	public UTGBDesignFactory(UTGBImageBundle imageBundle) {
		this.defaultImageBundle = imageBundle;
	}

	public UTGBImageBundle getUTGBImageBundle() {
		return defaultImageBundle;
	}

	private Image image(ImageResource resource, String title) {
		Image image = new Image(resource);
		image.setTitle(title);
		return image;
	}

	private Image image(ImageResource resource) {
		return new Image(resource);
	}

	public Icon getCloseButton() {
		return new Icon(image(defaultImageBundle.trackCloseIcon()), image(defaultImageBundle.trackCloseMouseOverIcon(), "close"));
	}

	public Icon getConfigButton() {
		return new Icon(image(defaultImageBundle.trackConfigcon()), image(defaultImageBundle.trackConfigMouseOverIcon(), "configuration"));
	}

	public Icon getAdjustHightButton() {
		return new Icon(image(defaultImageBundle.trackAdjustHightIcon()), image(defaultImageBundle.trackAdjustHightMouseOverIcon(), "adjust height"));
	}

	public Icon getFixedHightButton() {
		return new Icon(image(defaultImageBundle.trackFixedHightIcon()), image(defaultImageBundle.trackFixedHightMouseOverIcon(), "fixed height"));
	}

	public Icon getReloadButton() {
		return new Icon(image(defaultImageBundle.trackReloadIcon()), image(defaultImageBundle.trackReloadMouseOverIcon(), "reload"));
	}

	public Icon getOpenButton() {
		return new Icon(image(defaultImageBundle.trackOpenIcon()), image(defaultImageBundle.trackOpenMouseOverIcon(), "open"));
	}

	public Icon getHideButton() {
		return new Icon(image(defaultImageBundle.trackHideIcon()), image(defaultImageBundle.trackHideMouseOverIcon(), "hide"));
	}

	public Switch getOpenHideSwith() {
		return new Switch(getOpenButton(), getHideButton());
	}

	public Switch getAdjustHightSwitch() {
		return new Switch(getFixedHightButton(), getAdjustHightButton());
	}

	public Icon getTabCloseButton() {
		return new Icon(image(defaultImageBundle.tabCloseIcon()), image(defaultImageBundle.tabCloseMouseOverIcon(), "close tab"));
	}

	public NowLoadingIcon getNowLoadingIcon() {
		Image loadingImage = new Image("theme/default/trackLoading.gif");
		loadingImage.setTitle("loading");
		return new NowLoadingIcon(loadingImage, getReloadButton());
	}

	public static String getWindowBorderColor() {
		return "74DAFC";
	}

	public static String getWindowBorderColorDark() {
		return "54BADC";
	}

	/**
	 * Get the path to the image file for the selected theme
	 * 
	 * @param file
	 * @return
	 */
	public String getImagePath(String file) {
		return themeFolder + file;
	}

}
