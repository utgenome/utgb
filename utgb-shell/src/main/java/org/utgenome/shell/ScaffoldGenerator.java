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
// ScaffoldGenerator.java
// Since: Jun 2, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.utgenome.config.UTGBConfig;
import org.utgenome.shell.Create.OverwriteMode;
import org.utgenome.shell.Create.ScaffoldFileFilter;
import org.xerial.util.FileResource;
import org.xerial.util.io.VirtualFile;
import org.xerial.util.log.Logger;
import org.xerial.util.template.Template;

/**
 * Scaffold generator
 * 
 * @author leo
 * 
 */
public class ScaffoldGenerator {

	private static Logger _logger = Logger.getLogger(ScaffoldGenerator.class);

	private String outputFolder;
	private OverwriteMode overwriteMode = OverwriteMode.INTERACTIVE;
	private final Properties property = new Properties();
	private ScaffoldFileFilter generateFileFilter = null;

	public ScaffoldGenerator(String outputFolder, ScaffoldFileFilter generateFileFilter) {
		this.outputFolder = outputFolder;
		if (this.outputFolder == null)
			this.outputFolder = ".";
		this.generateFileFilter = generateFileFilter;
	}

	public void prepareTemplatePropeties(UTGBConfig config) {
		property.put("projectName", config.projectName);
		property.put("package", config.javaPackage);
		property.put("group", config.group);
		String moduleName = config.javaPackage + ".gwt.Browser";
		property.setProperty("moduleName", moduleName);
		property.put("explodedWebappDir", UTGBShellCommand.EXPLODED_WEBAPP_DIR);

		// properties for the GWT interface code
		String clientPackageName = config.javaPackage + ".gwt.client";
		String serverPackageName = config.javaPackage + ".gwt.server";
		String entryPoint = clientPackageName + ".Browser";

		property.setProperty("packageName", config.javaPackage);
		property.setProperty("clientPackageName", clientPackageName);
		property.setProperty("serverPackageName", serverPackageName);
		property.setProperty("moduleName", moduleName);
		property.setProperty("entryPoint", entryPoint);

		property.setProperty("utgbVersion", UTGBShell.getVersion());
	}

	public void createProjectScaffold(UTGBConfig config) throws IOException, UTGBShellException {
		prepareTemplatePropeties(config);

		// compute class path entries for the Eclipse project
		// create src and test folders
		mkdirs(new File(outputFolder, "src/main/java"));
		mkdirs(new File(outputFolder, "src/test/java"));

		// copy template codes into the output folder
		copyScaffold("org.utgenome.shell.template.java.scaffold", outputFolder);

		// GWT module folder
		mkdirs(new File(outputFolder, "war/utgb"));

		// create a Eclipse launch file to start UTGB Portable
		createEclipseLaunchFile(config.projectName, "template/java/server.launch.template", "server");
	}

	public void createGWTModuleScaffold(UTGBConfig config) throws IOException, UTGBShellException {

		prepareTemplatePropeties(config);

		String gwtOutputFolder = outputFolder + "/src/main/java/" + config.javaPackage.replaceAll("\\.", "/");
		copyScaffold("org.utgenome.shell.template.java.gwtscaffold", gwtOutputFolder);

		createEclipseLaunchFile(config.projectName, "template/java/gwt-win.launch.template", "gwt");
		createEclipseLaunchFile(config.projectName, "template/java/gwt-mac.launch.template", "gwt-mac");

	}

	public void createEclipseLaunchFile(String projectName, String templatePath, String suffix) throws IOException, UTGBShellException {
		String launchFileLogicalName = "eclipse/" + projectName + "-" + suffix + ".launch";
		File launchFile = new File(outputFolder, launchFileLogicalName);
		if (canOverwrite(launchFile, launchFileLogicalName))
			UTGBShellCommand.createFileFromTemplate(UTGBShell.class, templatePath, outputFolder, launchFileLogicalName, property, true);
	}

