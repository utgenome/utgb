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
// ChrLoc.java
// Since: 2009/10/02
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ChrLoc implements IsSerializable {
	public int start;
	public int end;
	public String chr;

	public ChrLoc() {
	}

	public ChrLoc(String chr, int start, int end) {
		this.chr = chr;
		this.start = start;
		this.end = end;
	}

	public ChrLoc getLocForPositiveStrand() {
		if (start < end)
			return new ChrLoc(chr, start, end);
		else
			return new ChrLoc(chr, end, start);
	}

	public int length() {
		return viewEnd() - viewStart();
	}

	public int viewStart() {
		if (isSense())
			return start;
		else
			return end;
	}

	public int viewEnd() {
		if (isSense())
			return end;
		else
			return start;
	}

	public boolean isSense() {
		return start <= end;
	}

	public boolean isAntiSense() {
		return !isSense();
	}

	@Override
	public String toString() {
		return chr + ":" + start + "-" + end;
	}
}
