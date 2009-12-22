//--------------------------------------
//
// SubSequence.java
// Since: 2008/01/25
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBException;
import org.utgenome.graphics.GenomeCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;
import org.xerial.db.DBException;
import org.xerial.db.sql.BeanResultHandler;
import org.xerial.db.sql.ByteArray;
import org.xerial.db.sql.DatabaseAccess;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.json.JSONWriter;
import org.xerial.util.log.Logger;
import org.xerial.util.xml.XMLAttribute;
import org.xerial.util.xml.XMLGenerator;

/**
 * SubSequence retrieves a genome sequence of the specified target in the range (start, end) (inclusive)
 * 
 * 
 * @author leo
 * 
 */
public class Sequence extends WebTrackBase {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = Logger.getLogger(Sequence.class);

	// default values
	private static final int DEFAULT_WIDTH = 800;
	private static final String DEFAULT_COLOR_A = "50B6E8";
	private static final String DEFAULT_COLOR_C = "E7846E";
	private static final String DEFAULT_COLOR_G = "84AB51";
	private static final String DEFAULT_COLOR_T = "FFE980";
	private static final String DEFAULT_COLOR_N = "EEEEEE";

	public String dbGroup = "org/utgenome/leo/genome";
	public String species = "human";
	public String revision = "hg19";
	public long start = 0;
	public long end = 10000;
	public String name = "chr1";
	public int width = DEFAULT_WIDTH;

	public String colorA = DEFAULT_COLOR_A;
	public String colorC = DEFAULT_COLOR_C;
	public String colorG = DEFAULT_COLOR_G;
	public String colorT = DEFAULT_COLOR_T;
	public String colorN = DEFAULT_COLOR_N;

	public static class NSeq {
		private long start;
		private long end;
		private ByteArray sequence;

		public NSeq() {
		}

		public NSeq(long start, long end, ByteArray sequence) {
			this.start = start;
			this.end = end;
			this.sequence = sequence;
		}

		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append("(");
			buf.append(start);
			buf.append(",");
			buf.append(end);
			buf.append(")");
			return buf.toString();
		}

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getEnd() {
			return end;
		}

		public void setEnd(long end) {
			this.end = end;
		}

		public ByteArray getSequence() {
			return sequence;
		}

		public String getSubSequence(int start, int end) {
			byte[] seq = sequence.getBytes();
			return new String(seq, start, end - start);
		}

		public int getLength() {
			return this.sequence.size();
		}

