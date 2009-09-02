package org.rproject.ant;

import org.rosuda.REngine.REXPMismatchException;

@SuppressWarnings("serial")
public class REXPMismatchBuildException extends RBuildException {

	public REXPMismatchBuildException( REXPMismatchException e){
		super( "REXPMismatchException exception : " + e.getMessage()) ; 
	}
	
}
