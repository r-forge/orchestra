/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.rproject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

/**
 * Super class of all R specific tasks 
 * 
 * @author Romain Francois <francoisromain@free.fr>
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
	
	protected boolean failonerror; 
	
	protected boolean failure = false; 
	
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
		REXP res = null ; 
		if( failonerror ){
			try{
				setCustomErrorHandler( ) ;
				failure = false; 
				res =  run_(code) ;
				if( failure ){
					throw new BuildException( "R error" ) ;
				}
			} catch( BuildException e){
				log( "error " + e.getMessage() , Project.MSG_ERR ) ;
				System.exit( 1 ) ;
			} finally{
				resetCustomErrorHandler( ) ;
			}
		} else {
			res = run_(code) ;
		}
		return res ; 
	}
	
	private void resetCustomErrorHandler() {
		try {
			R.parseAndEval( "options( error = NULL )" );
		} catch (REngineException e) {
			throw new REngineBuildException("resetting the custom error handler", e) ;
		} catch (REXPMismatchException e) {
			throw new REXPMismatchBuildException("resetting the custom error handler", e) ; 
		} 
	}

	private void setCustomErrorHandler() throws BuildException {
		try {
			R.parseAndEval( "options( error = ant:::ant.task.error.handler )" );
		} catch (REngineException e) {
			throw new REngineBuildException("setting the custom error handler", e) ;
		} catch (REXPMismatchException e) {
			throw new REXPMismatchBuildException("setting the custom error handler", e) ; 
		} 
	}

	protected REXP run_(String code) throws BuildException {
		REXP out = null ;
		buffer = new StringBuilder() ; 
		loop.setMessageListener( this ) ;
		
		/* assign the project R variable to the project */
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
		
		try{
			R.assign( "self", createJavaObjectReference(this) ) ;
		} catch( REngineException e){
			throw new REngineBuildException( "assigning 'self'", e ) ;
		} catch( REXPMismatchException e){
			throw new REXPMismatchBuildException( "assigning 'self'", e ) ;
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
	private static REXPReference createJavaObjectReference(Object o){
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
	
	public void setFailonerror(boolean failonerror){
		this.failonerror = failonerror ; 
	}
	
	public void fails(){
		failure = true ; 
	}

	/**
	 * {@link MessageListener} implementation, add the message to a message buffer 
	 */
	public void send( Message message){
		buffer.append( message.getMessage() ) ;
	}
	
	/**
	 * {@link MessageListener} implementation, log the message buffer
	 */
	public void flush(){
		if(buffer.length() != 0) {
			log( buffer.toString() ) ;
			buffer.setLength(0) ;
		} 
	}
	
	
}
