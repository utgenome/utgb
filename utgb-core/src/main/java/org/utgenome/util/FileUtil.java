/*--------------------------------------------------------------------------
 *  Copyright 2011 utgenome.org
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
// utgb-core Project
//
// FileUtil.java
// Since: 2011/03/24
//
//--------------------------------------
package org.utgenome.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.xerial.util.log.Logger;

public class FileUtil {

	private static Logger _logger = Logger.getLogger(FileUtil.class);

	public static void extractTarGZ(URL tarArchive, File outputFolder) throws IOException {

		TarInputStream tis = new TarInputStream(new GZIPInputStream(new BufferedInputStream(tarArchive.openStream())));
		try {
			TarEntry nextEntry = null;
			while ((nextEntry = tis.getNextEntry()) != null) {
				int mode = nextEntry.getMode();
				String name = nextEntry.getName();
				Date modTime = nextEntry.getModTime();

				File extractedFile = new File(outputFolder, name);

				if (extractedFile.exists() && extractedFile.lastModified() == modTime.getTime())
					continue;

				if (!nextEntry.isDirectory()) {
					_logger.info(String.format("extracted %s into %s", name, outputFolder.getPath()));

					File parent = extractedFile.getParentFile();
					if (parent != null && !parent.exists())
						parent.mkdirs();

					BufferedOutputStream fo = new BufferedOutputStream(new FileOutputStream(extractedFile));
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

				extractedFile.setLastModified(modTime.getTime());
			}
		}
		finally {
			tis.close();
		}

	}
}
