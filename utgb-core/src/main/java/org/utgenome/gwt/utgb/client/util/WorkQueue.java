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
// WorkQueue.java
// Since: Aug 15, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;

import java.util.ArrayList;

public class WorkQueue implements Command {

	ArrayList<Worker> _workerQueue = new ArrayList<Worker>();
	
	public WorkQueue()
	{
		
	}

	public void execute() {
		if(_workerQueue.isEmpty())
			return;

        Scheduler.get().scheduleDeferred(nextWorker());
		
	}
	
	public boolean hasNextWorker()
	{
		return !_workerQueue.isEmpty();
	}
	
	public Worker nextWorker()
	{
		if(!_workerQueue.isEmpty())
		{
			return _workerQueue.remove(0);
		}
		else 
			return null;
	}
	
	public void addWorker(Worker worker)
	{
		_workerQueue.add(worker);
	}
	
	
	
}




