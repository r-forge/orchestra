package org.rproject.ant;

import org.apache.tools.ant.BuildException;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

public class RSet extends RTask {

	private String code; 
	private String property ;
	
	@Override
	public void execute() throws BuildException {
		String res ;
		try {
			res = R.parseAndEval( code ).asString() ;
		} catch (REngineException e) {
			throw new REngineBuildException( e ) ;
		} catch (REXPMismatchException e) {
			throw new REXPMismatchBuildException( e ) ;
		}
		getProject().setProperty( property, res ) ;
	}
	
	public void setCode( String code){
		this.code = code ; 
	}
	
	public void setProperty( String property ){
		this.property = property ; 
	}

}
