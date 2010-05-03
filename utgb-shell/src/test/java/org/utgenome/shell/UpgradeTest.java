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
// UpgradeTest.java
// Since: May 3, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.gwt.utgb.client.view.TrackView;
import org.xerial.lens.Lens;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;
import org.xerial.util.log.Logger;

public class UpgradeTest {

	private static Logger _logger = Logger.getLogger(UpgradeTest.class);
	static String tmpDir = "target"; // System.getProperty("java.io.tmpdir");
	static String appName = "sample";
	private String appDir = null;

	@Before
	public void setUp() throws Exception {
		int count = 0;
		while (new File(tmpDir, appName).exists()) {
			appName = "sample" + count++;
		}
		appDir = new File(tmpDir, appName).toString();

		// create a web application scaffold
		UTGBShell.runCommand(new String[] { "create", "-d", tmpDir, appName });
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void upgradeView() throws Exception {

		// copy oldview.xml to src/main/webapp/view/oldview.xml
		FileUtil.copy(FileResource.openByteStream(UpgradeTest.class, "oldview.xml"), new File(appDir, "src/main/webapp/view/oldview.xml"));
		UTGBShell.runCommand(new String[] { "upgrade", "-d", appDir });

		File newViewFile = new File(appDir, "config/view/oldview.silk");
		assertTrue(newViewFile.exists());

		TrackView tv = Lens.loadSilk(TrackView.class, new FileReader(newViewFile));
		assertEquals(6, tv.track.size());

	}
}
