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
// UTGBCommandBase.java
// Since: 2011/03/23
//
//--------------------------------------
package org.utgenome.core.cui;

import java.net.URL;

import org.xerial.util.FileResource;
import org.xerial.util.opt.Command;

public abstract class UTGBCommandBase implements Command {

	public Object getOptionHolder() {
		return this;
	}

	public URL getHelpMessageResource() {
		return FileResource.find(this.getClass().getPackage(), String.format("help-%s.txt", name()));
	}

	public abstract void execute(String[] args) throws Exception;

	public abstract String getOneLineDescription();

	public abstract String name();

}
