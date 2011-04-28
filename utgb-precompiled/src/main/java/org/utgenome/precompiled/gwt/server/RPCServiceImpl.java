//--------------------------------------
//
// RPCServiceImpl.java
// Since: 
//
//  
// 
//--------------------------------------
package org.utgenome.precompiled.gwt.server;

import org.utgenome.precompiled.gwt.client.RPCService;
import com.google.gwt.rpc.server.RpcServlet;

/**
 * Server-side implementation of the RPCService
 * 
 */
public class RPCServiceImpl extends RpcServlet implements RPCService {

	private static final long serialVersionUID = 1L;

	public String helloWorld(String name)
	{
		return String.format("hello world! %s", name);
	}
}