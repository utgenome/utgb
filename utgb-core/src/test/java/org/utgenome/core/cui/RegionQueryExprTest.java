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
// utgb-shell Project
//
// RegionQueryExprTest.java
// Since: 2011/01/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.core.cui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.shell.UTGBShellException;
import org.xerial.util.log.Logger;

public class RegionQueryExprTest {
	private static Logger _logger = Logger.getLogger(RegionQueryExprTest.class);

	public void verify(String chr, int s, int e) throws UTGBShellException {
		String expr = String.format("%s:%,d-%,d", chr, s, e);
		_logger.trace(expr);
		ChrLoc loc = RegionQueryExpr.parse(expr);
		assertEquals(chr, loc.chr);
		assertEquals(s, loc.start);
		assertEquals(e, loc.end);
	}

	@Test
	public void parse() throws Exception {
		verify("chr1", 1, 1000);
		verify("chr1", 10000000, 2000000000);
		verify("chr3", 100000, 2000000);
	}
}
