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
// utgb-core Project
//
// StatusLabel.java
// Since: Sep 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.ui;

import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * for message
 * 
 * @author leo
 * 
 */
public class Message extends Composite {

	private Label messageLabel = new Label();
	private MessageType messageType = MessageType.INFO;

	public Message() {
		this("");
	}

	public Message(String text) {
		this(MessageType.INFO, text);
	}

	public Message(MessageType type, String text) {
		setMessage(type, text);
		Style.bold(messageLabel);
		initWidget(messageLabel);
	}

	public void setMessage(MessageType type, String text) {
		this.messageType = type;
		messageLabel.setText(text);

		switch (type) {
		case INFO:
			Style.fontColor(messageLabel, "#666666");
			break;
		case WARN:
			Style.fontColor(messageLabel, "#FFCC99");
			break;
		case ERROR:
			Style.fontColor(messageLabel, "#FF9999");
			break;
		}
	}

	public void info(String text) {
		setMessage(MessageType.INFO, text);
	}

	public void warn(String text) {
		setMessage(MessageType.WARN, text);
	}

	public void error(String text) {
		setMessage(MessageType.ERROR, text);
	}

}
