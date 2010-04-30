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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.maven.cli.CLIManager;
import org.apache.maven.cli.MavenCli;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.xerial.util.FileResource;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * Maven utility
 * 
 * @author leo
 * 
 */
public class Maven extends UTGBShellCommand {

	private static Logger _logger = Logger.getLogger(Maven.class);

	private static String MAVEN_VERSION = "3.0-beta-1";

	public static boolean isMavenInstalled() {
		String utgbHome = System.getProperty("utgb.home");
		if (utgbHome == null)
			return false;
		else
			return new File(utgbHome, "maven").exists();
	}

	/**
	 * Extract the Maven binary from the archive inside the JAR
	 * 
	 * @return MAVEN_HOME, where Mavan library is included.
	 * @throws IOException
	 */
	static String extractMaven() throws IOException {

		final String utgbFolder = System.getProperty("user.home") + "/.utgb";
		final String mavenFolderName = utgbFolder + "/maven";

		File mavenFolder = new File(mavenFolderName);
		if (!mavenFolder.exists())
			mavenFolder.mkdirs();

		String mavenBinParentFolder = null;

		URL mavenArchive = FileResource.find("org.utgenome.shell.archive", "apache-maven-" + MAVEN_VERSION + "-bin.tar.bz2");

		BufferedInputStream bufferedInputStream = new BufferedInputStream(mavenArchive.openStream());
		try {
			// read two bytes "BZ"
			bufferedInputStream.read();
			bufferedInputStream.read();
			TarInputStream tis = new TarInputStream(new CBZip2InputStream(bufferedInputStream));
			TarEntry nextEntry = null;
			while ((nextEntry = tis.getNextEntry()) != null) {
				int mode = nextEntry.getMode();
				String name = nextEntry.getName();
				Date modTime = nextEntry.getModTime();

				if (name.endsWith("/bin/mvn")) {
					mavenBinParentFolder = new File(mavenFolderName, name.replace("/bin/mvn", "")).getAbsolutePath();
				}

				File extractedFile = new File(mavenFolder, name);

				if (extractedFile.exists() && extractedFile.lastModified() == modTime.getTime())
					continue;

				if (!nextEntry.isDirectory()) {
					_logger.info(String.format("extracted %s into %s", name, mavenFolder.getPath()));

					File parent = extractedFile.getParentFile();
					if (parent != null && !parent.exists())
						parent.mkdirs();

					FileOutputStream fo = new FileOutputStream(extractedFile);
					try {
						tis.copyEntryContents(fo);
					}
					finally {
						fo.close();
					}

				}
				else {
					if (!extractedFile.exists())
						extractedFile.mkdirs();
				}

				// chmod
				if (!System.getProperty("os.name").contains("Windows")) {
					try {
						int m = mode & 00777;
						String modeStr = Integer.toOctalString(m);
						String cmd = String.format("chmod %s %s", modeStr, extractedFile.getAbsolutePath());

						CommandExecutor.exec(String.format(cmd));
					}
					catch (Throwable e) {
						_logger.error(e);
					}
				}

				extractedFile.setLastModified(modTime.getTime());

			}

			if (mavenBinParentFolder == null)
				throw new IllegalStateException("maven binary is not found in the archive");
		}
		finally {
			bufferedInputStream.close();
		}

		return mavenBinParentFolder;
	}

	static String getMavenBinary() {

		String utgbHome = System.getProperty("utgb.home");
		String osName = System.getProperty("os.name");
		if (osName == null)
			throw new IllegalStateException("cannot find out your OS name or os.name JVM property is wrongly set somewhere");

		String mavenStartupScript = (osName.contains("Windows")) ? "mvn.bat" : "mvn";

		if (utgbHome == null) {
			try {
				String mavenHome = extractMaven();
				return new File(mavenHome, "bin/" + mavenStartupScript).getPath();
			}
			catch (IOException e) {
				throw new IllegalStateException("cannot extract Maven binaries: " + e.getMessage());
			}
		}
		else
			return new File(utgbHome, "maven/bin/" + mavenStartupScript).getPath();
	}

	public static void runMaven(String arg) throws UTGBShellException {
		runMaven(arg.split("[\\s]+"));
	}

	public static void runMaven(String arg, File workingDir) throws UTGBShellException {
		// runMaven(arg.split("[\\s]+"), workingDir);
		runEmbeddedMaven(arg.split("[\\s]+"), workingDir);
	}

