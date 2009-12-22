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
// Content.java
// Since: Feb 6, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bean.track;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Content descriptor in the track
 * 
 * @author leo
 * 
 */
public class Content implements IsSerializable {

	private String type = "utgb";

	/**
	 */
	private ArrayList<Species> speciesList = new ArrayList<Species>();

	/**
	 */
	private ArrayList<HiddenParameter> hiddenParameterList = new ArrayList<HiddenParameter>();

	/**
	 */
	private ArrayList<Parameter> parameterList = new ArrayList<Parameter>();

	/**
	 */
	private ArrayList<Layer> layerList = new ArrayList<Layer>();

	public Content() {
	}

	public void addSpecies(Species species) {
		speciesList.add(species);
	}

	public void addHidden(HiddenParameter hidden) {
		hiddenParameterList.add(hidden);
	}

	public void addParam(Parameter param) {
		parameterList.add(param);
	}

	public void addLayer(Layer layer) {
		layerList.add(layer);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Species> getSpeciesList() {
		return speciesList;
	}

	public ArrayList<HiddenParameter> getHiddenParameterList() {
		return hiddenParameterList;
	}

	public ArrayList<Parameter> getParameterList() {
		return parameterList;
	}

	public ArrayList<Layer> getLayerList() {
		return layerList;
	}

}
