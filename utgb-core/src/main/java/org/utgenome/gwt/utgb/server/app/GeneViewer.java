//--------------------------------------
//
// GeneViewer.java
// Since: 2009/01/15
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.graphics.GeneCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.util.bean.BeanHandler;
import org.xerial.util.bean.BeanUtil;
import org.xerial.util.log.Logger;

/**
 * Gene Viewer
 * 
 * 
 */
public class GeneViewer extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(GeneViewer.class);

	private String url = "http://utgenome.org/api/refseq/human/hg18/chr1:1-1000000/list.json";

	private String species = "human";
	private String revision = "hg18";
	private String name = "chr1";
	private long start = 1;
	private long end = 1000000;
	private int width = 800;

	public GeneViewer() {
	}

	static class GeneRetriever<T> implements BeanHandler<T> {
		private ArrayList<T> geneList = new ArrayList<T>();

		public GeneRetriever() {
		}

		public ArrayList<T> getResult() {
			return geneList;
		}

		public void handle(T bean) throws Exception {
			geneList.add(bean);
		}
	}

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String refseqURL = String.format("http://utgenome.org/api/refseq/%s/%s/%s:%d-%d/list.json", species, revision, name, start, end);
		URL apiURL = new URL(refseqURL);

		// retrieve gene data from the UTGB web API

		GeneRetriever<Gene> geneRetriever = new GeneRetriever<Gene>();
		try {

			BeanUtil.loadJSON(new InputStreamReader(apiURL.openStream()), Gene.class, geneRetriever);

			String actionSuffix = getActionSuffix(request);
			if (actionSuffix.equals("tab")) {
				response.setContentType("text/plain");
				for (Gene each : geneRetriever.getResult()) {
					response.getWriter().println(String.format("%s\t%s\t%s", each.getName(), each.getStart(), each.getStrand()));
				}
			}
			else {
				GeneCanvas geneCanvas = new GeneCanvas(width, 300, new GenomeWindow(start, end));
				geneCanvas.draw(geneRetriever.getResult());

				response.setContentType("image/png");
				geneCanvas.toPNG(response.getOutputStream());
			}
		}
		catch (Exception e) {
			_logger.error(e);
		}
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

}