	public static void runMaven(String[] args) throws UTGBShellException {
		// runMaven(args, null);
		runEmbeddedMaven(args, null);
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
					public void output(String line) {
						_logger.info(line);
					}
				});
				stderrReader = threadManager.submit(new ProcessOutputReader(proc.getErrorStream()) {
					public void output(String line) {
						_logger.error(line);
					}
				});

				int ret = proc.waitFor();
				return ret;
			}
			catch (InterruptedException e) {
				_logger.error(e);
				return 1;
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

	public static int runMaven(String[] args, File workingDir) throws UTGBShellException {

		try {

			// add the hook for killing the Maven process when ctrl+C is pressed
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					_logger.debug("shutdown hook is called");
				}
			});

			String mavenBinary = getMavenBinary();

			_logger.debug("Maven binary: " + mavenBinary);
			File mavenHome = new File(mavenBinary).getParentFile().getParentFile();

			String[] envp = prepareEnvironmentVariables(mavenHome);

			String cmdLineFormat = System.getProperty("os.name").contains("Windows") ? "\"%s\" %s" : "%s %s";
			int returnCode = CommandExecutor.exec(String.format(cmdLineFormat, mavenBinary, StringUtil.join(args, " ")), envp, workingDir);

			if (returnCode != 0)
				throw new UTGBShellException("error: " + returnCode);

			return returnCode;
		}
		catch (Exception e) {
			throw new UTGBShellException(e);
		}
	}

	public static void explodeWar() throws UTGBShellException {
		runMaven(new String[] { "compile", "war:exploded" });
	}

	@Override
	public void execute(String[] args) throws Exception {
		runMaven(args);
	}

	@Override
	public String name() {
		return "maven";
	}

	public String getOneLinerDescription() {
		return "execute maven tasks";
	}

	public static void runEmbeddedMaven(String[] args, File workDir) throws UTGBShellException {

		try {
			// 
			// 
			//

			// ContainerConfiguration conf = new DefaultContainerConfiguration().setClassWorld(cw).setName("maven");
			// DefaultPlexusContainer container = new DefaultPlexusContainer(conf);
			// org.apache.maven.Maven maven = container.lookup(org.apache.maven.Maven.class);
			//			
			//
			// // MavenExecutionResult result = maven.execute(request);

			File utgbHome = new File(System.getProperty("user.home"), ".utgb");
			File utgbShellJAR = new File(utgbHome, "lib/utgb-shell-standalone.jar");
			if (!utgbShellJAR.exists())
				throw new IllegalStateException(utgbShellJAR + " is not found");

			ClassLoader clo = Thread.currentThread().getContextClassLoader();
			ClassWorld classWorld = new ClassWorld("plexus.core", clo);
			ClassRealm utgbRealm = classWorld.newRealm("utgb");
			utgbRealm.addURL(utgbShellJAR.toURI().toURL());

			ContainerConfiguration cc = new DefaultContainerConfiguration();
			cc.setClassWorld(classWorld);
			cc.setName("embedder");
			DefaultPlexusContainer plexus = new DefaultPlexusContainer(cc);
			org.apache.maven.Maven maven = plexus.lookup(org.apache.maven.Maven.class);
			ModelProcessor modelProcessor = plexus.lookup(ModelProcessor.class);

			CLIManager cliManager = new CLIManager();
			CommandLine cl = cliManager.parse(args);
			if (cl.hasOption(CLIManager.HELP)) {
				cliManager.displayHelp(System.out);
				return;
			}

			MavenExecutionRequest request = new DefaultMavenExecutionRequest();
			request.setUserSettingsFile(MavenCli.DEFAULT_USER_SETTINGS_FILE).setGlobalSettingsFile(MavenCli.DEFAULT_GLOBAL_SETTINGS_FILE).setPom(
					modelProcessor.locatePom(workDir)).setBaseDirectory(workDir).setGoals(cl.getArgList());

			MavenExecutionResult ret = maven.execute(request);
			if (ret.hasExceptions()) {
				throw new UTGBShellException("Maven execution failed: " + ret.getExceptions());
			}

			// // ClassLoader pcl = cl.getParent();
			// // utgbRealm.importFrom(cl, "org.codehaus.plexus.classworlds");
			// // utgbRealm.importFrom(cl, "org.apache.maven");
			//
			// MavenCli mavenCli = new MavenCli(classWorld);
			// int ret = mavenCli.doMain(args, workDir == null ? System.getProperty("user.dir") :
			// workDir.getAbsolutePath(), System.out, System.err);
			// if (ret != 0)
			// throw new UTGBShellException("Maven execution failed: " + ret);
		}
		catch (UTGBShellException e) {
			throw e;
		}
		catch (Exception e) {
			throw new UTGBShellException(e);
		}

	}

}