		public void setSequence(ByteArray sequence) throws IOException {
			GZIPInputStream decompressor = new GZIPInputStream(new ByteArrayInputStream(sequence.getBytes()));
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int readBytes = 0;
			while ((readBytes = decompressor.read(buf)) != -1) {
				b.write(buf, 0, readBytes);
			}
			this.sequence = new ByteArray(b.toByteArray());
		}

	}

	public Sequence() {
	}

	@Override
	public void validate(HttpServletRequest request, HttpServletResponse response) throws ServletException, UTGBException {
		if (width < 0)
			width = DEFAULT_WIDTH;

		if (start < 0 || end < 0) {
			start = 0;
			end = 0;
		}
	}

	public static abstract class SequenceRetrieverBase extends BeanResultHandler<NSeq> {
		private final long start;
		private final long end;
		private final boolean isReverseStrand;

		private static HashMap<Character, Character> reverseStrandTable = new HashMap<Character, Character>();

		static {
			reverseStrandTable.put('a', 't');
			reverseStrandTable.put('A', 'T');
			reverseStrandTable.put('g', 'c');
			reverseStrandTable.put('G', 'C');
			reverseStrandTable.put('t', 'a');
			reverseStrandTable.put('T', 'A');
			reverseStrandTable.put('c', 'g');
			reverseStrandTable.put('C', 'G');
		}

		public SequenceRetrieverBase(long start, long end, boolean isReverseStrand) {
			super(NSeq.class);

			assert (start <= end);
			this.start = start;
			this.end = end;

			this.isReverseStrand = isReverseStrand;
		}

		public void handle(NSeq seq) throws SQLException {
			int rangeStart = (int) ((seq.getStart() < start) ? start - seq.getStart() : 0);
			int rangeEnd = (int) ((end > seq.getEnd()) ? seq.getLength() : end - seq.getStart() + 1);
			output(seq.getSubSequence(rangeStart, rangeEnd));
		}

		public long getStart() {
			return start;
		}

		public long getEnd() {
			return end;
		}

		public boolean isReverseStrand() {
			return isReverseStrand;
		}

		public abstract void output(String subSequence);

		public char getComplement(char base) {
			Character ch = reverseStrandTable.get(base);
			if (ch == null)
				return 'N';
			else
				return ch.charValue();
		}
	}

	/**
	 * Output a sequence as text
	 * 
	 * @author leo
	 * 
	 */
	public static class TextOutput extends SequenceRetrieverBase {
		private final PrintWriter writer;

		public TextOutput(PrintWriter writer, long start, long end, boolean isReverseStrand) {
			super(start, end, isReverseStrand);
			this.writer = writer;
		}

		public void output(String subSequence) {
			writer.print(subSequence);
		}

	}

	/**
	 * Output a sequence as XML
	 * 
	 * @author leo
	 * 
	 */
	public static class XMLOutput extends SequenceRetrieverBase {
		private final XMLGenerator xml;

		public XMLOutput(PrintWriter writer, long start, long end, boolean isReverseStrand) {
			super(start, end, isReverseStrand);
			xml = new XMLGenerator(writer);
		}

		public void init() {
			xml.startTag("sequence", new XMLAttribute().add("start", getStart()).add("end", getEnd()));
		}

		public void output(String subSequence) {
			xml.text(subSequence);
		}

		public void finish() {

			xml.endDocument();

		}

	}

	/**
	 * Output a sequence as JSON
	 * 
	 * @author leo
	 * 
	 */
	public static class JSONOutput extends SequenceRetrieverBase {
		private final JSONWriter jsonWriter;

		public JSONOutput(PrintWriter writer, long start, long end, boolean isReverseStrand) {
			super(start, end, isReverseStrand);
			jsonWriter = new JSONWriter(writer);
		}

		@Override
		public void init() {
			try {
				jsonWriter.startObject();
				jsonWriter.put("start", getStart());
				jsonWriter.put("end", getEnd());
				jsonWriter.startString("sequence");
			}
			catch (Exception e) {
				_logger.error(e);
			}

		}

		public void output(String subSequence) {
			try {
				jsonWriter.append(subSequence);
			}
			catch (Exception e) {
				_logger.error(e);
			}
		}

		public void finish() {
			try {
				jsonWriter.endJSON();
			}
			catch (Exception e) {
				_logger.error(e);
			}
		}

	}

	public class GraphicalOutput extends SequenceRetrieverBase {
		public static final int DEFAULT_HEIGHT = 12;
		public static final float FONT_SIZE = 10.5f;
		private Color UNKNOWN_BASE_COLOR = GraphicUtil.parseColor(colorN);

		protected final GenomeCanvas canvas;
		protected long startOffset;
		protected long endOffset;
		private HashMap<Character, Color> colorTable = new HashMap<Character, Color>();
		private final HttpServletResponse response;
		private boolean drawBase = false;

		public GraphicalOutput(HttpServletResponse response, long start, long end, int width, boolean isReverseStrand) {
			super(start, end, isReverseStrand);
			this.response = response;
			canvas = new GenomeCanvas(width, DEFAULT_HEIGHT, new GenomeWindow(start, end));
			this.startOffset = start;
			this.endOffset = end + 1;

			long seqWidth = end - start;
			if (seqWidth <= 100)
				drawBase = true;

			int repeatColorAlpha = 50;
			colorTable.put('a', GraphicUtil.parseColor(colorA, repeatColorAlpha));
			colorTable.put('c', GraphicUtil.parseColor(colorC, repeatColorAlpha));
			colorTable.put('g', GraphicUtil.parseColor(colorG, repeatColorAlpha));
			colorTable.put('t', GraphicUtil.parseColor(colorT, repeatColorAlpha));
			colorTable.put('A', GraphicUtil.parseColor(colorA));
			colorTable.put('C', GraphicUtil.parseColor(colorC));
			colorTable.put('G', GraphicUtil.parseColor(colorG));
			colorTable.put('T', GraphicUtil.parseColor(colorT));
		}

		public Color getColor(char base) {
			Color color = colorTable.get(base);
			if (color == null)
				return UNKNOWN_BASE_COLOR;
			else
				return color;
		}

		@Override
		public void output(String subSequence) {
			Color textColor = new Color(80, 80, 80);
			for (int i = 0; i < subSequence.length(); i++) {
				char ch = subSequence.charAt(i);
				if (!isReverseStrand()) {
					canvas.drawRect(startOffset, startOffset + 1, 0, DEFAULT_HEIGHT, getColor(ch));
					if (drawBase)
						canvas.drawBase(subSequence.substring(i, i + 1), startOffset, startOffset + 1, DEFAULT_HEIGHT - 2, FONT_SIZE, textColor);

					startOffset++;
				}
				else {
					char reverse = getComplement(ch);
					canvas.drawRect(endOffset - 1, endOffset, 0, DEFAULT_HEIGHT, getColor(reverse));
					if (drawBase)
						canvas.drawBase(Character.toString(reverse), endOffset - 1, endOffset, DEFAULT_HEIGHT - 2, FONT_SIZE, textColor);

					endOffset--;
				}

			}
		}

		public void finish() {
			try {
				canvas.outputImage(response, "png");
			}
			catch (IOException e) {
				_logger.error(e);
			}
		}

	}

	private class RoughGraphicalOutput extends GraphicalOutput {

		private int loopSequenceWidth;

		public RoughGraphicalOutput(HttpServletResponse response, long start, long end, int width, boolean isReverseStrand) {
			super(response, start, end, width, isReverseStrand);

			long range = end - start;
			loopSequenceWidth = (int) (range / width + 0.5);

		}

		public void output(String subSequence) {
			long rangeEnd = startOffset + subSequence.length();
			for (long pos = startOffset + startOffset % loopSequenceWidth; pos < rangeEnd; pos += loopSequenceWidth) {
				char ch = subSequence.charAt((int) (pos - startOffset));
				if (!isReverseStrand()) {
					canvas.drawRect(pos, pos + loopSequenceWidth, 0, DEFAULT_HEIGHT, getColor(ch));
				}
				else {
					canvas.drawRect(getEnd() - (pos + loopSequenceWidth), getEnd() - pos, 0, DEFAULT_HEIGHT, getColor(getComplement(ch)));
				}
			}

			startOffset += subSequence.length();
		}

	}

	private static final int SEQUENCE_FRAGMENT_LENGTH = 10000;

	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			String dbFolder = getTrackConfigProperty("utgb.db.folder", getProjectRootPath() + "/db");
			File dbFile = new File(dbFolder, String.format("%s/%s/%s.sqlite", dbGroup, species, revision));

			if (!dbFile.exists())
				return;

			DatabaseAccess db = new SQLiteAccess(dbFile.getAbsolutePath());

			boolean isReverseStrand = false;
			if (end < start) {
				long tmp = end;
				end = start;
				start = tmp;
				isReverseStrand = true;
			}

			long searchStart = (start / SEQUENCE_FRAGMENT_LENGTH) * SEQUENCE_FRAGMENT_LENGTH + 1;

			long range = (end - start) + 1;

			String sql = createSQLFromFile("sequence.sql", name, searchStart, end, start);
			String actionString = getActionSuffix(request);

			if (actionString.equals("json"))
				db.query(sql, new JSONOutput(response.getWriter(), start, end, isReverseStrand));
			else if (actionString.equals("xml"))
				db.query(sql, new XMLOutput(response.getWriter(), start, end, isReverseStrand));
			else if (actionString.equals("png")) {
				if (range > width)
					db.query(sql, new RoughGraphicalOutput(response, start, end, width, isReverseStrand));
				else
					db.query(sql, new GraphicalOutput(response, start, end, width, isReverseStrand));
			}
			else
				db.query(sql, new TextOutput(response.getWriter(), start, end, isReverseStrand));
		}
		catch (DBException e) {
			_logger.error(e);
		}
		catch (UTGBException e) {
			_logger.error(e);
		}

	}

}
