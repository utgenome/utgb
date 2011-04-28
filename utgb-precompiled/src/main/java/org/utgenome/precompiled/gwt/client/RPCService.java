//--------------------------------------
//
// RPCService.java
// Since: 
//
//  
// 
//--------------------------------------
package org.utgenome.precompiled.gwt.client;


import com.google.gwt.rpc.client.RpcService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * GWT Remote Procedure Call (RPC) interface. See also
 * http://code.google.com/intl/en/webtoolkit/doc/1.6/DevGuideServerCommunication.html 
 *
 * After adding a new method to this
 * interface, you also have to its asynchronous version client/RPCServiceAsync.java, and provide its implementation
 * server/RPCServiceAsyncImpl.java.
 *
 * 
 * 
 */
@RemoteServiceRelativePath("rpc")
public interface RPCService extends RpcService {

	// add your own RPC procedure
	public String helloWorld(String name);
}