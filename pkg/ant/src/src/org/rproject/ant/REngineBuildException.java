package org.rproject.ant;

import org.rosuda.REngine.REngineException;

@SuppressWarnings("serial")
public class REngineBuildException extends RBuildException {

	public REngineBuildException( REngineException e){
		super( "REngine exception : " + e.getMessage()) ; 
	}
	
}
