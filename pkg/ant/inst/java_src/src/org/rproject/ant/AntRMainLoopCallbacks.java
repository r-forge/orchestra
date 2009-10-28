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

import org.rosuda.REngine.REngine ;
import org.rosuda.REngine.REngineOutputInterface ;
import org.rosuda.REngine.REngineCallbacks ;

/**
 * REngine callbacks. Only output callbacks are implemented since
 * ant is supposed to be used non interactively
 *
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class AntRMainLoopCallbacks implements REngineCallbacks, REngineOutputInterface {

	/**
	 * The message listener that grabs the messages
	 */
	private MessageListener listener = null ;
	
	
	/** called when R prints output to the console.
	 *  @param eng calling engine
	 *  @param text text to display in the console
	 *  @param oType output type (0=regular, 1=error/warning)
	 */
	 public void RWriteConsole(REngine eng, String text, int oType){
			if( listener != null){
				listener.send( new Message( text, oType ) ) ;
			}
	 }
	
	
	/** Send the message to the message listener
	 *
	 *  @param eng calling engine
	 *  @param text text to display in the message
	 */
	public void RShowMessage(REngine eng, String text){
		 if( listener != null ){
			listener.send( new Message( text ) ) ;
		}
	}
	
	/** Call the flush method of the associated message listener
	 *  @param eng calling engine
	 */
	public void RFlushConsole(REngine eng){
		 if( listener != null ){
		 	 listener.flush() ;
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
