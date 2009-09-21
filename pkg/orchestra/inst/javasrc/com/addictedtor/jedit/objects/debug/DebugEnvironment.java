package com.addictedtor.jedit.objects.debug;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.error.ExceptionManager;

/**
 * Encapsulates some information about an environment that 
 * is part of the call stack when debugging 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class DebugEnvironment {

	private REXPReference environment; 
	private int[] srcloc = null; 
	private String file = null ; 
	private String name; 
	
	public DebugEnvironment( REXPReference env, String name){
		environment = env; 
		this.name   = name; 
	}
	
	public DebugEnvironment( REXPReference env, String name, REXP srcinfo ){
		this( env, name ) ;
		if( !srcinfo.isNull() ){
			try{
				RList srcinfo_list = srcinfo.asList();
				file = srcinfo_list.at( "file" ).asString() ; 
				srcloc = srcinfo_list.at( "srcref" ).asIntegers() ;
			} catch( REXPMismatchException e){
				ExceptionManager.send( e ) ; 
			}
		}
	}
	
	public REXPReference getEnvironment() {
		return environment;
	}
	
	public int[] getSrcLocation(){
		return srcloc; 
	}
	
	public String getFile() {
		return file;
	}
	
	public String getName() {
		return name;
	}
}
