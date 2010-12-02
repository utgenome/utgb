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
// SAMReadPairFragment.java
// Since: 2010/12/02
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Paired-end reads without the other mate data
 * 
 * @author leo
 * 
 */
public class SAMReadPairFragment extends Interval {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SAMReadLight oneEnd;
	public int mateStart;

	public SAMReadPairFragment() {
	}

	public SAMReadPairFragment(SAMReadLight oneEnd, int mateStart) {
		super(Math.min(oneEnd.unclippedStart, mateStart), Math.max(oneEnd.unclippedEnd, mateStart));
		this.oneEnd = oneEnd;
		this.mateStart = mateStart;
	}

	@Override
	public String getName() {
		return oneEnd.getName();
	}

	public Gap getGap() {
		if (mateStart > oneEnd.unclippedEnd) {
			return new Gap(oneEnd.unclippedEnd, mateStart);
		}
		else if (mateStart < oneEnd.unclippedStart) {
			return new Gap(mateStart, oneEnd.unclippedStart);
		}
		else
			return new Gap(-1, -1);
	}

	@Override
	public void accept(OnGenomeDataVisitor visitor) {
		visitor.visitSAMReadPairFragment(this);
	}
}
