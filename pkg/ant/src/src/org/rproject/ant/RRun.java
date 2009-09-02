package org.rproject.ant;

import org.apache.tools.ant.BuildException;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

public class RRun extends RTask {

	private String code; 
	
	@Override
	public void execute() throws BuildException {
		try {
			R.parseAndEval( code ) ;
		} catch (REngineException e) {
			throw new REngineBuildException( e ) ;
		} catch (REXPMismatchException e) {
			throw new REXPMismatchBuildException( e ) ;
		}
	}
	
	public void setCode( String code){
		this.code = code ; 
	}
	
}
