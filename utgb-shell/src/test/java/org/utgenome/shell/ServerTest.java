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
// ServerTest.java
// Since: May 7, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xerial.util.log.Logger;

public class ServerTest {

	private static Logger _logger = Logger.getLogger(ServerTest.class);
	static String tmpDir = "target"; // System.getProperty("java.io.tmpdir");
	public static String appName = "sample";

	@BeforeClass
	public static void setUp() throws Exception {
		// create temporary application

		int count = 0;
		while (new File(tmpDir, appName).exists()) {
			appName = "sample" + count++;
		}

		// create a web application scaffold
		UTGBShell.runCommand(new String[] { "create", "-d", tmpDir, appName });

	}

	@AfterClass
	public static void tearUp() {
		// rmdir(new File(tmpDir, appName));
	}

	static boolean rmdir(File path) {
		if (path.exists()) {
			for (File each : path.listFiles()) {
				if (each.isDirectory())
					rmdir(each);
				else {
					each.delete();
				}
			}
		}

		return path.delete();
	}

	@Test
	public void compile() throws Exception {
		// UTGBShell.main(new String[] { "gwt", "-d", new File(tmpDir, appName).getAbsolutePath() });

		final String projectPath = new File(tmpDir, appName).getAbsolutePath();

		UTGBShell.runCommand(new String[] { "action", "-d", projectPath, "hello" });
		UTGBShell.runCommand(new String[] { "compile", "-d", projectPath });
	}

	@Test
	public void server() throws Exception {
		// UTGBShell.main(new String[] { "gwt", "-d", new File(tmpDir, appName).getAbsolutePath() });

		final String projectPath = new File(tmpDir, appName).getAbsolutePath();

		UTGBShell.runCommand(new String[] { "action", "-d", projectPath, "hello" });
		UTGBShell.runCommand(new String[] { "compile", "-d", projectPath });

		ExecutorService es = Executors.newFixedThreadPool(1);
		es.submit(new Callable<Void>() {
			public Void call() throws Exception {
				UTGBShell.runCommand(new String[] { "server", "-d", projectPath });
				return null;
			}
		});

		URL serverPage = new URL("http://localhost:8989/" + appName + "/utgb-core/roundcircle");
		URL actionPage = new URL("http://localhost:8989/" + appName + "/hello");

		Thread.sleep(8 * 1000);

		URLConnection conn = serverPage.openConnection();
		String ct = conn.getContentType();
		_logger.info("content-type: " + ct);

		URLConnection conn2 = actionPage.openConnection();
		String ct2 = conn2.getContentType();
		_logger.info("content-type: " + ct2);

		es.shutdownNow();
		es.awaitTermination(1, TimeUnit.SECONDS);

		Thread.sleep(2 * 1000);
	}

	@Test
	public void envSwitchTest() throws Exception {
		// UTGBShell.main(new String[] { "gwt", "-d", new File(tmpDir, appName).getAbsolutePath() });

		final String projectPath = new File(tmpDir, appName).getAbsolutePath();

		UTGBShell.runCommand(new String[] { "compile", "-d", projectPath });

		ExecutorService es = Executors.newFixedThreadPool(1);
		es.submit(new Callable<Void>() {
			public Void call() throws Exception {
				UTGBShell.runCommand(new String[] { "server", "-d", projectPath, "-e", "test" });
				return null;
			}
		});

		Thread.sleep(8 * 1000);

		es.shutdownNow();
		es.awaitTermination(1, TimeUnit.SECONDS);

		Thread.sleep(2 * 1000);
	}

}
