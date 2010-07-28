//--------------------------------------
//
// SubSequence.java
// Since: 2008/01/25
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.UTGBException;
import org.utgenome.format.fasta.FASTADatabase;
import org.utgenome.format.fasta.FASTADatabase.NSeq;
import org.utgenome.graphics.GenomeCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.utgenome.gwt.utgb.server.util.graphic.GraphicUtil;
import org.xerial.db.sql.BeanResultHandler;
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
	public int start = 0;
	public int end = 10000;
	public String name = "chr1";
	public int width = DEFAULT_WIDTH;
	public String path = null;

	public String colorA = DEFAULT_COLOR_A;
	public String colorC = DEFAULT_COLOR_C;
	public String colorG = DEFAULT_COLOR_G;
	public String colorT = DEFAULT_COLOR_T;
	public String colorN = DEFAULT_COLOR_N;

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

	public static abstract class SequenceRetrieverBase implements BeanResultHandler<NSeq> {

		private static Logger _logger = Logger.getLogger(SequenceRetrieverBase.class);

		private final int start;
		private final int end;
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

		public SequenceRetrieverBase(int start, int end, boolean isReverseStrand) {

			assert (start <= end);
			this.start = start;
			this.end = end;

			this.isReverseStrand = isReverseStrand;
		}

		public void handle(NSeq seq) throws SQLException {
			int rangeStart = ((seq.getStart() < start) ? start - seq.getStart() : 0);
			int rangeEnd = ((end > seq.getEnd()) ? seq.getLength() : end - seq.getStart() + 1);
			output(seq.getSubSequence(rangeStart, rangeEnd));
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
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

		public void init() {

		}

		public void finish() {

		}

		public void handleException(Exception e) throws Exception {
			_logger.error(e);
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

		public TextOutput(PrintWriter writer, int start, int end, boolean isReverseStrand) {
			super(start, end, isReverseStrand);
			this.writer = writer;
		}

		@Override
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

		public XMLOutput(PrintWriter writer, int start, int end, boolean isReverseStrand) {
			super(start, end, isReverseStrand);
			xml = new XMLGenerator(writer);
		}

		@Override
		public void init() {
			xml.startTag("sequence", new XMLAttribute().add("start", getStart()).add("end", getEnd()));
		}

		@Override
		public void output(String subSequence) {
			xml.text(subSequence);
		}

		@Override
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

		public JSONOutput(PrintWriter writer, int start, int end, boolean isReverseStrand) {
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

		@Override
		public void output(String subSequence) {
			try {
				jsonWriter.append(subSequence);
			}
			catch (Exception e) {
				_logger.error(e);
			}
		}

		@Override
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
		protected int startOffset;
		protected int endOffset;
		private HashMap<Character, Color> colorTable = new HashMap<Character, Color>();
		private final HttpServletResponse response;
		private boolean drawBase = false;

		public GraphicalOutput(HttpServletResponse response, int start, int end, int width, boolean isReverseStrand) {
			super(start, end, isReverseStrand);
			this.response = response;
			canvas = new GenomeCanvas(width, DEFAULT_HEIGHT, new GenomeWindow(start, end));
			this.startOffset = start;
			this.endOffset = end + 1;

			int seqWidth = end - start;
			final int FONT_WIDTH = 7;
			if (seqWidth <= width / FONT_WIDTH)
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
					canvas.drawGeneRect(startOffset, startOffset + 1, 0, DEFAULT_HEIGHT, getColor(ch));
					if (drawBase)
						canvas.drawBase(subSequence.substring(i, i + 1), startOffset, startOffset + 1, DEFAULT_HEIGHT - 2, FONT_SIZE, textColor);

					startOffset++;
				}
				else {
					char reverse = getComplement(ch);
					canvas.drawGeneRect(endOffset - 1, endOffset, 0, DEFAULT_HEIGHT, getColor(reverse));
					if (drawBase)
						canvas.drawBase(Character.toString(reverse), endOffset - 1, endOffset, DEFAULT_HEIGHT - 2, FONT_SIZE, textColor);

					endOffset--;
				}

			}
		}

		@Override
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

		public RoughGraphicalOutput(HttpServletResponse response, int start, int end, int width, boolean isReverseStrand) {
			super(response, start, end, width, isReverseStrand);

			int range = end - start;
			loopSequenceWidth = (int) (range / width + 0.5);

		}

		@Override
		public void output(String subSequence) {
			int rangeEnd = startOffset + subSequence.length();
			for (int pos = startOffset + startOffset % loopSequenceWidth; pos < rangeEnd; pos += loopSequenceWidth) {

				char ch = subSequence.charAt((pos - startOffset));
				if (!isReverseStrand()) {
					canvas.drawGeneRect(pos, pos + loopSequenceWidth, 0, DEFAULT_HEIGHT, getColor(ch));
				}
				else {
					canvas.drawGeneRect(getEnd() - (pos + loopSequenceWidth), getEnd() - pos, 0, DEFAULT_HEIGHT, getColor(getComplement(ch)));
				}
			}

			startOffset += subSequence.length();
		}

	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			File dbFile = null;
			if (path == null) {
				String dbFolder = getTrackConfigProperty("utgb.db.folder", getProjectRootPath() + "/db");
				dbFile = new File(dbFolder, String.format("%s/%s/%s.sqlite", dbGroup, species, revision));
			}
			else
				dbFile = new File(getProjectRootPath(), path);

			ChrLoc queryTarget = new ChrLoc(name, start, end);
			int range = queryTarget.length();
			boolean isReverseStrand = queryTarget.isAntiSense();

			String actionString = getActionSuffix(request);
			BeanResultHandler<NSeq> handler = null;
			if (actionString.equals("json"))
				handler = new JSONOutput(response.getWriter(), start, end, isReverseStrand);
			else if (actionString.equals("xml"))
				handler = new XMLOutput(response.getWriter(), start, end, isReverseStrand);
			else if (actionString.equals("png")) {
				if (range > width)
					handler = new RoughGraphicalOutput(response, start, end, width, isReverseStrand);
				else
					handler = new GraphicalOutput(response, start, end, width, isReverseStrand);
			}
			else
				handler = new TextOutput(response.getWriter(), start, end, isReverseStrand);

			FASTADatabase.querySequence(dbFile, new ChrLoc(name, start, end), handler);

		}
		catch (UTGBException e) {
			_logger.error(e);
			e.printStackTrace();
		}
	}

}