	/**
	 * Creates the folder structure for the Tomcat
	 * 
	 * @param catalinaBase
	 * @throws IOException
	 */
	public void copyScaffold(String inputResourcePacakge, String outputDir) throws IOException {
		_logger.info("output folder: " + outputFolder);
		for (Object keyObj : property.keySet()) {
			String key = keyObj.toString();
			_logger.info(key + "\t = " + property.getProperty(key));
		}

		// create the base folder for the scaffold
		File outputFolder = new File(outputDir);
		List<VirtualFile> scaffoldResourcesList = FileResource.listResources(inputResourcePacakge);
		// remove duplicates from resources
		ArrayList<VirtualFile> scaffoldResources = new ArrayList<VirtualFile>();
		{
			HashSet<String> observedPath = new HashSet<String>();
			for (VirtualFile vf : scaffoldResourcesList) {
				if (!observedPath.contains(vf.getLogicalPath())) {
					observedPath.add(vf.getLogicalPath());
					scaffoldResources.add(vf);
				}
			}
		}
		if (scaffoldResources.size() <= 0)
			throw new IllegalStateException(inputResourcePacakge + " is not found");
		// sync scaffoldDir with the output folder
		for (VirtualFile vf : scaffoldResources) {
			String srcLogicalPath = vf.getLogicalPath();
			if (vf.isDirectory()) {
				mkdirs(new File(outputFolder, srcLogicalPath));
			}
			else {
				boolean isTemplate = false;
				String outputFileName = srcLogicalPath;
				// is template?
				int extIndex = srcLogicalPath.lastIndexOf(".");
				if (extIndex > 0) {
					String fileExt = srcLogicalPath.substring(extIndex + 1);
					if (fileExt.equals("template")) {
						isTemplate = true;
						outputFileName = srcLogicalPath.substring(0, extIndex);
					}
				}
				File targetFile = new File(outputFolder, outputFileName);
				File parentFolder = targetFile.getParentFile();
				mkdirs(parentFolder);
				// copy the file content
				if (canOverwrite(targetFile, outputFileName)) {
					InputStream reader = null;
					if (isTemplate) {
						// fill variables in the template
						InputStream templateReader = vf.getURL().openStream();
						Template template = new Template(templateReader);
						String result = template.apply(property);
						reader = new ByteArrayInputStream(result.getBytes());
					}
					else
						reader = vf.getURL().openStream();
					_logger.info("create a file: " + getPath(targetFile));
					copyFile(reader, targetFile);
				}
			}
		}
	}

	private BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Tests overwriting of the file is allowed.
	 * 
	 * If allowed, rename the old file to (original name).tmp, and returns true.
	 * 
	 * @param targetFile
	 *            the file to be written.
	 * @return true if overwriting is allowed, false otherwise
	 * @throws IOException
	 */
	private boolean canOverwrite(File targetFile, String logicalPath) throws IOException {
		if (!generateFileFilter.accept(logicalPath))
			return false;
		if (!targetFile.exists())
			return true;
		if (overwriteMode == OverwriteMode.NO_TO_ALL)
			return false;
		if (overwriteMode != OverwriteMode.YES_TO_ALL) {
			System.out.println("File " + getPath(targetFile) + " already exists.");
			System.out.print("Overwrite it? (y: yes, n: no, A: yes to all, X: no to all) [n]: ");
			String input = cin.readLine();
			System.out.println();
			if (input == null)
				return false;
			input = input.trim();
			if (input.length() <= 0)
				return false;
			switch (input.charAt(0)) {
			case 'n':
				return false;
			case 'A':
				overwriteMode = OverwriteMode.YES_TO_ALL;
				break;
			case 'X':
				overwriteMode = OverwriteMode.NO_TO_ALL;
				return false;
			case 'y':
				break;
			default:
				return false;
			}
		}
		if (!targetFile.getName().endsWith(".jar")) // do not take a back up of the jar file
		{
			// rename the old file to (old file name).tmp
			File tempFile = new File(targetFile + ".old");
			_logger.info("previous file was copied as " + getPath(tempFile));
			copyFile(new FileInputStream(targetFile), tempFile);
		}
		return true;
	}

	public static void copyFile(InputStream in, File dest) throws IOException {
		FileOutputStream writer = new FileOutputStream(dest);
		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = in.read(buffer)) > 0) {
			writer.write(buffer, 0, bytesRead);
		}
		writer.flush();
		writer.close();
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

	public static String getPath(File f) {
		return UTGBShellCommand.getPath(f);
	}
}
