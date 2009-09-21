package com.addictedtor.jedit.rengine.jri ;

import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.rengine.REngineService;

/**
 * An implementation of the service REngineService that returns a
 * JRIEngine R engine
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class JRIEngineService extends REngineService {
	
	private static final JRIEngine jri = init();  
	
	public REngine getEngine(){
		return jri; 
	}
	
	private static JRIEngine init(){
		JRIEngine engine = null ; 
		String[] args = { "--save" } ;
		try{
			engine = new JRIEngine( args ) ;
		} catch( REngineException e){
			System.out.println( "could not create jri engine" ) ;
		}
		
		Rengine rni = null ; 
		try{
			rni = engine.getRni() ;
			rni.setName( "R engine" ) ;
		} catch( NullPointerException e){
			System.out.println( "could not get the Rengine" ) ;
		}
		
		return engine; 
	}
	
}
