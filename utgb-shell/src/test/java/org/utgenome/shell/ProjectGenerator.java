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
// ProjectGenerator.java
// Since: May 20, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

/**
 * create a UTGB project for debugging purpose
 * 
 * @author leo
 * 
 */
public class ProjectGenerator {

	public static class ProjectInfo {
		public final String projectRoot;
		public final String appName;

		public ProjectInfo(String projectRoot, String appName) {
			this.projectRoot = projectRoot;
			this.appName = appName;
		}

	}

	private static String tmpDir = "target";

	public static ProjectInfo createTemporatyProject() throws Exception {

		String appName = "myapp";
		int count = 0;
		while (new File(tmpDir, appName).exists()) {
			appName = "sample" + count++;
		}

		// create a web application scaffold
		UTGBShell.runCommand(new String[] { "create", "-d", tmpDir, appName });

		return new ProjectInfo(new File(tmpDir, appName).getAbsolutePath(), appName);
	}
}
