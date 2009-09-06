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
 * A message that is captured from the R output
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class Message {
	
	/**
	 * A regular message
	 */
	public static final int MESSAGE = 0;
	
	/** 
	 * Error or warning
	 */
	public static final int ERROR = 1; 
	
	/**
	 * The text of the message
	 */
	private String message ;
	
	/**
	 * The type of the message
	 */
	private int type = MESSAGE ;
	
	/**
	 * Creates a message of a given type
	 * 
	 * @param message text of the message
	 * @param type type of message
	 */
	public Message( String message, int type){
		this.message=message; 
		this.type = type ;
	}
	
	/**
	 * Creates a regular message
	 * @param message text of the message
	 */
	public Message( String message){
		this( message, MESSAGE ) ;
	}
	
	/**
	 * @return the text of this message
	 */
	public String getMessage(){
		return message ;
	}
	
	/**
	 * @return the type of this message
	 */
	public int getType(){
		return type ;
	}
	
}
