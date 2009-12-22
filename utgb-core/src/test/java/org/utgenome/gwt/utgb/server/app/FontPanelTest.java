/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// FontPanelTest.java
// Since: Jul 15, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.server.app.FontPanel.FontInfo;
import org.xerial.util.Pair;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

public class FontPanelTest {

	private static Logger _logger = Logger.getLogger(FontPanelTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFontInfo() throws IOException {
		List<String> stat = new ArrayList<String>();

		for (float i = 1; i <= 30; i += 0.5) {
			Pair<FontInfo, Font> fontInfo = FontPanel.getFontInfo(i);
			FontInfo fi = fontInfo.getFirst();
			stat.add(String.format("-font(size:%s, width:%s, height:%s, baseline:%s)", fi.size, fi.width, fi.height, fi.baseLine));
		}

		_logger.info("\n" + StringUtil.join(stat, "\n"));

	}

}
