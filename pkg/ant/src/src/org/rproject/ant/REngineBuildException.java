package org.rproject.ant;

import org.rosuda.REngine.REngineException;

@SuppressWarnings("serial")
public class REngineBuildException extends RBuildException {

	public REngineBuildException( String message, REngineException e){
		super( "REngine exception : " + message + " : " + e.getMessage()) ; 
	}
	
}
