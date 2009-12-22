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
// HandlerName.java
// Since: Jul 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server;

import java.util.ArrayList;
import java.util.List;

/**
 * RequestURI information
 * 
 * @author leo
 * 
 */
public class RequestURI {

	private String uri;
	private String suffix = "";
	private String fullName = null;

	public RequestURI(String uri) {
		this.uri = uri;
		int dotIndex = uri.lastIndexOf(".");
		if (dotIndex > 0) {
			suffix = uri.substring(dotIndex + 1);
			fullName = uri.substring(0, dotIndex);
		}
		else
			fullName = uri;

		assert (fullName != null);
	}

	/**
	 * Has suffix in the action request? e.g., .xml, .json, etc.
	 * 
	 * @return
	 */
	public boolean hasSuffix() {
		return suffix != null;
	}

	/**
	 * Get the suffix of the action request
	 * 
	 * @return
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Return the path component. For example, if the request is hello/world.json, its component list is ["hello",
	 * "world"]
	 * 
	 * @return the path component list
	 */
	public List<String> getPathComponentList() {
		ArrayList<String> result = new ArrayList<String>();
		String[] component = fullName.split("\\/");

		if (component == null) {
			result.add(fullName);
		}
		else {
			for (String c : component)
				result.add(c);
		}

		return result;

	}

	/**
	 * Get the leaf name of the action request
	 * 
	 * @return
	 */
	public String getName() {
		int leafIndex = fullName.lastIndexOf("/");
		if (leafIndex > 0)
			return fullName.substring(leafIndex + 1).replaceAll("/", ".");
		else
			return fullName.replaceAll("/", ".");
	}

	public String getHandlerName() {
		return fullName.replaceAll("/", ".");
	}

	/**
	 * Get the full path of the action request
	 * 
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Get the original URI
	 * 
	 * @return
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * Get the prefix before the handler name
	 * 
	 * @return
	 */
	public String getPrefix() {
		int leafIndex = fullName.lastIndexOf("/");
		if (leafIndex > 0) {
			return "/" + fullName.substring(0, leafIndex);
		}
		else
			return "/";
	}

	@Override
	public String toString() {
		return String.format("%s (prefix=%s, suffix=%s)", getName(), getPrefix(), suffix);
	}

}
