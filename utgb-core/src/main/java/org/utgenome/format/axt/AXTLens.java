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
// AXTLens.java
// Since: 2010/11/26
//
//--------------------------------------
package org.utgenome.format.axt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.utgenome.UTGBErrorCode;
import org.utgenome.UTGBException;
import org.xerial.lens.ObjectHandler;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * Lens for AXT format
 * 
 * @author leo
 * 
 */
public class AXTLens {

	private static Logger _logger = Logger.getLogger(AXTLens.class);

	public static AXTAlignment lens(List<String> axtBlock) throws UTGBException {
		if (axtBlock.size() < 3) {
			throw new UTGBException(UTGBErrorCode.INVALID_FORMAT, "axtBlock.size() must be larger than 3: " + axtBlock.size());
		}

		AXTAlignment result = new AXTAlignment();
		result.parseSummaryLine(axtBlock.get(0));
		result.primaryAssembly = axtBlock.get(1);
		result.aligningAssembly = axtBlock.get(2);
		return result;
	}

	public static void lens(BufferedReader in, ObjectHandler<AXTAlignment> handler) throws UTGBException {

		try {
			handler.init();
			try {
				for (;;) {
					String line = in.readLine();
					if (line == null)
						break;

					if (StringUtil.isWhiteSpace(line))
						continue;

					List<String> axtBlock = new ArrayList<String>(3);

					String header = line;
					String seq1 = in.readLine();
					String seq2 = in.readLine();

					if (seq1 == null || seq2 == null) {
						break;
					}

					axtBlock.add(line);
					axtBlock.add(seq1);
					axtBlock.add(seq2);

					try {
						handler.handle(lens(axtBlock));
					}
					catch (Exception e) {
						_logger.error(e);
					}
				}
			}
			finally {
				handler.finish();
				if (in != null)
					in.close();
			}
		}
		catch (Exception e) {
			throw UTGBException.convert(e);
		}

	}

	public static void lens(File axtFile, ObjectHandler<AXTAlignment> handler) throws UTGBException {
		try {
			lens(new BufferedReader(new FileReader(axtFile)), handler);
		}
		catch (Exception e) {

		}

	}

}
