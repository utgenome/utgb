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
// utgb-shell Project
//
// POM.java
// Since: Jan 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

/**
 * POM objects corresponds to a Maven project file (pom.xml)
 * 
 * @author leo
 * 
 */
public class POM {
	private String version = "(unknown)";
	private POMProperties properties = new POMProperties();

	public POM() {
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setProperties(POMProperties properties) {
		this.properties = properties;
	}

	public POMProperties getProperties() {
		return properties;
	}

	/**
	 * Retrieves Subversion's revision number from the pom.xml file
	 * 
	 * @return revision number
	 */
	public String getRevision() {
		return properties.getRevision();
	}
}
