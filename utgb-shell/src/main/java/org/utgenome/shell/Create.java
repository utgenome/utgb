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
// Create.java
// Since: Jan 9, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;
import java.io.IOException;

import org.utgenome.UTGBException;
import org.utgenome.config.UTGBConfig;
import org.xerial.lens.ObjectLens;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;

/**
 * Create sub command
 * 
 * @author leo
 * 
 */
public class Create extends UTGBShellCommand {
	private static Logger _logger = Logger.getLogger(Create.class);

	private String lang = "java";

	@Option(symbol = "p", longName = "package", varName = "PACKAGE_NAME", description = "specify the package name, e.g. org.yourdomain.track")
	private String packageName = null;

	@Argument
	private String projectName;

	@Option(symbol = "g", longName = "group", varName = "GROUP_NAME", description = "specify the maven group name of this project. default = org.utgenome.track")
	private String group = "org.utgenome.track";

	public static enum OverwriteMode {
		INTERACTIVE, YES_TO_ALL, NO_TO_ALL
	}

	public Create() {

	}

	@Override
	public void execute(String[] args) throws Exception {

		if (projectName == null)
			throw new UTGBException("No project name is given. See utgb create --help for the usage.");

		for (int i = 0; i < projectName.length(); i++) {
			if (StringUtil.isWhiteSpace(projectName.substring(i, i + 1))) {
				System.err.println("White spaces are not allowed in the project name: " + projectName);
				return;
			}
		}

		if (packageName == null)
			packageName = ObjectLens.getCanonicalParameterName(projectName);

		String outputFolder = globalOption.projectDir;
		if (outputFolder == null || outputFolder.length() <= 0)
			outputFolder = projectName;
		else {
			if (!outputFolder.endsWith("/"))
				outputFolder = outputFolder + "/";

			outputFolder = outputFolder + projectName;
		}

		UTGBConfig config = new UTGBConfig();
		config.projectName = projectName;
		config.group = group;
		config.javaPackage = packageName;

		// create the scaffold
		createScaffold(config, outputFolder, new CreateAllScaffoldFileFilter());

	}

	public static interface ScaffoldFileFilter {
		public boolean accept(String logicalPathName);
	}

	public static class CreateAllScaffoldFileFilter implements ScaffoldFileFilter {
		public boolean accept(String pathname) {
			return true;
		}
	}

	public static void createScaffold(UTGBConfig config, String outputFolder, ScaffoldFileFilter generateFileFilter) throws IOException, UTGBShellException {
		ScaffoldGenerator scaffoldGenerator = new ScaffoldGenerator(outputFolder, generateFileFilter);
		scaffoldGenerator.createProjectScaffold(config);
	}

	/**
	 * Create directories including its parent folders if not exist
	 * 
	 * @param dir
	 */
	public static void mkdirs(File dir) {
		if (!dir.exists()) {
			_logger.info("create a directory: " + getPath(dir));
			dir.mkdirs();
		}
	}

	@Override
	public String name() {
		return "create";
	}

	@Override
	public String getOneLinerDescription() {
		return "create a new project for implmenting your own track.";
	}

}
