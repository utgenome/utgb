//--------------------------------------
// tomcat-server Project
//
// TomcatServerTest.java
// Since: Sep 12, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell.tomcat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xerial.core.XerialException;
import org.xerial.util.log.Logger;

public class TomcatServerTest {
	private static Logger _logger = Logger.getLogger(TomcatServerTest.class);

	TomcatServer server = null;

	@Before
	public void setUp() throws Exception {
		server = new TomcatServer(8081);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void start() throws URISyntaxException, IOException {
		try {
			server.start();

			// server.addContext("/sample", "../../src/main/webapp");

			// System.in.read();
			server.stop();

			// server.start();
		}
		catch (XerialException e) {
			_logger.error(e);
		}
	}

}
