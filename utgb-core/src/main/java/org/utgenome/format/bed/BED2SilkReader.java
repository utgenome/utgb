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
// BED2SilkReader.java
// Since: May 26, 2009
//
// $URL: http://svn.utgenome.org/utgb/trunk/utgb/utgb-shell/src/main/java/org/utgenome/shell/db/bed/BED2SilkReader.java $ 
// $Author: leo $
//--------------------------------------
package org.utgenome.format.bed;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.utgenome.UTGBException;
import org.xerial.util.log.Logger;

public class BED2SilkReader extends Reader
{

    private static Logger _logger = Logger.getLogger(BED2SilkReader.class);

    private final Reader bedReader;
    private ExecutorService threadPool;

    private final PipedWriter pipeOut;
    private final PipedReader pipeIn;

    private boolean hasStarted = false;

    public BED2SilkReader(Reader bedReader) throws IOException
    {
        this.bedReader = bedReader;

        pipeOut = new PipedWriter();
        pipeIn = new PipedReader(pipeOut);

    }

    private static class PipeWorker implements Runnable
    {

        private final BED2Silk bed2silk;
        private final PrintWriter out;

        public PipeWorker(Reader in, PrintWriter out) throws IOException
        {
            bed2silk = new BED2Silk(in);
            this.out = out;
        }

        public void run()
        {
            if (out == null)
                return;
            try
            {
                bed2silk.toSilk(out);
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
        bedReader.close();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException
    {

        if (!hasStarted)
        {
            threadPool = Executors.newFixedThreadPool(1);
            threadPool.submit(new PipeWorker(bedReader, new PrintWriter(pipeOut)));
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
