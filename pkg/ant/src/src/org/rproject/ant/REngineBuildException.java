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

import org.rosuda.REngine.REngineException;

/**
 * Ant compatible wrapper for {@link REngineException}
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
@SuppressWarnings("serial")
public class REngineBuildException extends RBuildException {

	/**
	 * Constructor. Grabs the message of the {@link REngineException}
	 *  
	 * @param message additional message 
	 * @param e exception to wrap
	 */
	public REngineBuildException( String message, REngineException e){
		super( "REngine exception : " + message + " : " + e.getMessage()) ; 
	}
	
}
