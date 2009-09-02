package org.rproject.ant;

import org.apache.tools.ant.BuildException;

/**
 * Ant task that runs R code
 * 
 * <r-run>
 * 	rnorm(10)
 * </r-run>
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class RRun extends RTask {

	private String code = "" ; 
	
	@Override
	public void execute() throws BuildException {
		run(code) ;
	}
	
	public void addText(String text) {
	     code = text ; 
	}
	
	public void setCode(String code){
		this.code = code;
	}
}
