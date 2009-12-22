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
// ImportStmt.java
// Since: Jan 14, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.config;

/**
 * This is the import statement in the track-config.xml, which is used to load 
 * a UTGB action package from the class loader.
 * 
 * @author leo
 *
 */
public class ImportStmt {
	private String actionPackage = null;
	private String alias = "";
	
	public ImportStmt() {
	}

	public String getActionPackage() {
		return actionPackage;
	}

	public void setActionPackage(String actionPackage) {
		this.actionPackage = actionPackage;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

		
}




