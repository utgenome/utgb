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
// Upgrade.java
// Since: May 3, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.utgenome.config.OldViewXML;
import org.utgenome.config.TrackConfiguration;
import org.utgenome.config.UTGBConfig;
import org.utgenome.gwt.utgb.client.view.TrackView;
import org.utgenome.shell.Create.ScaffoldFileFilter;
import org.xerial.lens.Lens;
import org.xerial.util.FileType;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

public class Upgrade extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(Upgrade.class);

	@Override
	public void execute(String[] args) throws Exception {

		// upgrade the old track-config.xml
		if (!isInProjectRoot()) {
			// no configuration file is found
			File oldConfigXML = getObsolteConfigurationFile();
			if (oldConfigXML.exists()) {
				_logger.info(String.format("old-track configuration file %s is found", oldConfigXML));

				// convert the old config file to the silk format
				TrackConfiguration oldConfig = Lens.loadXML(TrackConfiguration.class, new BufferedReader(new FileReader(oldConfigXML)));
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
					final String targetFile = configFile;
					Create.createScaffold(newConfig, "./", new ScaffoldFileFilter() {
						public boolean accept(String logicalPathName) {
							return logicalPathName.startsWith(targetFile);
						}
					});
				}
			}
		}

		// upgrade the view XML files
		if (isInProjectRoot()) {
			_logger.info("converting view files...");
			File oldViewFolder = new File(getProjectRoot(), "src/main/webapp/view");
			File[] viewXMLFiles = oldViewFolder.listFiles();
			if (viewXMLFiles != null) {

				// create view folder
				File viewFolder = new File(getProjectRoot(), "config/view");
				viewFolder.mkdirs();

				// convert the old view file to new view Silk file
				for (File viewXML : viewXMLFiles) {
					if (FileType.getFileType(viewXML.getName()) != FileType.XML) {
						_logger.info(String.format("skip %s", viewXML));
						continue;
					}

					File newViewFile = new File(viewFolder, FileType.removeFileExt(viewXML.getName()) + ".silk");
					if (newViewFile.exists()) {
						_logger.info(String.format("skip convertion from %s to %s: %s exists", viewXML, newViewFile, newViewFile));
						continue;
					}

					_logger.info(String.format("generating %s from %s", newViewFile, viewXML));
					OldViewXML oldView = Lens.loadXML(OldViewXML.class, new FileReader(viewXML));
					TrackView tv = oldView.toTrackView();
					FileWriter out = new FileWriter(newViewFile);
					out.append(Lens.toSilk(tv));
					out.close();
				}
			}
		}

	}

	@Override
	public String getOneLinerDescription() {
		return "upgrade the utgb project";
	}

	@Override
	public String name() {
		return "upgrade";
	}

}
