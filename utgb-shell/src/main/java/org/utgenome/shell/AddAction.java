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
// Add.java
// Since: Jan 9, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import org.utgenome.config.UTGBConfig;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

/**
 * A UTGBShell sub-command for adding new action class
 * 
 * @author leo
 * 
 */
public class AddAction extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(AddAction.class);

	@Option(symbol = "p", longName = "package", varName = "PACKAGE", description = "base package name to add a new action class")
	private String packageName;

	@Argument
	private String target;

	public AddAction() {

	}

	@Override
	public void execute(String[] args) throws Exception {

		if (packageName == null) {
			// load the package name from the track-config.xml
			UTGBConfig config = loadUTGBConfig();
			packageName = config.javaPackage;
		}
		String appPackageName = packageName + "." + APP_FOLDER;

		if (target == null) {
			throw new UTGBShellException("No web action name is given. Type utgb action --help for the usage");
		}

		String newActionClassFullPath = appPackageName + "." + target.replaceAll("/", ".");
		_logger.info("add a new action class : " + newActionClassFullPath);

		int extPos = newActionClassFullPath.lastIndexOf(".");
		String actionClassName = (extPos > 0) ? newActionClassFullPath.substring(extPos + 1) : newActionClassFullPath;
		String actionPackageName = (extPos > 0) ? newActionClassFullPath.substring(0, extPos) : appPackageName;

		// create an action class
		Properties prop = new Properties();
		prop.put("actionClass", actionClassName);
		prop.put("actionPackage", actionPackageName);
		Date now = new Date();
		prop.put("date", DateFormat.getDateInstance().format(now));

		String javaFileName = SRC_FOLDER + "/" + newActionClassFullPath.replaceAll("\\.", "/") + ".java";
		createFileFromTemplate(AddAction.class, "template/java/Action.java.template", javaFileName, prop);

	}

	@Override
	public String name() {
		return "action";
	}

	public String getOneLinerDescription() {
		return "add a new web action handler";
	}

}
