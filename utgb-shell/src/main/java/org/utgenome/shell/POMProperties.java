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
// POMProperties.java
// Since: May 21, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

/**
 * This class is used for retrieving the revision value described in the pom.xml file
 * 
 * @author leo
 * 
 */
public class POMProperties {
	private String	revision	= "(unknown)";

	public POMProperties() {}

	public void setRevision(String revision) {
		if (revision != null && revision.length() > 1)
			this.revision = revision.substring(1, revision.length() - 1).trim();
	}

	public String getRevision() {
		return revision;
	}
}
