/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// UTGBMedaka Project
//
// TrackLoaderGenerator.java
// Since: Aug 7, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.xerial.util.FileResource;
import org.xerial.util.log.LogLevel;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Argument;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;
import org.xerial.util.opt.OptionParserException;
import org.xerial.util.opt.Usage;
import org.xerial.util.text.Template;

public class TrackLoaderGenerator {
	private static Logger _logger = Logger.getLogger(TrackLoaderGenerator.class);

	@Usage(command = "> java -cp bin org.utgenome.gwt.utgb.TrackLoaderGenerator [option] search_folder")
	public static class Config {

		@Option(symbol = "h", longName = "help", description = "display help messages")
		boolean displayHelp = false;
		@Option(symbol = "v", longName = "verbose", description = "display verbose messages")
		boolean verbose = false;
		@Option(symbol = "p", longName = "package", varName = "NAME", description = "package name")
		String packageName = null;
		@Option(symbol = "c", longName = "class", varName = "NAME", description = "class name")
		String className = null;

		@Argument
		String searchFolderName = null;
	}

	public static void main(String[] args) throws OptionParserException, IOException {
		Config config = new Config();
		OptionParser optionParser = new OptionParser(config);

		optionParser.parse(args);
		if (config.displayHelp) {
			optionParser.printUsage();
		}

		if (config.verbose) {
			Logger.getRootLogger().setLogLevel(LogLevel.ALL);
		}

		if (config.searchFolderName == null) {
			System.err.println("no search folder is specified");
			return;
		}

		_logger.debug("search folder = " + config.searchFolderName);

		if (config.packageName == null || config.className == null) {
			System.err.println("no package or class name is given");
			return;
		}

		// search track classes
		File searchFolder = new File(config.searchFolderName);
		TrackResourceFinder finder = new TrackResourceFinder(searchFolder);
		finder.enter(searchFolder);

		// template
		Template template = new Template(FileResource.find(TrackLoaderGenerator.class, "TrackLoader.java.template").openStream());
		Properties p = new Properties();
		p.put("trackClasses", finder.trackClassFile);
		p.put("trackGroupClasses", finder.trackGroupClassFile);
		p.put("packageName", config.packageName);
		p.put("className", config.className);
		String result = template.apply(p);
		System.out.print(result);
	}

	static class TrackResourceFinder {
		ArrayList<String> trackClassFile = new ArrayList<String>();
		ArrayList<String> trackGroupClassFile = new ArrayList<String>();

		File searchFolder;

		public TrackResourceFinder(File searchFolder) {
			if (!searchFolder.isDirectory())
				throw new IllegalArgumentException(searchFolder.getName() + " is not a directory");
			this.searchFolder = searchFolder;
		}

		private String getClassName(File classFile) {
			String classFilePath = classFile.getAbsolutePath().replace(searchFolder.getAbsolutePath() + File.separator, "");
			return classFilePath.replace(File.separator, ".").replace(".class", "");
		}

		public void enter(File directory) {
			assert (directory.isDirectory());

			_logger.trace("enter the directory: " + directory.getName());
			File[] fileList = directory.listFiles();
			for (File file : fileList) {
				if (file.isDirectory()) {
					enter(file);
				}
				else {
					if (file.getName().endsWith("Track.class") && !file.getName().equals("Track.class")) {
						String className = getClassName(file);
						_logger.trace("found a track class: " + className);
						trackClassFile.add(className);
					}
					if (file.getName().endsWith("TrackGroup.class")) {
						String className = getClassName(file);
						_logger.trace("found a track group class: " + className);
						trackGroupClassFile.add(className);
					}

				}
			}

		}

	}

}
