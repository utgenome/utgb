//--------------------------------------
//
// MethylViewer.java
// Since: 2009/08/07
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.graphics.GeneCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.Locus;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.db.sql.BeanResultHandler;
import org.xerial.db.sql.DatabaseAccess;
import org.xerial.util.Pair;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class MethylViewer extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(MethylViewer.class);

	public String species = "medaka";
	public String revision = "version1.0";
	public String name = "scaffold1";
	public String db = "blastura";
	public long start = 1;
	public long end = 100000;
	public int width = 700;

	public MethylViewer() {
	}

	public static class MethlEntry extends Locus {
		public String scaffold;
		public String genome;
		public String tag;
		public int frequency;
		public int numCtoT;
		public int numOtherMismatch;

		public MethlEntry() {
			setName("");
		}

		/**
		 * list of C-C and C-T positions
		 * 
		 * @return
		 */
		public Pair<List<Integer>, List<Integer>> getMPos() {
			ArrayList<Integer> mPos = new ArrayList<Integer>();
			ArrayList<Integer> cPos = new ArrayList<Integer>();

			for (int i = 0; i < genome.length(); ++i) {
				char a = genome.charAt(i);
				char b = tag.charAt(i);

				if (a == 'C') {
					if (b == 'C') {
						mPos.add(i);
						continue;
					}
					else if (b == 'T') {
						cPos.add(i);
						continue;
					}
				}
			}

			boolean isReverse = isAntiSense();
			if (isReverse) {
				int width = (int) (getEnd() - getStart());
				for (int i = 0; i < mPos.size(); ++i)
					mPos.set(i, width - mPos.get(i));
				for (int i = 0; i < cPos.size(); ++i)
					cPos.set(i, width - cPos.get(i));
			}

			return new Pair<List<Integer>, List<Integer>>(mPos, cPos);
		}

		public void setScaffold(String scaffold) {
			this.scaffold = scaffold;
		}

		public void setGenome(String genome) {
			this.genome = genome;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}

		public void setNumCtoT(int numCtoT) {
			this.numCtoT = numCtoT;
		}

		public void setNumOtherMismatch(int numOtherMismatch) {
			this.numOtherMismatch = numOtherMismatch;
		}

	}

	List<MethlEntry> entryList;

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DatabaseAccess dbAccess = getDatabaseAccess(db);

			long s = start;
			long e = end;
			if (s > e) {
				long tmp = s;
				s = e;
				e = tmp;
			}

			final long TAG_LEN = 32;
			s -= TAG_LEN;
			if (s < 0)
				s = 0;

			String sql = createSQLFromFile("tagmap.sql", name, s, e);

			entryList = new ArrayList<MethlEntry>();

			dbAccess.query(sql, MethlEntry.class, new BeanResultHandler<MethlEntry>() {
				public void handle(MethlEntry e) throws SQLException {
					entryList.add(e);
				}

				public void finish() {

				}

				public void handleException(Exception e) throws Exception {
					_logger.error(e);
				}

				public void init() {

				}
			});

			GeneCanvas canvas = new GeneCanvas(width, 300, new GenomeWindow(start, end));
			canvas.setGeneHeight(8);
			canvas.draw(entryList);

			response.setContentType("image/png");
			canvas.toPNG(response.getOutputStream());

		}
		catch (Exception e) {
			_logger.error(e);
		}

	}
}
