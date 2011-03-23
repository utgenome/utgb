/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// utgb-installer Project
//
// UTGBInstaller.java
// Since: Sep 2, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.installer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.GZIPInputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.xerial.lens.Lens;
import org.xerial.util.FileResource;
import org.xerial.util.FileResource.FileInJarArchive;
import org.xerial.util.io.VirtualFile;
import org.xerial.util.log.LogLevel;
import org.xerial.util.log.LogWriter;
import org.xerial.util.log.Logger;
import org.xerial.util.opt.Option;
import org.xerial.util.opt.OptionParser;
import org.xerial.util.opt.OptionParserException;

/**
 * GUI Installer of the UTGB Shell.
 * 
 * 
 * 
 * 
 * @author leo
 * 
 */
public class UTGBInstaller {

	private static Logger _logger = Logger.getLogger(UTGBInstaller.class);

	@Option(symbol = "d", description = "installation folder. The default is your home directory.")
	private String installationFolder = new File(System.getProperty("user.home"), ".utgb").getAbsolutePath();

	static {
		// take the menu bar off the jframe
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		// set the name of the application menu item
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "UTGB Portable");
		System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");

		// set the look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			_logger.error(e);
		}
	}

	public static void main(String[] args) {
		UTGBInstaller installer = new UTGBInstaller();
		OptionParser parser = new OptionParser(installer);

		try {
			parser.parse(args);
		}
		catch (OptionParserException e) {
			_logger.error(e);
			return;
		}

		installer.install();

	}

	private static class CustomTitledBorder extends TitledBorder {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CustomTitledBorder(String title) {
			super(title);
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			insets.set(12, 7, 5, 7);
			return insets;
		}
	}

	class GUIPanel {

		final JFrame f = new JFrame();

		// progress bar
		final JProgressBar progressBar = new JProgressBar();

		// version selector
		private StyledDocument log;

		private ExecutorService exec = Executors.newFixedThreadPool(1);

		public GUIPanel() {
			buildGUI();
		}

		public JPanel buildInstallPanel() {

			final JButton iButton = new JButton("Change");
			final JButton next = new JButton("Install >");
			final JLabel iLabel = new JLabel(installationFolder);

			JPanel iPanel = new JPanel();
			iPanel.setLayout(new BoxLayout(iPanel, BoxLayout.LINE_AXIS));
			iPanel.setBorder(new CustomTitledBorder("UTGB Installation Path"));

			iPanel.add(iLabel);
			iLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			iButton.setMnemonic('C');
			iButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser(installationFolder);
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int ret = chooser.showOpenDialog(iButton);
					if (ret == JFileChooser.APPROVE_OPTION) {
						installationFolder = chooser.getSelectedFile().getAbsolutePath();
						iLabel.setText(installationFolder);
						f.pack();
					}
				}
			});
			iPanel.add(Box.createHorizontalStrut(10));
			iPanel.add(Box.createHorizontalGlue());
			iPanel.add(iButton);
			iButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

			// next, cancel button
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			next.setMnemonic('I');
			next.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					next.setEnabled(false);
					exec.submit(new Runnable() {
						public void run() {
							getTheLatestVersionOfUTGBToolkit();
						}
					});
					next.setEnabled(true);
				}
			});

			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.dispose();
				}
			});
			buttonPanel.add(next);
			buttonPanel.add(cancel);

			// install panel layout
			JPanel installPanel = new JPanel();
			installPanel.setLayout(new BoxLayout(installPanel, BoxLayout.Y_AXIS));
			installPanel.add(iPanel);
			installPanel.add(buttonPanel);

			return installPanel;
		}

		public JPanel buildShellPanel() {
			// shell panel
			JPanel shellPanel = new JPanel();
			GridBagLayout gridbag = new GridBagLayout();
			shellPanel.setLayout(gridbag);

			GridBagConstraints c = new GridBagConstraints();

			c.anchor = GridBagConstraints.EAST;
			c.fill = GridBagConstraints.NONE;
			c.weightx = 0.0;
			c.gridy = 0;
			c.gridx = 0;
			shellPanel.add(new JLabel("project home: "), c);

			c.anchor = GridBagConstraints.CENTER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.5;
			c.gridy = 0;
			c.gridx = 1;
			JLabel label = new JLabel();
			shellPanel.add(label, c);

			c.anchor = GridBagConstraints.EAST;
			c.fill = GridBagConstraints.NONE;
			c.weightx = 0.1;
			c.gridy = 0;
			c.gridx = 2;
			JButton changeProjectFolderButton = new JButton("Change");
			shellPanel.add(changeProjectFolderButton, c);

			c.gridx = 0;
			c.gridy = 1;
			c.fill = GridBagConstraints.NONE;
			c.weightx = 0.0;
			shellPanel.add(new JLabel("command: "), c);

			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 2;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			JTextField commandLineBox = new JTextField();
			shellPanel.add(commandLineBox, c);

			return shellPanel;
		}

		public void buildGUI() {

			f.setTitle("UTGB Toolkit");

			ImageIcon imageIcon = new ImageIcon(FileResource.find(UTGBInstaller.class, "utgb-icon.png"));
			f.setIconImage(imageIcon.getImage());

			// Build install panel
			JPanel installPanel = buildInstallPanel();
			JPanel shellPanel = buildShellPanel();

			// set tab panel
			final JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
			tabPane.addTab("Install", installPanel);
			tabPane.addTab("Shell", shellPanel);

			final JTextPane console = new JTextPane();
			final JScrollPane consolePane = new JScrollPane(console);
			{
				// console panel
				consolePane.setPreferredSize(new Dimension(600, 200));
				consolePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				log = console.getStyledDocument();

			}

			final JPanel statusPanel = new JPanel();
			{
				// status panel
				final JLabel statusMessage = new JLabel();

				statusPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.WEST;
				c.fill = GridBagConstraints.NONE;
				c.weightx = 0.0;
				c.gridy = 0;
				c.gridx = 0;
				statusPanel.add(Box.createHorizontalStrut(2));
				c.anchor = GridBagConstraints.WEST;
				c.fill = GridBagConstraints.NONE;
				c.weightx = 0.0;
				c.gridy = 0;
				c.gridx = 1;
				statusPanel.add(statusMessage, c);
				c.anchor = GridBagConstraints.EAST;
				c.fill = GridBagConstraints.NONE;
				c.weightx = 1.0;
				c.gridy = 0;
				c.gridx = 2;
				statusPanel.add(progressBar, c);

				_logger.setLogWriter(new LogWriter() {

					public void log(Logger logger, LogLevel logLevel, Object message) throws IOException {
						if (message == null)
							return;
						try {
							String logMessage = message.toString();
							statusMessage.setText(logMessage);
							log.insertString(log.getLength(), String.format("[%s] %s\n", logger.getLoggerShortName(), logMessage), null);
							console.setCaretPosition(log.getLength());
						}
						catch (BadLocationException e) {
							System.err.println(e);
						}
					}

				});
			}

			JPanel layoutPanel;
			{
				// layout panel 
				layoutPanel = new JPanel();
				//layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
				layoutPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.anchor = GridBagConstraints.PAGE_START;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1.0;
				c.weighty = 0.0;
				c.gridy = 0;
				c.gridx = 0;
				layoutPanel.add(tabPane, c);
				c.anchor = GridBagConstraints.CENTER;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1.0;
				c.weighty = 1.0;
				c.gridy = 1;
				c.gridx = 0;
				layoutPanel.add(consolePane, c);
				c.anchor = GridBagConstraints.PAGE_END;
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 1.0;
				c.weighty = 0.0;
				c.gridy = 2;
				c.gridx = 0;
				layoutPanel.add(statusPanel, c);
			}

			// display the panel
			f.add(layoutPanel);
			f.pack();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			f.setLocation((int) d.getWidth() / 4, (int) d.getHeight() / 4);
			f.setVisible(true);
		}

		/**
		 * 
		 * Development note:
		 * 
		 * <ol>
		 * <li>Check the latest version of the utgb-shell by reading maven-metadata.xml.
		 * <li>Download the latest version to the $HOME/.utgb/lib folder if the latest jar file is newer than the
		 * locally installed one.
		 * <li>
		 * </ol>
		 * 
		 */
		public void getTheLatestVersionOfUTGBToolkit() {

			final String mavenRepository = "http://maven.utgenome.org/repository/artifact/org/utgenome/utgb-shell/";

			try {

				MavenMetadata m = Lens.loadXML(MavenMetadata.class, new URL(mavenRepository + "maven-metadata.xml"));

				URL archivePath = new URL(mavenRepository + m.getTarGZPackage());
				URLConnection conn = archivePath.openConnection();
				long releaseDate = conn.getLastModified();

				//				final String libPath = "lib/utgb-shell-standalone.jar";
				//				File localJAR = new File(installationFolder, libPath);

				//				boolean needsDownload = true;
				//				if (localJAR.exists()) {
				//					needsDownload = localJAR.lastModified() < releaseDate;
				//				}
				//
				//				if (!needsDownload) {
				//					int ret = JOptionPane.showConfirmDialog(f, "UTGB Toolkit is already installed. Reinstall?", "UTGB Installer", JOptionPane.YES_NO_OPTION,
				//							JOptionPane.QUESTION_MESSAGE);
				//
				//					if (ret != JOptionPane.YES_OPTION)
				//						return;
				//				}
				//				else {
				//					_logger.info(String.format("new version %s is available.", m.release));
				//				}

				//				{
				//					File parentFolder = localJAR.getParentFile();
				//					if (!parentFolder.exists())
				//						parentFolder.mkdirs();
				//				}

				// Download the jar file
				int jarSize = conn.getContentLength();
				progressBar.setMaximum(jarSize);
				progressBar.setValue(0);
				progressBar.setStringPainted(true);

				boolean success = false;
				try {
					_logger.info(String.format("Downloading UTGB Toolkit verison %s ...", m.release));
					File localToolkitArchive = new File(installationFolder, String.format("utgb-shell-%s-bin.tar.gz", m.release));
					File tmp = File.createTempFile(localToolkitArchive.getName(), ".download.tmp");
					tmp.deleteOnExit();

					FileOutputStream fout = new FileOutputStream(tmp);

					BufferedInputStream reader = new BufferedInputStream(conn.getInputStream());
					byte[] buf = new byte[8192];
					int readBytes = 0;
					int readTotal = 0;
					while ((readBytes = reader.read(buf)) != -1) {
						readTotal += readBytes;
						fout.write(buf, 0, readBytes);

						progressBar.setValue(readTotal);
					}
					fout.close();
					success = true;
					_logger.info("Downloaded " + jarSize + " bytes");
					progressBar.setValue(0);
					progressBar.setStringPainted(false);

					//					// copy bin/utgb, bin/utgb.bat
					//					copyScaffoldFromJar(localToolkitArchive, "org/utgenome/shell/script/", installationFolder);

					// Extract archive
					TarInputStream tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(tmp))));
					try {
						for (TarEntry entry; (entry = tarIn.getNextEntry()) != null;) {
							File f = entry.getFile();
							if (f.isDirectory()) {
								new File(installationFolder, f.getPath()).mkdirs();
								continue;
							}

							//copyFile(new InputStreamReader(tarIn), entry., dest)

						}
					}
					finally {
						tarIn.close();
					}

					// chmod script files
					String[] files = new String[] { "bin/utgb", "bin/utgb.bat" };
					File binFolder = new File(installationFolder, "bin");
					if (!binFolder.exists())
						binFolder.mkdirs();

					for (String f : files) {
						File target = new File(installationFolder, f);
						// chmod +x
						if (!System.getProperty("os.name").contains("Windows")) {
							try {
								Runtime.getRuntime().exec(new String[] { "chmod", "755", target.getAbsolutePath() }).waitFor();
							}
							catch (Throwable e) {
								throw new Exception(e);
							}
						}
					}

					_logger.info("Installation completed.");
				}
				catch (Exception e) {
					_logger.error(e);
					success = false;
				}

			}
			catch (Exception e) {
				_logger.error(e);
			}

		}

	}

	private GUIPanel panel = null;

	public void install() {

		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				public void run() {
					// build GUI 
					panel = new GUIPanel();
				}
			});
		}
		catch (Exception e) {
			_logger.error(e);
		}
	}

	private static class MavenMetadata {
		public String groupID;
		public String artifactID;
		public String release;

		public Versions versions = new Versions();

		public static class Versions {
			public ArrayList<String> version = new ArrayList<String>();
		}

		public String getTarGZPackage() {
			return String.format("%s/%s-%s-bin.tar.gz", release, artifactID, release);
		}
	}

	private static String extractLogicalName(String packagePath, String resourcePath) {
		if (!packagePath.endsWith("/"))
			packagePath = packagePath + "/";

		int pos = resourcePath.indexOf(packagePath);
		if (pos < 0)
			return null;

		String logicalName = resourcePath.substring(pos + packagePath.length());
		return logicalName;
	}

	/**
	 * Creates the folder structure for the Tomcat
	 * 
	 * @param catalinaBase
	 * @throws IOException
	 */
	public void copyScaffoldFromJar(File jarFile, String packagePath, String outputDir) throws IOException {

		// create the base folder for the scaffold
		File outputFolder = new File(outputDir);

		List<VirtualFile> scaffoldResourcesList = new ArrayList<VirtualFile>();

		JarFile jf = new JarFile(jarFile);
		for (Enumeration<JarEntry> entryEnum = jf.entries(); entryEnum.hasMoreElements();) {
			JarEntry jarEntry = entryEnum.nextElement();

			String physicalURL = "jar:" + jarFile.toURI().toURL() + "!/" + jarEntry.getName();
			URL jarFileURL = new URL(physicalURL);

			String logicalName = extractLogicalName(packagePath, jarEntry.getName());
			if (logicalName != null)
				scaffoldResourcesList.add(new FileInJarArchive(jarFileURL, logicalName, jarEntry.isDirectory()));
		}

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
			throw new IllegalStateException("No file is found in " + packagePath);

		// sync scaffoldDir with the output folder
		for (VirtualFile vf : scaffoldResources) {
			String srcLogicalPath = vf.getLogicalPath();
			if (vf.isDirectory()) {
				mkdirs(new File(outputFolder, srcLogicalPath));
			}
			else {

				String outputFileName = srcLogicalPath;
				File targetFile = new File(outputFolder, outputFileName);
				File parentFolder = targetFile.getParentFile();
				mkdirs(parentFolder);

				// copy the file content
				InputStream reader = vf.getURL().openStream();
				copyFile(reader, targetFile);
			}
		}
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
		_logger.info("create a file: " + getPath(dest));

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
		return f.getPath().replaceAll("\\\\", "/");
	}
}
