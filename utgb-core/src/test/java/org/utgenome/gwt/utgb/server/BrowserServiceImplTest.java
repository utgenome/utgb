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
// utgb-core Project
//
// BrowserServiceImplTest.java
// Since: Sep 5, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.Alignment;
import org.utgenome.gwt.utgb.client.bio.AlignmentResult;
import org.xerial.lens.JSONLens;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class BrowserServiceImplTest {

	private static Logger _logger = Logger.getLogger(BrowserServiceImplTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void aligmentDataFormat() throws Exception {
		// alignment 1
		Alignment a1 = new Alignment();
		a1.setNumMatch(717);
		a1.setNumMisMatch(0);
		a1.setNumRepMatch(0);
		a1.setNumN(0);
		a1.setStrand("+");
		a1.setQueryName("query_seq");
		a1.setQueryLen(717);
		a1.setQueryStart(0);
		a1.setQueryEnd(717);
		a1.setTargetName("scaffold1");
		a1.setTargetSize(8220096);
		a1.setTargetStart(129488);
		a1.setTargetEnd(132262);
		a1.setBlockCount(4);

		a1.addBlockSizes(118);
		a1.addBlockSizes(108);
		a1.addBlockSizes(87);
		a1.addBlockSizes(404);

		a1.addQueryStarts(0);
		a1.addQueryStarts(118);
		a1.addQueryStarts(226);
		a1.addQueryStarts(313);

		a1.addTargetStarts(129488);
		a1.addTargetStarts(129919);
		a1.addTargetStarts(130752);
		a1.addTargetStarts(131858);

		// alignment 2
		Alignment a2 = new Alignment();
		a2.setNumMatch(161);
		a2.setNumMisMatch(8);
		a2.setNumRepMatch(0);
		a2.setNumN(0);
		a2.setStrand("-");
		a2.setQueryName("query_seq");
		a2.setQueryLen(717);
		a2.setQueryStart(400);
		a2.setQueryEnd(615);
		a2.setTargetName("scaffold21");
		a2.setTargetSize(4496770);
		a2.setTargetStart(3262261);
		a2.setTargetEnd(3262624);
		a2.setBlockCount(6);

		a2.addBlockSizes(80);
		a2.addBlockSizes(18);
		a2.addBlockSizes(27);
		a2.addBlockSizes(12);
		a2.addBlockSizes(11);
		a2.addBlockSizes(21);

		a2.addQueryStarts(103);
		a2.addQueryStarts(194);
		a2.addQueryStarts(212);
		a2.addQueryStarts(249);
		a2.addQueryStarts(279);
		a2.addQueryStarts(296);

		// 3262261,3262355,3262380,3262414,3262574,3262603
		a2.addTargetStarts(3262261);
		a2.addTargetStarts(3262355);
		a2.addTargetStarts(3262380);
		a2.addTargetStarts(3262414);
		a2.addTargetStarts(3262574);
		a2.addTargetStarts(3262603);

		ArrayList<Alignment> alignmentList = new ArrayList<Alignment>();
		alignmentList.add(a1);
		alignmentList.add(a2);

		String json = JSONLens.toJSON(alignmentList);
		_logger.debug(String.format("{\"alignment\":%s}", json));
	}

	@Test
	public void loadAlignmentJSON() throws Exception, IOException {
		AlignmentResult result = JSONLens.loadJSON(AlignmentResult.class, FileResource.open(BrowserServiceImpl.class, "alignment.json"));

		String json = JSONLens.toJSON(result);
		_logger.debug(json);

	}
}
