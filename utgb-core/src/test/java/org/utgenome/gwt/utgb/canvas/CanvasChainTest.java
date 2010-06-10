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
// CanvasChainTest.java
// Since: Jun 10, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb.canvas;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.canvas.TrackWindowChain;
import org.utgenome.gwt.utgb.client.canvas.TrackWindowChain.WindowUpdateInfo;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

public class CanvasChainTest {

	private static Logger _logger = Logger.getLogger(CanvasChainTest.class);

	@Test
	public void chain() throws Exception {
		TrackWindowChain chain = new TrackWindowChain();
		TrackWindow view = new TrackWindow(800, 1, 1001);
		WindowUpdateInfo update = chain.setViewWindow(view);
		_logger.info(String.format("view: %s\n%s", view.toString(), Lens.toSilk(update)));

		update = chain.setViewWindow(view = view.scroll(500));
		_logger.info(String.format("view: %s\n%s", view.toString(), Lens.toSilk(update)));

		update = chain.setViewWindow(view = view.scroll(500));
		_logger.info(String.format("view: %s\n%s", view.toString(), Lens.toSilk(update)));

		update = chain.setViewWindow(view = view.scroll(500));
		_logger.info(String.format("view: %s\n%s", view.toString(), Lens.toSilk(update)));

		update = chain.setViewWindow(view = view.scroll(-500));
		_logger.info(String.format("view: %s\n%s", view.toString(), Lens.toSilk(update)));

		update = chain.setViewWindow(view = view.scroll(1000));
		_logger.info(String.format("view: %s\n%s", view.toString(), Lens.toSilk(update)));

		update = chain.setViewWindow(view = view.scroll(3000));
		_logger.info(String.format("view: %s\n%s", view.toString(), Lens.toSilk(update)));

	}

}
