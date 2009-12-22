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

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * A {@link ImageBundle} of UTGB Images
 * 
 * @author leo
 * 
 */
public interface UTGBImageBundle extends ImageBundle {

	@Resource("org/utgenome/gwt/widget/theme/default/disabledButton.gif")
	public AbstractImagePrototype disabledIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/close-btn-on.gif")
	public AbstractImagePrototype tabCloseMouseOverIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/close-btn.gif")
	public AbstractImagePrototype tabCloseIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/resize.gif")
	public AbstractImagePrototype windowResizeIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/wframe_l.gif")
	public AbstractImagePrototype windowFrameLeftIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/wframe_c.gif")
	public AbstractImagePrototype windowFrameCenterIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/wframe_r.gif")
	public AbstractImagePrototype windowFrameRightIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackReload.gif")
	public AbstractImagePrototype trackReloadIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackReloadW.gif")
	public AbstractImagePrototype trackReloadMouseOverIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackClose.gif")
	public AbstractImagePrototype trackCloseIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackCloseW.gif")
	public AbstractImagePrototype trackCloseMouseOverIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackConfig.gif")
	public AbstractImagePrototype trackConfigcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackConfigW.gif")
	public AbstractImagePrototype trackConfigMouseOverIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackHide.gif")
	public AbstractImagePrototype trackHideIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackHideW.gif")
	public AbstractImagePrototype trackHideMouseOverIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackOpen.gif")
	public AbstractImagePrototype trackOpenIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackOpenW.gif")
	public AbstractImagePrototype trackOpenMouseOverIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackPack.gif")
	public AbstractImagePrototype trackAdjustHightIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackPackW.gif")
	public AbstractImagePrototype trackAdjustHightMouseOverIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackUnpack.gif")
	public AbstractImagePrototype trackFixedHightIcon();

	@Resource("org/utgenome/gwt/widget/theme/default/trackUnpackW.gif")
	public AbstractImagePrototype trackFixedHightMouseOverIcon();

}
