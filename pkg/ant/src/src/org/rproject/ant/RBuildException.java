package org.rproject.ant;

import org.apache.tools.ant.BuildException;

@SuppressWarnings("serial")
public abstract class RBuildException extends BuildException {

	public RBuildException(String message) {
		super( message ) ;
	}
	
}
