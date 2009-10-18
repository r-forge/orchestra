package com.addictedtor.jedit.rengine.jri ;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.gjt.sp.util.Log;
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REngine;
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
	
	@SuppressWarnings("unchecked")
	private static JRIEngine init(){
		
		/* so that System.load("jri") failing does not cause System.exit(1), see Rengine */
		System.setProperty("jri.ignore.ule", "yes" ) ; 		
		
		/* use reflection to load the RJavaClassLoader */
		Class RJCL = null ; 
		Constructor<?> cons = null ; 
		ClassLoader rjcl = null ;
		try{
			/* the RJavaClassLoader class */
			RJCL =  Class.forName("RJavaClassLoader" ) ;
			
			Class<?>[] clazzes = new Class<?>[]{ String.class, String.class } ;
			cons = 	RJCL.getConstructor( clazzes ) ; 
			
			rjcl = (ClassLoader) cons.newInstance( "/usr/local/lib/R/library/rJava" , "/usr/local/lib/R/library/rJava/libs" ) ;
			
			/* use the rjcl to load the jri library without relying on java.library.path */
			Method findlib = getProtectedMethod( RJCL, "findLibrary" ) ;
			boolean access = findlib.isAccessible() ;
			findlib.setAccessible(true ) ;
			String jrilib = (String)findlib.invoke( rjcl, new Object[]{ "jri" }) ;
			findlib.setAccessible(access ) ;
			
			System.load( jrilib ) ;
			
		} catch( Exception e){
			e.printStackTrace() ;
		}
		Log.log( Log.ERROR, null, rjcl.getClass().getName() ) ;
		Rengine.jriLoaded = true ;
		
		JRIEngine engine = null ; 
		String[] args = { "--save" } ;
		try{
			engine = new JRIEngine( args ) ;
			engine.parseAndEval( "{ require( rJava ); .jinit() }" ) ;
		} catch( Exception e){
			e.printStackTrace() ;
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
	
	
	private static Method getProtectedMethod( Class<?> cl, String name){
		Method[] methodz = cl.getDeclaredMethods() ;
		Method m = null ;
		for( int i=0; i<methodz.length; i++){
			m = methodz[i] ;
			if( name.equals( m.getName() ) ){
				return m ; 
			}
		}
		return null ;
	}
	
	
}
