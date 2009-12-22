//--------------------------------------
//
// ReadView.java
// Since: 2009/04/27
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.format.silk.read.Coordinate;
import org.utgenome.format.silk.read.Read;
import org.utgenome.format.silk.read.Reference;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.graphics.RibbonCanvas;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

/**
 * Request handler
 * 
 */
public class ReadView extends WebTrackBase
{
    private static final long serialVersionUID = 1L;
    private static Logger _logger = Logger.getLogger(ReadView.class);

    public ReadView()
    {}

    public long start = 0;
    public long end = 1000;
    public String strand = "+";
    public String group = "utgb";
    public String species = "medaka";
    public String revision = "version1.0";
    public String name = "scaffold3000";

    public int width = 700;

    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // input data file
        File readFile = new File(getProjectRootPath(), "db/" + name + ".silk");

        try
        {
            ReadPainter painter = new ReadPainter(start, end, width);
            Lens.loadSilk(painter, readFile.toURL());

            response.setContentType("image/png");
            painter.canvas.toPNG(response.getOutputStream());

        }
        catch (Exception e)
        {
            _logger.error(e);
        }

    }

    public static class ReferenceReader extends Reference
    {
        @Override
        public void appendSequence(String sequence)
        {

        }
    }

    public static class ReadPainter
    {
        final RibbonCanvas canvas;

        public Coordinate coordinate;

        public ReadPainter(long start, long end, int width)
        {
            canvas = new RibbonCanvas(width, 500, new GenomeWindow(start, end));
        }

        public void addReference(ReferenceReader reference)
        {
            _logger.info("reference: " + reference);
            canvas.setReference(reference);

        }

        public void addReference_read(ReferenceReader reference, Read read)
        {
            _logger.info(String.format("ref:%s, read: %s", reference.hashCode(), read));
            canvas.draw(read);
        }

    }

}
