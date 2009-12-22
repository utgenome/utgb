/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// utgb-shell Project
//
// WIG2SilkReader.java
// Since: Aug 28, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-shell/src/main/java/org/utgenome/shell/db/wig/WIG2SilkReader.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.format.wig;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.utgenome.UTGBException;
import org.xerial.util.log.Logger;

public class WIG2SilkReader extends Reader
{

    private static Logger _logger = Logger.getLogger(WIG2SilkReader.class);

    private final Reader wigReader;
    private ExecutorService threadPool;

    private final PipedWriter pipeOut;
    private final PipedReader pipeIn;

    private boolean hasStarted = false;

    public WIG2SilkReader(Reader wigReader) throws IOException
    {
        this.wigReader = wigReader;

        pipeOut = new PipedWriter();
        pipeIn = new PipedReader(pipeOut);

    }

    private static class PipeWorker implements Runnable
    {

        private final WIG2Silk wig2silk;
        private final PrintWriter out;

        public PipeWorker(Reader in, PrintWriter out) throws IOException
        {
            wig2silk = new WIG2Silk(in);
            this.out = out;
        }

        public void run()
        {
            if (out == null)
                return;
            try
            {
                wig2silk.toSilk(out);
                out.close();
            }
            catch (IOException e)
            {
                _logger.error(e);
            }
            catch (UTGBException e)
            {
                _logger.error(e);
            }
        }

    }

    @Override
    public void close() throws IOException
    {
        pipeIn.close();
        wigReader.close();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException
    {

        if (!hasStarted)
        {
            threadPool = Executors.newFixedThreadPool(1);
            threadPool.submit(new PipeWorker(wigReader, new PrintWriter(pipeOut)));
            hasStarted = true;
        }

        int ret = pipeIn.read(cbuf, off, len);

        if (ret == -1)
        {
            threadPool.shutdownNow();
        }
        return ret;
    }

}
