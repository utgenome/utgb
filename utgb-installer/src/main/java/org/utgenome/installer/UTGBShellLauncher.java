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
// utgb-installer Project
//
// UTGBShellLauncher.java
// Since: 2009/10/21
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.installer;

import java.io.File;

import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.launcher.Launcher;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.xerial.util.log.Logger;

public class UTGBShellLauncher {

	private static Logger _logger = Logger.getLogger(UTGBShellLauncher.class);

	private File utgbHome = new File(System.getProperty("user.home"), ".utgb");
	private ClassWorld classWorld = new ClassWorld();
	private ClassRealm utgbRealm = null;

	public UTGBShellLauncher() {
	}

	public void setUTGBHome(File utgbHome) {
		this.utgbHome = utgbHome;
	}

	public void launchUTGBShell(String[] args) throws Exception {
		File utgbShellJAR = new File(utgbHome, "lib/utgb-shell-standalone.jar");

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		ClassLoader pcl = cl.getParent();

		if (utgbRealm == null) {
			// use the parent class loader 
			utgbRealm = classWorld.newRealm("utgb", pcl);
		}

		utgbRealm.addURL(utgbShellJAR.toURL());
		utgbRealm.importFrom(cl, "org.codehaus.plexus.classworlds");

		Launcher launcher = new Launcher();
		launcher.setWorld(classWorld);
		launcher.setAppMain("org.utgenome.shell.UTGBShell", "utgb");

		launcher.launch(args);

	}
}
