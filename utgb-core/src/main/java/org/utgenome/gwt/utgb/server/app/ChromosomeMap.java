//--------------------------------------
//
// ChromosomeMap.java
// Since: 2009/07/29
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.graphics.ChromosomeMapCanvas;
import org.utgenome.graphics.ChromosomeWindow;
import org.utgenome.gwt.utgb.client.bio.CytoBand;
import org.utgenome.gwt.utgb.client.bio.Locus;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.db.sql.ResultSetHandler;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.util.log.Logger;

/**
 * draw chromosome map
 * 
 */
public class ChromosomeMap extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(ChromosomeMap.class);

	public String species = "human";
	public String revision = "hg19";
	public String displayType = "normal";
	public int width = 700;
	public long start;
	public long end;
	public String dbGroup;
	public String dbName;
	public String bssQuery;
	public String name;

	private HashMap<String, ChromosomeWindow> chromWindows = new HashMap<String, ChromosomeWindow>();
	private List<String> chrNames = new ArrayList<String>();
	private List<CytoBand> cytoBandList = new ArrayList<CytoBand>();

	public ChromosomeMap() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			File cytoBandDb = new File(getProjectRootPath(), "db/" + species + "/" + revision + "/cytoBand/cytoBand.db");

			if (cytoBandDb.exists()) {
				SQLiteAccess dbAccess = new SQLiteAccess(cytoBandDb.getAbsolutePath());
				String sql = createSQLStatement("select * from entry");
				if (_logger.isDebugEnabled())
					_logger.debug(sql);
				dbAccess.query(sql, new ResultSetHandler() {
					@Override
					public Object handle(ResultSet rs) throws SQLException {
						CytoBand cytoBand = new CytoBand();
						cytoBand.setChrom(rs.getString(1));
						cytoBand.setStart(rs.getLong(2) + 1); // 1-origin
						cytoBand.setEnd(rs.getLong(3));
						cytoBand.setName(rs.getString(4));
						cytoBand.setGieStain(rs.getString(5));
						cytoBandList.add(cytoBand);

						if (chromWindows.containsKey(cytoBand.getChrom())) {
							// make chromosome range
							if (chromWindows.get(cytoBand.getChrom()).getChromosomeStart() > cytoBand.getStart()) {
								chromWindows.get(cytoBand.getChrom()).setChromosomeStart(cytoBand.getStart());
							}
							if (chromWindows.get(cytoBand.getChrom()).getChromosomeEnd() < cytoBand.getEnd()) {
								chromWindows.get(cytoBand.getChrom()).setChromosomeEnd(cytoBand.getEnd());
							}
						}
						else {
							// make chromosome window
							ChromosomeWindow window = new ChromosomeWindow(cytoBand.getStart(), cytoBand.getEnd());
							chromWindows.put(cytoBand.getChrom(), window);

							// make chromosome name list
							chrNames.add(cytoBand.getChrom());
						}

						return null;
					}
				});

				// make chromosome rank
				Object[] str = chrNames.toArray();
				Comparator comparator = new Comparator4ChrName();

				Arrays.sort(str, comparator);

				for (int i = 0; i < str.length; i++) {
					_logger.debug(str[i]);
					chromWindows.get(str[i]).setRank(i);
				}
			}
			// draw chromosome map canvas
			ChromosomeMapCanvas canvas = new ChromosomeMapCanvas(width, 100, chromWindows);

			//drawBlastResult(canvas);

			// draw cytoband
			if (displayType.equals("compact")) {
				canvas.setChromHeight(7);
				canvas.setChromMargin(3);
			}
			else if (displayType.equals("rotate")) {
				canvas.setRotate();
				canvas.setPixelHeight(300);
			}
			else {
				canvas.setChromHeight(10);
				canvas.setChromMargin(10);
			}

			canvas.setLighter(true);
			canvas.draw(cytoBandList);

			if (name != null)
				canvas.drawGenomeWindow(name, start, end);

			// query alignment result
			if (bssQuery != null) {
				String dbFolder = getTrackConfigProperty("utgb.db.folder", getProjectRootPath() + "/db");
				File dbFile = new File(dbFolder, dbGroup + "/" + dbName);

				if (dbFile.exists()) {
					SQLiteAccess dbAccess = new SQLiteAccess(dbFile.getAbsolutePath());
					String sql = createSQLFromFile("bss_whole.sql", bssQuery);
					List<ReadLocus> result = dbAccess.query(sql, ReadLocus.class);
					//_logger.info(Lens.toJSON(result));

					for (ReadLocus each : result)
						canvas.drawMapping(each.target, each.getStart(), each.getEnd(), each.getStrand());
				}
			}
			response.setContentType("image/png");
			canvas.toPNG(response.getOutputStream());
		}
		catch (Exception e) {
			_logger.error(e);
		}
	}

	public static class ReadLocus extends Locus {
		public String target;

		public void setTarget(String target) {
			this.target = target;
		}
	}

	public static class Comparator4ChrName implements Comparator<Object> {

		public int compare(Object a, Object b) {
			String p = (String) a;
			String q = (String) b;
			int x = p.length();
			int y = q.length();
			int n = Math.min(x, y);
			for (int i = 0; i < n; i++) {
				char c = p.charAt(i);
				char d = q.charAt(i);
				if (c != d) {
					boolean f = (c >= '0' && c <= '9');
					boolean g = (d >= '0' && d <= '9');
					if (f && !g) {
						return -1;
					}
					else if (!f && g) {
						return 1;
					}
					else if (!f && !g) {
						return c - d;
					}
					if (x != y) {
						return x - y;
					}
					return c - d;
				}
			}
			return x - y;
		}
	}

}
