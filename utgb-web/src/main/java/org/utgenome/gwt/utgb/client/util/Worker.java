/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// UTGBMedaka Project
//
// Worker.java
// Since: Aug 15, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class Worker implements Command {

	private Timer timer = null;
	private WorkQueue workQueue;
	private Command command = null;

	public Worker(WorkQueue workQueue, Command command) {
		this.workQueue = workQueue;
	}

	public class WorkerCallback implements AsyncCallback<Object> {
		public void onFailure(Throwable caught) {
			handleFailure(caught);
		}

		public void onSuccess(Object result) {
			handleSuccess();
		}

	}

	public abstract void execute();

	public void handleFailure(Throwable caught) {

	}

	public void handleSuccess() {
		if (workQueue.hasNextWorker())
			DeferredCommand.addCommand(workQueue.nextWorker());
	}
}
