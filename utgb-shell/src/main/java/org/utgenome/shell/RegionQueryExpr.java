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
// RegionQueryExpr.java
// Since: 2011/01/06
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;

public class RegionQueryExpr {

	private static Pattern p = Pattern.compile("([^:]+)(:([0-9]+)-([0-9]+))?");

	public static ChrLoc parse(String expr) throws UTGBShellException {
		Matcher m = p.matcher(expr.replaceAll(",", "")); // remove comma
		if (!m.matches())
			throw new UTGBShellException("invalid query format:" + expr);
		String chr = m.group(1);
		String sStart = m.group(3);
		String sEnd = m.group(4);

		int start = 0;
		if (sStart != null)
			start = Integer.parseInt(sStart);
		int end = Integer.MAX_VALUE;
		if (sEnd != null)
			end = Integer.parseInt(sEnd);

		return new ChrLoc(chr, start, end);
	}
}
