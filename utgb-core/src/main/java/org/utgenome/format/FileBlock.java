/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// FileBlock.java
// Since: 2010/10/05
//
//--------------------------------------
package org.utgenome.format;

import java.io.File;

import org.xerial.lens.Lens;

/**
 * A block in a file
 * 
 * @author leo
 * 
 */
public class FileBlock {

	public final File file;
	public final int id;
	public final long offset;

	public FileBlock(File file, int id, long offset) {
		this.file = file;
		this.id = id;
		this.offset = offset;
	}

	@Override
	public String toString() {
		return Lens.toSilk(this);
	}

}
