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

/**
 * Listens to messages generated by the R main loop 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public interface MessageListener {

	/**
	 * Send the message. This is called by various methods of 
	 * the {@link AntRMainLoopCallbacks} class
	 * 
	 * @param message a message sent by the R main loop
	 */
	public void send( Message message) ;
	
	/**
	 * Flush the listener
	 */
	public void flush() ; 
}
