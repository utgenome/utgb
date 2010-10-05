/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// BlockwiseFileReader.java
// Since: 2010/10/05
//
//--------------------------------------
package org.utgenome.format;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Reading files in block-wise manner
 * 
 * @author leo
 * 
 */
public class BlockwiseFileReader {

	private final File file;
	private RandomAccessFile fileAccess;
	private final int blockSizeInMB;

	public BlockwiseFileReader(File file, int blockSizeInMB) throws FileNotFoundException {
		this.file = file;
		this.blockSizeInMB = blockSizeInMB;
		this.fileAccess = new RandomAccessFile(this.file, "r");
	}

	public long getFileSize() {
		return file.getTotalSpace();
	}

	public List<FileBlock> getBlockList() {
		final long blockByteSize = blockSizeInMB * 1024 * 1024;
		final long fileSize = getFileSize();
		final int numBlocks = (int) (fileSize / blockByteSize) + (fileSize % blockByteSize != 0 ? 1 : 0);

		List<FileBlock> blockList = new ArrayList<FileBlock>(numBlocks);
		long offset = 0;
		for (int i = 0; i < numBlocks; i++, offset += blockByteSize) {
			blockList.add(new FileBlock(file, i + 1, blockByteSize));
		}

		return blockList;
	}

}
