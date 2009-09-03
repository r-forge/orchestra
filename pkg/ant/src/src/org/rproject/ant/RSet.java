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

import org.apache.tools.ant.BuildException;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

/**
 * Ant task that sets a property to contain the result of
 * an R expression.
 * 
 * &lt;r-set code="R.home()" property="r.home" /&gt;
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class RSet extends RTask {

	/**
	 * Code to be run
	 */
	private String code = "" ;
	
	/**
	 * Name of the property to set
	 */
	private String property = "r.default.property" ;
	
	/**
	 * Run the R code, grab the result of the last expression and use 
	 * the value to set the property
	 */
	@Override
	public void execute() throws BuildException {
		REXP res = run( code ) ;
		String out;
		try {
			out = res.asString();
		} catch (REXPMismatchException e) {
			throw new REXPMismatchBuildException( "converting to String", e ) ;
		}
		getProject().setProperty( property, out ) ;
	}
	
	/**
	 * The R code to execute. At the moment, no checking is done
	 * to ensure that the result is a character string, it just has to be 
	 * 
	 * @param code R expression that must produce a character vector
	 */
	public void setCode( String code){
		this.code = code ; 
	}
	
	/**
	 * The name of the property to set
	 * @param property the name of the property to set
	 */
	public void setProperty( String property ){
		this.property = property ; 
	}
	
	/**
	 * @param code set the code to run by reading the content of the tag
	 */
	public void addText(String code){
		this.code = code;
	}

}
