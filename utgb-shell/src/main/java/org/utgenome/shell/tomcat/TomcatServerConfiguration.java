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
// TomcatServerConfiguration.java
// Since: May 7, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell.tomcat;

import java.io.File;

/**
 * Configuration bean for the {@link TomcatServer}.
 * 
 * @author leo
 * 
 */
public class TomcatServerConfiguration {
	private int port = 8989; // default port number is set to 8989
	private int ajp13port = 8990; // proxy server
	private String catalinaBase; //

	public TomcatServerConfiguration() {
		String workDir = getSystemProperty("user.dir", "");
		// The default Tomcat base folder is set to workdir/target/tomcat
		this.catalinaBase = new File(getSystemProperty("catalina.base", workDir), "target/tomcat").getPath();
	}

	public static TomcatServerConfiguration newInstance(int port) {
		TomcatServerConfiguration config = new TomcatServerConfiguration();
		config.setPort(port);
		return config;
	}

	private static String getSystemProperty(String key, String defaultValue) {
		String value = System.getProperty("user.dir");
		return value != null ? value : defaultValue;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if (port < 0)
			throw new IllegalArgumentException("invalid port number: " + port);
		this.port = port;
	}

	public String getCatalinaBase() {
		return catalinaBase;
	}

	public void setCatalinaBase(String catalinaBase) {

		// File path = new File(catalinaBase);
		// if (!path.isAbsolute())
		// catalinaBase = path.getAbsolutePath();

		this.catalinaBase = catalinaBase;
	}

	public int getAjp13port() {
		return ajp13port;
	}

	public void setAjp13port(int ajp13port) {
		this.ajp13port = ajp13port;
	}

}
