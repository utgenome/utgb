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
// ReadCoverage.java
// Since: 2010/05/25
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Read coverage histogram
 * 
 * @author leo
 * 
 */
public class ReadCoverage extends Interval {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name = "read coverage";
	/**
	 * Coverage of each pixel position
	 */
	public int[] coverage;
	public int pixelWidth;

	public int maxHeight = 0;
	public int minHeight = 0;

	public ReadCoverage() {
	}

	public ReadCoverage(int start, int end, int pixelWidth, int[] coverage) {
		super(start, end);
		this.pixelWidth = pixelWidth;
		this.coverage = coverage;

		for (int each : coverage) {
			if (maxHeight < each)
				maxHeight = each;
			if (minHeight > each)
				minHeight = each;
		}
	}

	@Override
	public void accept(GenomeRangeVisitor visitor) {
		visitor.visitReadCoverage(this);
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
