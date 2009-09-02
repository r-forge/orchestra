package org.rproject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

/**
 * Super class of all R specific tasks 
 * 
 * @author romain
 *
 */
public abstract class RTask extends Task implements MessageListener {

	/**
	 * REngine used by R specific tasks
	 */
	protected static JRIEngine R = initR() ;

	protected static AntRMainLoopCallbacks loop ; 
	
	protected static StringBuilder buffer ;
	
	protected static REXPReference project  = null ;
	
	/**
	 * Initializes R
	 */
	private static JRIEngine initR(){
		JRIEngine r = null ;
		loop = new AntRMainLoopCallbacks() ;
		try {
			r = new JRIEngine(new String[]{ "--vanilla" }, null ) ;
			r.parseAndEval( "require( 'ant', quietly= TRUE, character.only = TRUE )" ) ;
			r.getRni().addMainLoopCallbacks( loop ) ;
			r.getRni().startMainLoop() ;
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

	protected REXP run(String code) throws BuildException {
		REXP out = null ;
		buffer = new StringBuilder() ; 
		loop.setMessageListener( this ) ;
		
		/* assign the project R variable to the project */
		/* TODO: maybe do this only once */
		if( project == null ) {
			try{
				project = createJavaObjectReference(getProject())  ;
				R.assign( "project" , project ) ;
			} catch (REngineException e) {
				throw new REngineBuildException( "assigning 'project'", e ) ;
			} catch (REXPMismatchException e) {
				throw new REXPMismatchBuildException( "assigning 'project' ", e ) ;
			}
		}
		
		/* run the code */
		try {
			out = R.parseAndEval( code ) ;
		} catch (REngineException e) {
			throw new REngineBuildException( "running code" , e ) ;
		} catch (REXPMismatchException e) {
			throw new REXPMismatchBuildException( "running code", e ) ;
		}
		
		flush(); 
		loop.setMessageListener( null ) ;
		return out ;
	}

	/* this should be in the JRIEngine class */
	private REXPReference createJavaObjectReference(Object o){
		REXPReference ref = null; 
		int key = R.lock() ;
		try {
			org.rosuda.JRI.REXP rx = R.getRni().createRJavaRef( o );
			if( rx != null){
				long p = rx.xp; 
				R.getRni().rniPreserve(p);
				ref = new REXPReference( R, new Long(p) ) ;
			}
		} finally {
			R.unlock(key) ;
		}
		return ref ; 
	}
	
	public void send( Message message){
		buffer.append( message.getMessage() ) ;
	}
	
	public void flush(){
		if(buffer.length() != 0) {
			log( buffer.toString() ) ;
			buffer.setLength(0) ;
		} 
	}

}
