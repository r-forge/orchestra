package org.rproject.ant;

import org.apache.tools.ant.Task;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

/**
 * Super class of all R specific tasks 
 * 
 * @author romain
 *
 */
public abstract class RTask extends Task {

	/**
	 * REngine used by R specific tasks
	 */
	protected static JRIEngine R = initR() ;
	
	/**
	 * Initializes R
	 */
	private static JRIEngine initR(){
		JRIEngine r = null ;
		try {
			r = new JRIEngine(new String[]{ "--vanilla" }, null ) ;
			r.parseAndEval( "require( 'ant', quietly= TRUE, character.only = TRUE )" ) ;
		} catch (REngineException e) {
			System.out.println("problem creating R engine");
			e.printStackTrace();
			System.exit(1) ;
		} catch (REXPMismatchException e) {
			System.out.println("problem loading the ant package");
			e.printStackTrace();
			System.exit(1) ;
		} 
		return r; 
	}
}
