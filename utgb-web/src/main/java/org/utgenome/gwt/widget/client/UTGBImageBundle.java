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
// UTGBImageBundle.java
// Since: Apr 24, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Graphic Resource list of UTGB Interface
 * 
 * @author leo
 * 
 */
public interface UTGBImageBundle extends ClientBundle {

	@Source("org/utgenome/gwt/widget/theme/default/disabledButton.gif")
	public ImageResource disabledIcon();

	@Source("org/utgenome/gwt/widget/theme/default/close-btn-on.gif")
	public ImageResource tabCloseMouseOverIcon();

	@Source("org/utgenome/gwt/widget/theme/default/close-btn.gif")
	public ImageResource tabCloseIcon();

	@Source("org/utgenome/gwt/widget/theme/default/resize.gif")
	public ImageResource windowResizeIcon();

	@Source("org/utgenome/gwt/widget/theme/default/wframe_l.gif")
	public ImageResource windowFrameLeftIcon();

	@Source("org/utgenome/gwt/widget/theme/default/wframe_c.gif")
	public ImageResource windowFrameCenterIcon();

	@Source("org/utgenome/gwt/widget/theme/default/wframe_r.gif")
	public ImageResource windowFrameRightIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackReload.gif")
	public ImageResource trackReloadIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackReloadW.gif")
	public ImageResource trackReloadMouseOverIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackClose.gif")
	public ImageResource trackCloseIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackCloseW.gif")
	public ImageResource trackCloseMouseOverIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackConfig.gif")
	public ImageResource trackConfigcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackConfigW.gif")
	public ImageResource trackConfigMouseOverIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackHide.gif")
	public ImageResource trackHideIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackHideW.gif")
	public ImageResource trackHideMouseOverIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackOpen.gif")
	public ImageResource trackOpenIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackOpenW.gif")
	public ImageResource trackOpenMouseOverIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackPack.gif")
	public ImageResource trackAdjustHightIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackPackW.gif")
	public ImageResource trackAdjustHightMouseOverIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackUnpack.gif")
	public ImageResource trackFixedHightIcon();

	@Source("org/utgenome/gwt/widget/theme/default/trackUnpackW.gif")
	public ImageResource trackFixedHightMouseOverIcon();

}
