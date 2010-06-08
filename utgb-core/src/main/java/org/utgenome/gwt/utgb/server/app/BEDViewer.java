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
// utgb-core Project
//
// BEDViewer.java
// Since: 2009/05/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBException;
import org.utgenome.format.bed.BEDDatabase;
import org.utgenome.graphics.GeneCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.OnGenome;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

/**
 * BED viewer
 * 
 * @author leo
 * 
 */
public class BEDViewer extends WebTrackBase implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger _logger = Logger.getLogger(BEDViewer.class);

	public String species = "human";
	public String revision = "hg18";
	public String name = "chr22";
	public int start = 1;
	public int end = 1000000;
	public int width = 700;
	public String fileName;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			List<OnGenome> geneList = BEDDatabase.overlapQuery(new File(getProjectRootPath(), fileName), new ChrLoc(name, start, end));

			String suffix = getActionSuffix(request);

			if (suffix != null && suffix.equals("silk")) {
				response.setContentType("text/plain");
				response.getWriter().print(Lens.toSilk(geneList));
			}
			else {
				GeneCanvas geneCanvas = new GeneCanvas(width, 300, new GenomeWindow(start, end));
				geneCanvas.draw(geneList);

				response.setContentType("image/png");
				geneCanvas.toPNG(response.getOutputStream());
			}
		}
		catch (UTGBException e) {
			_logger.error(e);
			e.printStackTrace();
		}
	}

}
