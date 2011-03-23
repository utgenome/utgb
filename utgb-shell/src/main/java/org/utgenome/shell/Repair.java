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
// Repair.java
// Since: Feb 1, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

import org.utgenome.config.TrackConfiguration;
import org.utgenome.config.UTGBConfig;
import org.utgenome.shell.Create.OverwriteMode;
import org.utgenome.shell.Create.ScaffoldFileFilter;
import org.xerial.lens.XMLLens;
import org.xerial.silk.SilkWriter;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

public class Repair extends UTGBShellCommand {
	private static Logger _logger = Logger.getLogger(Repair.class);

	@Option(symbol = "p", longName = "package", varName = "PACKAGE_NAME", description = "specify the java package name. e.g. org.yourdomain.track")
	private String packageName = null;

	@Option(symbol = "g", longName = "group", varName = "GROUP_NAME", description = "specify the maven group name of this project. default = org.utgenome.track")
	private String group = "org.utgenome.track";

	@Option(symbol = "f", longName = "filepath", varName = "PATH", description = "repair the specified file/folder only")
	private String repairTargetPath = null;

	@Option(symbol = "y", description = "overwrite existing files without asking")
	private boolean yesToAll = false;

	private ScaffoldFileFilter scaffoldFilter = new Create.CreateAllScaffoldFileFilter();

	@Argument(index = 0)
	private String projectName = null;

	public Repair() {
	}

	private String targetFile;

	@Override
	public void execute(String[] args) throws Exception {

		if (yesToAll)
			ScaffoldGenerator.overwriteMode = OverwriteMode.YES_TO_ALL;

		if (repairTargetPath != null) {
			scaffoldFilter = new Create.ScaffoldFileFilter() {
				public boolean accept(String pathname) {
					return pathname.startsWith(repairTargetPath);
				}
			};
		}

		if (!isInProjectRoot()) {
			// no configuration file is found
			File oldConfigXML = getObsolteConfigurationFile();
			if (oldConfigXML.exists()) {
				_logger.info(String.format("old-track configuration file %s is found", oldConfigXML));

				// convert the old config file to the silk format
				TrackConfiguration oldConfig = XMLLens.loadXML(TrackConfiguration.class, new BufferedReader(new FileReader(oldConfigXML)));
				UTGBConfig newConfig = oldConfig.convert();
				String silk = newConfig.toSilk();

				// output the new configuration file
				File newConfigFile = new File("config/common.silk");
				if (!newConfigFile.exists()) {
					_logger.info("needs upgrade");
					_logger.info("generating " + newConfigFile);
					FileWriter fout = new FileWriter(newConfigFile);
					fout.append(silk);
					fout.append(StringUtil.newline());
					fout.flush();
					fout.close();
				}

				// generate the new configuration files
				for (String configFile : new String[] { "config/development.silk", "config/production.silk", "config/test.silk" }) {
					targetFile = configFile;
					Create.createScaffold(newConfig, "./", new ScaffoldFileFilter() {
						public boolean accept(String logicalPathName) {
							return logicalPathName.startsWith(targetFile);
						}
					});
				}
			}

		}

		try {
			UTGBConfig config = loadUTGBConfig();
			Create.createScaffold(config, getProjectRoot().getPath(), scaffoldFilter);
		}
		catch (UTGBShellException e) {
			_logger.info(String.format("No %s file found or the config file is collapsed", getConfigFile()));
			// create from scratch
			if (projectName == null)
				throw new UTGBShellException("please specify your package name with -p option and the project name as a command line argument.");
			if (packageName == null)
				packageName = projectName;
			UTGBConfig config = new UTGBConfig();
			config.projectName = projectName;
			config.javaPackage = packageName;
			config.group = group;
			Create.createScaffold(config, "./", scaffoldFilter);
		}

	}

	public static String toSilk(UTGBConfig config) {
		StringWriter buf = new StringWriter();
		SilkWriter w = new SilkWriter(buf);

		return null;
	}

	@Override
	public String name() {
		return "repair";
	}

	@Override
	public String getOneLinerDescription() {
		return "repair or restore template files";
	}

}
