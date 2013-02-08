/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// GenomeBrowser Project
//
// OperationImpl.java
// Since: 2007/06/14
//
// $URL$ 
// $Author$ ssksn
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.operation;

import org.utgenome.gwt.utgb.client.track.Track;

public abstract class OperationImpl implements Operation {
	protected Track track;

	protected OperationImpl(final Track track) {
		this.track = track;
	}

	protected void setTrack(final Track track) {
		this.track = track;
	}

	protected Track getTrack() {
		return track;
	}

}
