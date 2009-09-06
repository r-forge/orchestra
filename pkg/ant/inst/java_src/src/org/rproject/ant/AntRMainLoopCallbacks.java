/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.rproject.ant;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 * Dummy implementation of the main loop callbacks, 
 * used to grab output and send them as messages to a
 * MessageListener
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class AntRMainLoopCallbacks implements RMainLoopCallbacks {

	/**
	 * The message listener that grabs the messages
	 */
	private MessageListener listener = null ;
	
	/**
	 * Does nothing
	 */
	public void rBusy(Rengine arg0, int arg1) {}

	/**
	 * Send an error message and return null
	 * 
	 * @return always null 
	 */
	public String rChooseFile(Rengine arg0, int arg1) {
		if( listener != null ){
			listener.send( new Message( "Cannot choose a file within ant" , Message.ERROR) ) ;
		}
		return null; 
	}

	/**
	 * Call the flush method of the associated message listener
	 * 
	 * @see MessageListener#flush()
	 */
	public void rFlushConsole(Rengine arg0) {
		if( listener != null ){
			listener.flush() ;
		}
	}
	
	/**
	 * Does nothing
	 */
	public void rLoadHistory(Rengine arg0, String arg1) {}

	/**
	 * Waits 100ms and return an empty string
	 */
	public String rReadConsole(Rengine arg0, String arg1, int arg2) {
		try{
			Thread.sleep( 100 ) ;
		} catch( InterruptedException e){}
		return " " ;
	}

	/**
	 * Does nothing
	 */
	public void rSaveHistory(Rengine arg0, String arg1) {}

	/**
	 * Send the message to the message listener
	 * 
	 * @see MessageListener#send(Message)
	 */
	public void rShowMessage(Rengine arg0, String arg1) {
		if( listener != null ){
			listener.send( new Message( arg1) ) ;
		}
	}

	/**
	 * Send the message to the message listener
	 * 
	 * @see MessageListener#send(Message)
	 */
	public void rWriteConsole(Rengine arg0, String arg1, int arg2) {
		if( listener != null){
			listener.send( new Message( arg1, arg2 ) ) ;
		}
	}

	/**
	 * Set the message listener (typically a instance of {@link RTask})
	 * 
	 * @param listener a message listener
	 */
	public void setMessageListener( MessageListener listener){
		this.listener = listener ; 
	}
}
