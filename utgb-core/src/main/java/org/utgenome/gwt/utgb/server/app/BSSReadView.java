//--------------------------------------
//
// BSSReadView.java
// Since: Oct 14, 2009
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.client.bio.Locus;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.util.StringUtil;
import org.xerial.util.log.Logger;

/**
 * BSS Read Viewer
 * 
 */
public class BSSReadView extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(BSSReadView.class);

	public String bssQuery = null;
	public String name = "chr1";
	public String dbGroup;
	public String dbName;
	public int width = 700;

	public BSSReadView() {

	}

	public static class BSSAlignment extends Locus {
		public double similarity;
		public double queryCoverage;
		public int queryLength;
		public String evalue;
		public int bitScore;
		public String targetSequence;
		public String querySequence;
		public String alignment;
		public String target;

		public void setTarget(String target) {
			this.target = target;
		}

		public void setQueryLength(int queryLength) {
			this.queryLength = queryLength;
		}

		public void setSimilarity(double similarity) {
			this.similarity = similarity;
		}

		public void setQueryCoverage(double queryCoverage) {
			this.queryCoverage = queryCoverage;
		}

		public void setEvalue(String evalue) {
			this.evalue = evalue;
		}

		public void setBitScore(int bitScore) {
			this.bitScore = bitScore;
		}

		public void setTargetSequence(String targetSequence) {
			this.targetSequence = targetSequence;
		}

		public void setQuerySequence(String querySequence) {
			this.querySequence = querySequence;
		}

		public void setAlignment(String alignment) {
			this.alignment = alignment;
		}

		public String toAlignmentView() {
			final int w = 50;
			StringBuilder buf = new StringBuilder();
			buf.append(String.format("query: %s (length=%d, %s:%s-%s strand:%s)", getName(), queryLength, target, getStart(), getEnd(), getStrand()));
			buf.append(StringUtil.NEW_LINE);
			buf.append(String.format("       similarity:%.0f%%, query coverage=%.0f%%, e-value=%s, bit score=%s", similarity, queryCoverage, evalue, bitScore));
			buf.append(StringUtil.NEW_LINE);
			buf.append(StringUtil.NEW_LINE);
			for (int i = 0; i < alignment.length(); i += w) {
				buf.append("target: ");
				buf.append(substr(targetSequence, i, w));
				buf.append(StringUtil.NEW_LINE);

				buf.append("        ");
				buf.append(substr(alignment, i, w));
				buf.append(StringUtil.NEW_LINE);

				buf.append("query:  ");
				buf.append(substr(querySequence, i, w));
				buf.append(StringUtil.NEW_LINE);
				buf.append(StringUtil.NEW_LINE);
			}
			return buf.toString();
		}

		private static String substr(String s, int offset, int len) {
			try {
				if (offset + len < s.length() - 1)
					return s.substring(offset, offset + len);
				else
					return s.substring(offset);
			}
			catch (StringIndexOutOfBoundsException e) {
				_logger.error(e);
				return "";
			}
		}

	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/plain");

		if (bssQuery == null || dbGroup == null || dbName == null)
			return;

		String dbFolder = getTrackConfigProperty("utgb.db.folder", getProjectRootPath() + "/db");
		File dbFile = new File(dbFolder, dbGroup + "/" + dbName);

		if (!dbFile.exists())
			return;

		try {
			SQLiteAccess dbAccess = new SQLiteAccess(dbFile.getAbsolutePath());
			String sql = createSQLFromFile("bss_range.sql", bssQuery);

			//_logger.info(sql);

			List<BSSAlignment> result = dbAccess.query(sql, BSSAlignment.class);

			//_logger.info(Lens.toJSON(result));

			StringBuilder b = new StringBuilder();
			b.append("#matches: " + result.size());
			b.append(StringUtil.NEW_LINE);
			for (BSSAlignment each : result) {
				if (each.target.equals(name))
					b.append(each.toAlignmentView());
			}
			for (BSSAlignment each : result) {
				if (!each.target.equals(name))
					b.append(each.toAlignmentView());
			}

			response.getWriter().write(b.toString());
		}
		catch (Exception e) {
			_logger.error(e);
		}

	}

}
