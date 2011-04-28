//--------------------------------------
// utgb-shell Project
//
// Browser.java
// Since: 
//
//--------------------------------------
package org.utgenome.precompiled.gwt.client;

import com.google.gwt.core.client.GWT;
import org.utgenome.gwt.utgb.client.UTGBEntryPointBase;

/**
 * Entry point of the UTGB Browser. 
 * Edit the following files to change the appearance of the browser:
 *  src/main/webapp/view/default-view.xml
 *  public/browser.html
 *
 */
public class Browser extends UTGBEntryPointBase {

	private static RPCServiceAsync _service = null;

	public static void initServices() {
		// set up an access interface to the web service
		_service = (RPCServiceAsync) GWT.create(RPCService.class);
	}

	/**
	 * Get RPC service to communicate with the server-side code 
	 */ 
	public static RPCServiceAsync getRPCService() {
		if (_service == null)
			initServices(); 
			
		return _service;
	}


	@Override
	public void main() {
		// This line insert the UTGB interface to the <div id="utgb-main"></div> part in the browser.html file
		// You can control track contents to be displayed by editing the view XML file (src/main/webapp/view/default-view.xml is used in default)
		displayTrackView();	
		
		// add your GWT codes here
		
		
		// If you want to make RPC calls, use the following code snipet 
		/*
		RPCService rpc = Browser.getRPCService();
		rpc.helloWorld("kitty", new AsyncCallBack<String>(){
			public void onFailure(Throwable e) {
				GWT.log("error during the RPC call", e); 
			}
			public void onSuccess(String message) {
				GWT.log("server returns " + message, null); // message is "hello world! kitty" 
			}
		}); 
        */ 
	}

}

