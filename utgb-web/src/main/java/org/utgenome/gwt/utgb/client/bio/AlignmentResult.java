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
// AlignmentResult.java
// Since: Sep 5, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AlignmentResult implements IsSerializable {

	private ArrayList<Alignment> alignmentList = new ArrayList<Alignment>();
	private ArrayList<String> prettyAlignmentList = new ArrayList<String>();

	public AlignmentResult() {

	}

	public void addAlignment(Alignment alignment) {
		alignmentList.add(alignment);
	}

	public void addPrettyAlignment(String prettyAlignment) {
		prettyAlignmentList.add(prettyAlignment);
	}

	public ArrayList<Alignment> getAlignment() {
		return alignmentList;
	}

	public ArrayList<String> getPrettyAlignment() {
		return prettyAlignmentList;
	}

}
