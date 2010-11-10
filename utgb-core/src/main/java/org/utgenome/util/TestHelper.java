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
// TestHelper.java
// Since: 2010/10/20
//
//--------------------------------------
package org.utgenome.util;

import java.io.File;
import java.io.IOException;

import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;

/**
 * Utilities for writing JUnit code
 * 
 * @author leo
 * 
 */
public class TestHelper {

	/**
	 * Create a temporary file
	 * 
	 * @param <T>
	 * @param referenceClass
	 * @param srcFileName
	 * @return
	 * @throws IOException
	 */
	public static <T> File createTempFileFrom(Class<T> referenceClass, String srcFileName) throws IOException {
		File tmp = FileUtil.createTempFile(new File("target"), "temp", srcFileName);
		FileUtil.copy(FileResource.openByteStream(referenceClass, srcFileName), tmp);
		tmp.deleteOnExit();
		return tmp;
	}

	public static <T> File createTempDir() throws IOException {
		File tmp = FileUtil.createTempDir(new File("target"), "tempdir");
		return tmp;
	}

}
