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
// SVNAdd.java
// Since: Jul 7, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * Subcommand to add the current project to the subversion repository
 * 
 * @author leo
 * 
 */
public class SVNAdd extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(SVNAdd.class);

	public SVNAdd() {

	}

	private static String[] addTargetFiles = { "README", "pom.xml" };
	private static String[] addTargetDirs = { "config", "eclipse", "src" };
	private static String[] ignoreTargetFiles = { "target" };

	SVNClientManager svnClientManager = null;
	SVNWCClient workingCopyClient = null;

	@Override
	public void execute(String[] args) throws Exception {

		if (!isInProjectRoot())
			throw new UTGBShellException("must be in the project root");

		svnClientManager = SVNClientManager.newInstance();
		workingCopyClient = svnClientManager.getWCClient();

		// svn add the current directory

		svnAddDir(globalOption.projectDir != null ? globalOption.projectDir : "", false);

		// svn add
		for (String target : addTargetDirs) {
			// recursively add the folder contents to the version management
			svnAddDir(target, true);
		}
		for (String target : addTargetFiles) {
			svnAddFile(target);
		}

		// svn:ignore
		String ignoreTarget = StringUtil.join(ignoreTargetFiles, "\n");
		svnIgnore(".", ignoreTarget, false);

		// db folder
		svnAddDir("db", false);
		svnIgnore("db", "*.db", false);

		// add tomcat folder and ignore work files
		// svnAddDir("tomcat", false);
		// svnAddDir("tomcat/conf", true);
		// svnAddDir("tomcat/webapps", true);
		// svnIgnore("tomcat", "work", false);

		svnAddDir("war", false);
		svnAddDir("war/WEB-INF", true);
		svnIgnore("war", "utgb", false);

	}

	public void svnAddDir(String dir, boolean isRecursive) throws SVNException {
		assert (workingCopyClient != null);
		_logger.info("svn add " + dir);
		File f = new File(dir);
		boolean createDir = !f.exists();
		// workingCopyClient.doAdd(f, false, createDir, false, isRecursive); // File, force, mkdir,
		// climbUnversionedParents, recursive
		workingCopyClient.doAdd(f, true, createDir, false, SVNDepth.fromRecurse(isRecursive), false, true);

	}

	public void svnAddFile(String file) throws SVNException {
		assert (workingCopyClient != null);
		_logger.info("svn add " + file);
		File f = new File(file);
		// workingCopyClient.doAdd(new File(file), false, false, false, false);
		workingCopyClient.doAdd(new File(file), true, false, false, SVNDepth.fromRecurse(false), false, false);
	}

	public void svnIgnore(String file, String ignoreTargets, boolean isRecursive) throws SVNException {
		assert (workingCopyClient != null);
		_logger.info("set svn:ignore on directory =" + file + ", targets = " + ignoreTargets);
		// workingCopyClient.doSetProperty(new File(file), "svn:ignore", ignoreTargets, false, isRecursive, null); //
		// path, propName, propValue, force, recursive, handler
		workingCopyClient.doSetProperty(new File(file), "svn:ignore", SVNPropertyValue.create(ignoreTargets), false, SVNDepth.fromRecurse(isRecursive), null,
				null);
	}

	@Override
	public String name() {
		return "svn-add";
	}

	public String getOneLinerDescription() {
		return "add the current project to the SVN repostiory";
	}

}
