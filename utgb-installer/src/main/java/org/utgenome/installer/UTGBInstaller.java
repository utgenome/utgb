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
import java.io.Writer;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.xerial.lens.Lens;
import org.xerial.util.FileResource;
import org.xerial.util.FileResource.FileInJarArchive;
import org.xerial.util.io.VirtualFile;
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
		final JLabel iLabel = new JLabel(installationFolder);
		final JButton iButton = new JButton("Change");
		final JButton next = new JButton("Install >");
		final JTextPane console = new JTextPane();
		final JScrollPane consolePane = new JScrollPane(console);

		final JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);

		StyledDocument log;
		ProgressMonitor pm;

		private ExecutorService exec = Executors.newFixedThreadPool(1);

		public GUIPanel() {
			buildGUI();
		}

		public void buildGUI() {

			f.setTitle("UTGB Toolkit");

			ImageIcon imageIcon = new ImageIcon(FileResource.find(UTGBInstaller.class, "utgb-icon.png"));
			f.setIconImage(imageIcon.getImage());

			// track project folder

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

			// console panel
			consolePane.setPreferredSize(new Dimension(600, 200));
			consolePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			log = console.getStyledDocument();

			_logger.setOutputWriter(new Writer() {
				@Override
				public void close() throws IOException {

				}

				@Override
				public void flush() throws IOException {

				}

				@Override
				public void write(char[] cbuf, int off, int len) throws IOException {

					try {
						log.insertString(log.getLength(), new String(cbuf, off, len), null);
						console.setCaretPosition(log.getLength());
					}
					catch (BadLocationException e) {
						System.err.println(e);
					}
				}
			});

			// install panel layout
			JPanel installPanel = new JPanel();
			installPanel.setLayout(new BoxLayout(installPanel, BoxLayout.Y_AXIS));
			installPanel.add(iPanel);
			installPanel.add(buttonPanel);

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

			// set tab panel
			tabPane.addTab("Shell", shellPanel);
			tabPane.addTab("Install", installPanel);

			// layout panel
			JPanel layoutPanel = new JPanel();
			layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
			layoutPanel.add(tabPane);
			layoutPanel.add(consolePane);

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
			final String libPath = "lib/utgb-shell-standalone.jar";
			try {

				MavenMetadata m = Lens.loadXML(MavenMetadata.class, new URL(mavenRepository + "maven-metadata.xml"));

				URL jarPath = new URL(mavenRepository + m.getStandaloneJAR());
				URLConnection conn = jarPath.openConnection();
				long releaseDate = conn.getLastModified();

				File localJAR = new File(installationFolder, libPath);

				boolean needsDownload = true;
				if (localJAR.exists()) {
					needsDownload = localJAR.lastModified() < releaseDate;

				}

				if (!needsDownload) {
					int ret = JOptionPane.showConfirmDialog(f, "UTGB Toolkit is already installed. Reinstall?", "UTGB Installer", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (ret != JOptionPane.YES_OPTION)
						return;
				}
				else {
					_logger.info(String.format("new version %s is available.", m.release));
				}

				{
					File parentFolder = localJAR.getParentFile();
					if (!parentFolder.exists())
						parentFolder.mkdirs();
				}

				// download jar
				int jarSize = conn.getContentLength();
				pm = new ProgressMonitor(f, "Downloading UTGB Toolkit", "", 0, jarSize);
				pm.setMillisToDecideToPopup(0);
				pm.setMillisToPopup(0);
				pm.setProgress(0);

				boolean success = false;
				try {

					_logger.info("downloading UTGB Toolkit");
					File tmp = File.createTempFile(localJAR.getName(), ".download.tmp");
					tmp.deleteOnExit();

					FileOutputStream fout = new FileOutputStream(tmp);

					BufferedInputStream reader = new BufferedInputStream(conn.getInputStream());
					byte[] buf = new byte[8192];
					int readBytes = 0;
					int readTotal = 0;
					while ((readBytes = reader.read(buf)) != -1) {
						readTotal += readBytes;
						fout.write(buf, 0, readBytes);

						pm.setProgress(readTotal);
						if (pm.isCanceled()) {
							throw new Exception("cancelled");
						}
					}
					fout.close();
					success = true;
					_logger.info("download done: " + jarSize + " bytes");

					copyFile(new BufferedInputStream(new FileInputStream(tmp)), localJAR);

					// copy bin/utgb, bin/utgb.bat
					copyScaffoldFromJar(localJAR, "org/utgenome/shell/script/", installationFolder);

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
							}
						}
					}

					_logger.info("installation completed.");
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

		public String getStandaloneJAR() {
			return String.format("%s/%s-%s-standalone.jar", release, artifactID, release);
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

			String physicalURL = "jar:" + jarFile.toURL() + "!/" + jarEntry.getName();
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
