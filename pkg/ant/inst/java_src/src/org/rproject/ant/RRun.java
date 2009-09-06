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

/**
 * Ant task that runs R code
 * 
 * <p> Example :</p>
 * 
 * &lt;r-run&gt;rnorm(10)&lt;/r-run&gt;
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class RRun extends RTask {

	/**
	 * Code to be run
	 */
	private String code = "" ; 
	
	/**
	 * Runs the code
	 */
	@Override
	public void execute() throws BuildException {
		run(code) ;
	}
	
	/**
	 * @param text set the code to be run by reading the content of the 
	 * &lt;r-run&gt; tag
	 */
	public void addText(String text) {
	     code = text ; 
	}
	
	/**
	 * @param code set the code to be run by reading the <strong>code</strong> attribute of the 
	 * &lt;r-run&gt; tag
	 */
	public void setCode(String code){
		this.code = code;
	}
}
