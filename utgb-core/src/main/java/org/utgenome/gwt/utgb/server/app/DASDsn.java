//--------------------------------------
//
// DASDns.java
// Since: 2009/05/28
//
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.core.XerialException;
import org.xerial.lens.Lens;
import org.xerial.lens.ObjectLens;
import org.xerial.util.log.Logger;

/**
 * Read DAS DSN
 * 
 */
public class DASDsn extends WebTrackBase
{
    private static final long serialVersionUID = 1L;
    private static Logger _logger = Logger.getLogger(DASDsn.class);

    private String dasDSNURL = "http://www.ensembl.org/das/dsn";

    public DASDsn()
    {}

    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        try
        {
            DSNQuery result = Lens.loadXML(DSNQuery.class, new URL(dasDSNURL));
            response.getWriter().print(ObjectLens.toJSON(result.dsn));
        }
        catch (XerialException e)
        {
            _logger.error(e);
        }
    }

    public static class DSNQuery
    {
        public List<DSN> dsn;
    }

    public static class DSN
    {
        public String href;
        public DSNSource source;
        public String mapMaster;
        public String description;
    }

    public static class DSNSource
    {
        public String id;
        public String value;
    }

}
