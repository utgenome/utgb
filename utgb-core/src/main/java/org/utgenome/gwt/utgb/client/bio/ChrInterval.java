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
// ChrInterval.java
// Since: 2011/05/31
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Interval in a chromosome
 * 
 * @author leo
 * 
 */
public class ChrInterval extends Interval {

	private static final long serialVersionUID = 1L;
	public String chr;

	public ChrInterval() {
	}

	public ChrInterval(String chr, Interval interval) {
		super(interval);
		this.chr = chr;
	}

	public ChrInterval(String chr, int start, int end) {
		super(start, end);
		this.chr = chr;
	}

	@Override
	public String toString() {
		return chr + ":" + start + "-" + "end";
	}
}
