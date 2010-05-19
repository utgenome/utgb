/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
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
// utgb-core Project
//
// Pipe.java
// Since: 2010/05/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.xerial.util.log.Logger;

/**
 * A base class for converting an input format into another through a pipe.
 * 
 * @author leo
 * 
 */
public class FormatConversionReader extends Reader {

	private static Logger _logger = Logger.getLogger(FormatConversionReader.class);

	private ExecutorService threadPool;

	private final PipeConsumer worker;
	private PipeRunner pipeRunner;

	private boolean hasStarted = false;

	public FormatConversionReader(Reader input, PipeConsumer worker) throws IOException {
		pipeRunner = new PipeRunnerForReader(input);
		this.worker = worker;
	}

	public FormatConversionReader(InputStream input, PipeConsumer worker) throws IOException {
		pipeRunner = new PipeRunnerForInputStream(input);
		this.worker = worker;
	}

	public static abstract class PipeConsumer {

		/**
		 * Read the data from <i>in</i>, then output the data to <i>out</i>
		 * 
		 * @param in
		 * @param out
		 */
		public void consume(Reader in, Writer out) throws Exception {
			// do some thing;
		}

		public void consume(InputStream in, Writer out) throws Exception {
			this.consume(new InputStreamReader(in), out);
		}

		public void reportError(Exception e) {
			_logger.error(e);
		}
	}

	private abstract class PipeRunner implements Callable<Void> {
		protected final PipedWriter pipeOut;
		private final PipedReader pipeIn;

		public PipeRunner() throws IOException {
			pipeOut = new PipedWriter();
			pipeIn = new PipedReader(pipeOut);
		}

		public int readPipe(char[] cbuf, int off, int len) throws IOException {
			return pipeIn.read(cbuf, off, len);
		}

		public Void call() throws Exception {
			try {
				consume();
			}
			catch (Exception e) {
				_logger.error(e);
			}
			finally {
				if (pipeOut != null)
					pipeOut.close();
			}
			return null;
		}

		public abstract void consume() throws Exception;

		public void close() throws IOException {
			if (pipeIn != null)
				pipeIn.close();
		}
	}

	private class PipeRunnerForReader extends PipeRunner {

		private final Reader in;

		public PipeRunnerForReader(Reader in) throws IOException {
			super();
			this.in = in;
			if (in == null)
				throw new NullPointerException("missing input reader");
		}

		public void close() throws IOException {
			super.close();
			if (in != null)
				in.close();
		}

		@Override
		public void consume() throws Exception {
			worker.consume(in, pipeOut);
		}

	}

	private class PipeRunnerForInputStream extends PipeRunner {

		private final InputStream in;

		public PipeRunnerForInputStream(InputStream in) throws IOException {
			super();
			this.in = in;

			if (in == null)
				throw new NullPointerException("missing input stream");
		}

		@Override
		public void consume() throws Exception {
			worker.consume(in, pipeOut);
		}

		public void close() throws IOException {
			super.close();
			if (in != null)
				in.close();
		}

	}

	@Override
	public void close() throws IOException {
		pipeRunner.close();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {

		if (!hasStarted) {
			threadPool = Executors.newFixedThreadPool(1);
			threadPool.submit(pipeRunner);
			hasStarted = true;
		}

		int ret = pipeRunner.readPipe(cbuf, off, len);

		if (ret == -1) {
			threadPool.shutdownNow();
		}
		return ret;
	}

}
