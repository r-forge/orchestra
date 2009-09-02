package org.rproject.ant;

import org.rosuda.REngine.REXPMismatchException;

@SuppressWarnings("serial")
public class REXPMismatchBuildException extends RBuildException {

	public REXPMismatchBuildException( String message, REXPMismatchException e){
		super( "REXPMismatchException exception : " + message + " : " + e.getMessage()) ; 
	}
	
}
