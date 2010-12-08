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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.canvas.TrackWindowChain;
import org.utgenome.gwt.utgb.client.canvas.TrackWindowChain.WindowUpdateInfo;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.xerial.lens.SilkLens;
import org.xerial.util.log.Logger;

public class TrackWindowChainTest {

	private static Logger _logger = Logger.getLogger(TrackWindowChainTest.class);

	public void verifyChain(TrackWindow view, TrackWindowChain chain) {

		ArrayList<TrackWindow> w = new ArrayList<TrackWindow>(chain.getTrackWindowList());
		Collections.sort(w);

		// contiguousness test
		if (w.isEmpty())
			return;
		int s = w.get(0).getViewStartOnGenome();
		int e = w.get(0).getViewEndOnGenome();
		for (int i = 1; i < w.size(); ++i) {
			TrackWindow next = w.get(i);
			assertEquals(e, next.getViewStartOnGenome());
			e = next.getViewEndOnGenome();
		}

		// containment test
		TrackWindow globalView = view.newWindow(s, e);
		assertTrue(globalView.contains(view));

	}

	@Test
	public void chain() throws Exception {
		TrackWindowChain chain = new TrackWindowChain();
		TrackWindow view = new TrackWindow(800, 1, 1001);
		WindowUpdateInfo update = chain.setViewWindow(view);
		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		verifyChain(view, chain);

		update = chain.setViewWindow(view = view.scroll(500));
		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		verifyChain(view, chain);

		update = chain.setViewWindow(view = view.scroll(500));
		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		verifyChain(view, chain);

		update = chain.setViewWindow(view = view.scroll(500));
		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		verifyChain(view, chain);

		update = chain.setViewWindow(view = view.scroll(-500));
		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		verifyChain(view, chain);

		update = chain.setViewWindow(view = view.scroll(1000));
		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		verifyChain(view, chain);

		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		update = chain.setViewWindow(view = view.scroll(3000));
		verifyChain(view, chain);

		update = chain.setViewWindow(new TrackWindow(view.getPixelWidth(), view.getStartOnGenome() - 1000, view.getEndOnGenome() + 1000));
		_logger.debug(String.format("view: %s\n%s", view.toString(), SilkLens.toSilk(update)));
		verifyChain(view, chain);

	}

}
