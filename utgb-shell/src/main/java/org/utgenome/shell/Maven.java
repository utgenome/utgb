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
// Maven.java
// Since: Jan 11, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.maven.cli.MavenCli;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.xerial.core.XerialException;
import org.xerial.lens.SilkLens;
import org.xerial.silk.SilkWriter;
import org.xerial.util.FileResource;
import org.xerial.util.StringUtil;
import org.xerial.util.io.StandardErrorStream;
import org.xerial.util.io.StandardOutputStream;
import org.xerial.util.log.Logger;

/**
 * Maven utility
 * 
 * @author leo
 * 
 */
public class Maven extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(Maven.class);


	public static void runMaven(String arg) throws UTGBShellException {
		runMaven(tokenizeCommandLineArgument(arg));
	}

	public static void runMaven(String arg, File workingDir) throws UTGBShellException {
		runMaven(arg, workingDir, null);
	}

	public static void runMaven(String arg, File workingDir, Properties sytemProperties) throws UTGBShellException {
		runMaven(tokenizeCommandLineArgument(arg), workingDir, sytemProperties);
	}

	public static void runMaven(String[] args) throws UTGBShellException {
		runMaven(args, null, null);
	}

	private static abstract class ProcessOutputReader implements Runnable {
		private final BufferedReader reader;

		public ProcessOutputReader(InputStream in) {
			reader = new BufferedReader(new InputStreamReader(in));
		}

		public abstract void output(String line);

		public void run() {
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					output(line);
				}
			}
			catch (IOException e) {
				// If the process is already terminated, IOException (bad file descriptor) might be reported.
				_logger.debug(e);
			}
		}
	}

	public static class CommandExecutor {
		final ExecutorService threadManager = Executors.newFixedThreadPool(2);
		Process proc = null;
		Future<?> stdoutReader;
		Future<?> stderrReader;

		private void dispose() {
			if (proc != null) {
				proc.destroy();
				proc = null;
			}

			threadManager.shutdown();
			try {
				while (!threadManager.awaitTermination(1L, TimeUnit.SECONDS)) {
				}
			}
			catch (InterruptedException e) {
				_logger.error(e);
			}
		}

		public int execCommand(String commandLine, String[] envp, File workingDir) throws IOException {
			try {
				if (_logger.isDebugEnabled())
					_logger.debug(commandLine);

				proc = Runtime.getRuntime().exec(commandLine, envp, workingDir);

				// pipe the program's stdout and stderr to the logger
				stdoutReader = threadManager.submit(new ProcessOutputReader(proc.getInputStream()) {
					@Override
					public void output(String line) {
						_logger.info(line);
					}
				});
				stderrReader = threadManager.submit(new ProcessOutputReader(proc.getErrorStream()) {
					@Override
					public void output(String line) {
						_logger.error(line);
					}
				});

				int ret = proc.waitFor();
				return ret;
			}
			catch (InterruptedException e) {
				_logger.error(e);
				return 0;
			}
			finally {
				dispose();
			}

		}

		public static int exec(String commandLine) throws IOException {
			return exec(commandLine, null, null);
		}

		public static int exec(String commandLine, String[] envp, File workingDir) throws IOException {
			CommandExecutor e = new CommandExecutor();
			return e.execCommand(commandLine, envp, workingDir);
		}

	}

	public static String[] prepareEnvironmentVariables(File mavenHome) {
		Properties env = new Properties();
		for (Entry<String, String> eachEnv : System.getenv().entrySet()) {
			env.setProperty(eachEnv.getKey(), eachEnv.getValue());
		}
		if (!env.contains("JAVA_HOME") || env.getProperty("JAVA_HOME").contains("jre")) {
			env.setProperty("JAVA_HOME", System.getProperty("java.home"));
		}
		if (mavenHome != null && !env.contains("M2_HOME")) {
			env.setProperty("M2_HOME", mavenHome.getAbsolutePath());
		}

		String[] envp = new String[env.size()];
		int index = 0;
		for (Object each : env.keySet()) {
			String key = each.toString();
			envp[index++] = String.format("%s=%s", key, env.getProperty(key));
		}

		_logger.trace("environment variables: " + env);
		return envp;
	}

	public static int runMaven(String[] args, File workingDir, Properties systemProperties) throws UTGBShellException {

		// Preserve the context class loader
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			// add the hook for killing the Maven process when ctrl+C is pressed
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					_logger.debug("shutdown hook is called");
				}
			});

			MavenCli maven = new MavenCli();

			if (workingDir == null)
				workingDir = new File("");

			Properties prevSystemProperties = null;
			if (systemProperties != null) {
				// preserve the current system properties
				prevSystemProperties = (Properties) System.getProperties().clone();

				// add the user-specified system properties
				for (Object key : systemProperties.keySet()) {
					System.setProperty(key.toString(), systemProperties.get(key).toString());
				}
			}

			try {
				int returnCode = maven.doMain(args, workingDir.getPath(), new PrintStream(new StandardOutputStream()), new PrintStream(
						new StandardErrorStream()));

				if (returnCode != 0)
					throw new UTGBShellException("error: " + returnCode);

				return returnCode;
			}
			finally {
				// reset the system properties
				if (prevSystemProperties != null)
					System.setProperties(prevSystemProperties);
			}
		}
		catch (Exception e) {
			throw new UTGBShellException(e);
		}
		finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
	}

	public static String[] tokenizeCommandLineArgument(String arg) {
		return StringUtil.tokenizeCommandLineArgument(arg);
	}

	@Override
	public void execute(String[] args) throws Exception {
		runMaven(args);
	}

	@Override
	public String name() {
		return "maven";
	}

	@Override
	public String getOneLinerDescription() {
		return "execute maven tasks";
	}

}
