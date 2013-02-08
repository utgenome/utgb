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
// Layer.java
// Since: Feb 6, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bean.track;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Layer descriptor of the track
 * 
 * @author leo
 * 
 */
public class Layer implements IsSerializable {

	private String type;
	private String name;
	private String baseURL;
	private Frame frame;
	/**
	 */
	private ArrayList<Parameter> paramList = new ArrayList<Parameter>();
	/**
	 */
	private ArrayList<HiddenParameter> hiddenParamList = new ArrayList<HiddenParameter>();

	public Layer() {

	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public Frame getFrame() {
		return frame;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}

	public ArrayList<Parameter> getParamList() {
		return paramList;
	}

	public ArrayList<HiddenParameter> getHiddenParamList() {
		return hiddenParamList;
	}

	public void addParam(Parameter param) {
		paramList.add(param);
	}

	public void addHidden(HiddenParameter hidden) {
		hiddenParamList.add(hidden);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
