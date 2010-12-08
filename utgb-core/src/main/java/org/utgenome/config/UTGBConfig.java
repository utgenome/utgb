/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// UTGBConfig.java
// Since: 2009/08/21
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.config;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.xerial.core.XerialException;
import org.xerial.lens.SilkLens;

/**
 * UTGB configuration. This object corresponds to config/{common, default}.silk file.
 * 
 * @author leo
 * 
 */
public class UTGBConfig {

	public static class WebAction {
		public String alias;
		public String javaPackage;

	}

	public static class Database {
		public String id;
		public String dbms = "sqlite";
		public String driver;
		public String jdbcPrefix;
		public String address;
		public String user;
		public String pass;

		@Override
		public String toString() {
			return String.format("id:%s, dbms:%s, driver:%s, address:%s", id, dbms, driver, address);
		}
	}

	public String version = "1.0";
	public String group;
	public String projectName;
	public String javaPackage;
	public List<WebAction> webAction = new ArrayList<WebAction>();
	public List<Database> database = new ArrayList<Database>();
	public Properties _ = new Properties();

	public void put(String key, String value) {
		_.put(key, value);
	}

	public String getProperty(String key) {
		return _.getProperty(key);
	}

	public boolean hasPropertyOf(String key) {
		return _.containsKey(key);
	}

	public String getProperty(String key, String defaultValue) {
		return _.getProperty(key, defaultValue);
	}

	public Database getDatabase(String id) {
		for (Database each : database) {
			if (id.equals(each.id))
				return each;
		}
		return null;
	}

	/**
	 * Parse the input config file in Silk format, and return the UTGBConfig instance
	 * 
	 * @param configResource
	 * @return {@link UTGBConfig} object
	 * @throws IOException
	 *             when failed to read the resource
	 * @throws XerialException
	 *             when some syntax error is observed
	 */
	public static UTGBConfig parse(URL configResource) throws IOException, XerialException {
		return SilkLens.loadSilk(UTGBConfig.class, configResource);
	}

	public static UTGBConfig parseSilk(String silk) throws XerialException, IOException {
		return SilkLens.loadSilk(UTGBConfig.class, new StringReader(silk));
	}

	public String toSilk() {
		return SilkLens.toSilk(this);
	}

}
