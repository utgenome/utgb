/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// ReadDepthTest.java
// Since: 2011/01/24
//
//--------------------------------------
package org.utgenome.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.Interval;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.util.ReadDepth.DepthOutput;
import org.xerial.util.log.Logger;

public class ReadDepthTest {

	private static Logger _logger = Logger.getLogger(ReadDepthTest.class);

	private class DepthReport implements DepthOutput {
		public int[] depth = new int[10];

		int cursor = -1;

		public DepthReport() {
			for (int i = 0; i < depth.length; ++i)
				depth[i] = 0;

		}

		public void reportDepth(String chr, int start, int end, int depth) throws Exception {
			Assert.assertTrue(start > cursor);
			_logger.info(String.format("%s:%d-%d depth: %d", chr, start, end, depth));
			for (int i = start; i < end; ++i)
				this.depth[i] = depth;

			cursor = start;
		}

	}

	@Test
	public void computeDepth() throws Exception {

		List<OnGenome> l = new ArrayList<OnGenome>();
		l.add(new Interval(1, 3));
		l.add(new Interval(4, 7));
		l.add(new Interval(4, 6));

		DepthReport d = new DepthReport();
		ReadDepth.compute("chr1", l.iterator(), d);

		int[] answer = new int[] { 0, 1, 1, 0, 2, 2, 1, 0, 0, 0 };
		Assert.assertArrayEquals(answer, d.depth);

	}

}
