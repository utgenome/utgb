//--------------------------------------
//
// RPCServiceAsync.java
// Since: 
//
//  
// 
//--------------------------------------
package org.utgenome.precompiled.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.rpc.client.RpcService;

/**
 * Asynchronous version of the RPCService interface.
 * To implement GWT RPC procecdure, see http://code.google.com/intl/en/webtoolkit/doc/1.6/DevGuideServerCommunication.html 
 */
public interface RPCServiceAsync extends RpcService {
	// add asynchronous version of the RPC procedure
	public void helloWorld(String name, AsyncCallback<String> callback);
}