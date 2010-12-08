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
// AXTAlignment.java
// Since: 2010/11/26
//
//--------------------------------------
package org.utgenome.format.axt;

import java.io.IOException;
import java.io.Writer;

/**
 * Record in AXT format
 * 
 * @author leo
 * 
 */
public class AXTAlignment {

	public int num;
	public String s_chr;
	public int s_start;
	public int s_end;
	public String d_chr;
	public int d_start;
	public int d_end;
	public String strand;
	public int score;

	public String primaryAssembly;
	public String aligningAssembly;

	public void toAXT(Writer out) throws IOException {
		out.append(Integer.toString(num));
		out.append("\t");
		out.append(s_chr);
		out.append("\t");
		out.append(Integer.toString(s_start));
		out.append("\t");
		out.append(Integer.toString(s_end));
		out.append("\t");
		out.append(d_chr);
		out.append("\t");
		out.append(Integer.toString(d_start));
		out.append("\t");
		out.append(Integer.toString(d_end));
		out.append("\t");
		out.append(strand);
		out.append("\t");
		out.append(Integer.toString(score));
		out.append("\n");

		out.append(primaryAssembly);
		out.append("\n");
		out.append(aligningAssembly);
		out.append("\n");
		out.append("\n");
	}

	@Override
	public boolean equals(Object obj) {

		AXTAlignment other = AXTAlignment.class.cast(obj);
		if (num != other.num)
			return false;
		if (!s_chr.equals(other.s_chr))
			return false;
		if (s_start != other.s_start)
			return false;
		if (s_end != other.s_end)
			return false;
		if (!d_chr.equals(other.d_chr))
			return false;
		if (d_start != other.d_start)
			return false;
		if (d_end != other.d_end)
			return false;
		if (!primaryAssembly.equals(other.primaryAssembly))
			return false;
		if (!aligningAssembly.equals(other.aligningAssembly))
			return false;
		if (score != other.score)
			return false;

		return true;
	}

}
