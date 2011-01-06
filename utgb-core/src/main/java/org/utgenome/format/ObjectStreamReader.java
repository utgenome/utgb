/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// ObjectStreamReader.java
// Since: 2010/12/12
//
//--------------------------------------
package org.utgenome.format;

import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.xerial.lens.ObjectStreamHandler;
import org.xerial.util.ArrayDeque;
import org.xerial.util.Deque;

/**
 * Text Format -> Object -> Iterator
 * 
 * @author leo
 * 
 */
public class ObjectStreamReader {

	public static interface TextFormatToObjectMapper {
		public void map(Reader input, ObjectStreamHandler out);
	}

	private final TextFormatToObjectMapper mapper;
	private final Deque<Object> objectQueue = new ArrayDeque<Object>();
	private final ExecutorService threadPool;
	private final TextReader reader;

	public ObjectStreamReader(Reader in, TextFormatToObjectMapper mapper) {
		this.mapper = mapper;
		this.threadPool = Executors.newFixedThreadPool(1);
		this.reader = new TextReader(in);

	}

	public class TextReader implements Callable<Void>, ObjectStreamHandler {

		private final Reader in;
		private final Deque<Object> queue = new ArrayDeque<Object>();
		private final int NUM_OBJECTS_TO_CACHE = 10000;

		public TextReader(Reader in) {
			this.in = in;
		}

		public void close() throws IOException {
			in.close();
		}

		public Void call() throws Exception {
			mapper.map(in, this);
			return null;
		}

		/**
		 * Fill the destination queue
		 * 
		 * @param dest
		 */
		public void fillQueue(Deque<Object> dest) {

		}

		public <T> void add(T obj) throws Exception {
			queue.add(obj);
			while (queue.size() > NUM_OBJECTS_TO_CACHE) {
				wait(1000);
			}
		}

		public <T> void add(String name, T obj) throws Exception {
			// TODO Auto-generated method stub

		}

		public <T, U> void connect(T parent, String name, U obj) throws Exception {
			// TODO Auto-generated method stub

		}

		public <T, U> void append(T parent, String name, U obj) throws Exception {
			// TODO Auto-generated method stub

		}
	}

	public boolean hasNext() throws IOException {
		if (!objectQueue.isEmpty())
			return true;

		// Fill the queue with the objects retrieved from the text reader thread
		// TODO
		return false;
	}

	public Object next() throws IOException {
		if (hasNext())
			return objectQueue.pollFirst();

		throw new NoSuchElementException();
	}

}
