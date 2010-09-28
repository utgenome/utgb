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
// CanvasChain.java
// Since: Jun 4, 2010
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.utgenome.gwt.utgb.client.track.TrackWindow;

/**
 * A chain of {@link TrackWindow}s for supporting GoogleMap-style graphic drawing.
 * 
 * <pre>
 *              (V: view window) 
 *               |----------|
 * |--------||--------||--------||--------|
 *    (w1)      (w2)      (w3)     (w4:prefetch)
 *     |--------------------------------|
 *         (G: global view, 3V size) 
 *                                
 *                                
 * After scrolling to right:                               
 * 
 *                       (V: view window) 
 *                         |-----------|
 * |--------||--------||--------||--------||--------|
 *  (w1:discard) (w2)     (w3)      (w4)       (w5:prefetch) 
 *             |----------------------------------|
 *                  (G: global view, 3V size)
 * </pre>
 * 
 * @author leo
 * 
 */
public class TrackWindowChain {

	private ArrayList<TrackWindow> windowList = new ArrayList<TrackWindow>();

	public TrackWindowChain() {

	}

	private TrackWindow viewWindow;
	private TrackWindow globalWindow;

	private int PREFETCH_FACTOR = 1; // (left) f*V + (current) V + (right) f*V = 3V (when f=1)

	public static class WindowUpdateInfo {
		public final List<TrackWindow> windowToCreate;
		public final List<TrackWindow> windowToDiscard;

		private WindowUpdateInfo(List<TrackWindow> windowToCreate, List<TrackWindow> windowToDiscard) {
			this.windowToCreate = windowToCreate;
			this.windowToDiscard = windowToDiscard;
		}
	}

	public void clear() {
		windowList.clear();
	}

	public void setPrefetchFactor(int factor) {
		this.PREFETCH_FACTOR = factor;
	}

	public List<TrackWindow> getTrackWindowList() {
		return windowList;
	}

	public TrackWindow getGlobalWindow() {
		return globalWindow;
	}

	public TrackWindow getViewWindow() {
		return viewWindow;
	}

	public WindowUpdateInfo setViewWindow(TrackWindow view) {

		final int factor = PREFETCH_FACTOR * 2 + 1;
		final int viewSize = view.getSequenceLength();
		final int viewExtensionDirection = view.isReverseStrand() ? -1 : 1;
		int gvStart = view.getStartOnGenome() - viewSize * PREFETCH_FACTOR * viewExtensionDirection;
		int gvEnd = view.getEndOnGenome() + viewSize * PREFETCH_FACTOR * viewExtensionDirection;
		this.globalWindow = new TrackWindow(view.getPixelWidth() * factor, gvStart, gvEnd);

		ArrayList<TrackWindow> windowToPreserve = new ArrayList<TrackWindow>();
		ArrayList<TrackWindow> windowToDiscard = new ArrayList<TrackWindow>();

		if (viewWindow != null && viewWindow.hasSameScaleWith(view)) {
			// scroll
			// update the window list
			for (TrackWindow each : windowList) {
				// discard the windows that do not overlap with the global window 
				if (each.overlapWith(globalWindow)) {
					windowToPreserve.add(each);
				}
				else {
					windowToDiscard.add(each);
				}
			}
		}
		else {
			windowToDiscard.addAll(windowList);
		}
		this.viewWindow = view;

		// compute the missing window list
		ArrayList<TrackWindow> newWindowList = new ArrayList<TrackWindow>();

		// sort the windows by the view start order
		Collections.sort(windowToPreserve);
		int gridStartOnGenome = windowToPreserve.isEmpty() ? view.getViewStartOnGenome() : windowToPreserve.get(0).getViewStartOnGenome();
		while (gridStartOnGenome > globalWindow.getViewStartOnGenome()) {
			gridStartOnGenome -= viewSize;
		}
		while (gridStartOnGenome < globalWindow.getViewEndOnGenome()) {
			TrackWindow grid;
			if (view.isPositiveStrand()) {
				grid = view.newWindow(gridStartOnGenome, gridStartOnGenome + viewSize);
			}
			else {
				grid = view.newWindow(gridStartOnGenome + viewSize, gridStartOnGenome);
			}

			if (!windowToPreserve.contains(grid)) {
				newWindowList.add(grid);
			}

			gridStartOnGenome += viewSize;
		}

		windowList.clear();
		windowList.addAll(windowToPreserve);
		windowList.addAll(newWindowList);

		return new WindowUpdateInfo(newWindowList, windowToDiscard);
	}

}
