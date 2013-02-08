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
// SEGRecord.java
// Since: 2011/07/03
//
//--------------------------------------
package org.utgenome.gwt.utgb.client.bean.track;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * Available parameter in the track
 * 
 * @author mkasa
 * 
 */
public class SEGRecord implements IsSerializable {
	private int start;
	private int end;
	private String id;
	private String data;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

}




