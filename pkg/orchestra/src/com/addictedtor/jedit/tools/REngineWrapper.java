package com.addictedtor.jedit.tools;

import java.util.Vector;

import org.gjt.sp.util.Log;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLanguage;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPNull;
import org.rosuda.REngine.REXPRaw;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REXPSymbol;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.error.ExceptionManager;

public class REngineWrapper {

	
	private static Class<?> int_ARRAY ;
	private static Class<?> double_ARRAY ;
	private static Class<?> short_ARRAY;
	private static Class<?> byte_ARRAY;
	
	private static Class<?> String_ARRAY ;
	private static Class<?> Integer_ARRAY ;
	private static Class<?> Double_ARRAY ;
	private static Class<?> Short_ARRAY;
	private static Class<?> Byte_ARRAY;
	
 	static{
		try{
			int_ARRAY     = Class.forName("[I" ); 
			short_ARRAY   = Class.forName("[S" ); 
			double_ARRAY  = Class.forName("[D" ); 
			byte_ARRAY    = Class.forName("[B") ;
			
			String_ARRAY  = Class.forName( "[Ljava.lang.String;") ;
			Integer_ARRAY = Class.forName( "[Ljava.lang.Integer;" ) ;
			Double_ARRAY  = Class.forName( "[Ljava.lang.Double;" ) ;
			Short_ARRAY   = Class.forName( "[Ljava.lang.Short;" ) ;
			Byte_ARRAY    = Class.forName( "[Ljava.lang.Byte;" ) ;
			
		} catch( Exception e){
			ExceptionManager.send( e ) ;
		}
	}
	
 	/**
 	 * Creates a call the function with the given arguments. A REXPReference to the 
 	 * call is returned, which may then be evaluated 
 	 * 
 	 * @param fun the name of a function
 	 * @param arguments list arguments, which are all wrapped into REXP 
 	 * @return a {@link REXPReference} to the created call
 	 * @throws REXPMismatchException
 	 * @throws REngineException
 	 */
	public static REXP call( String fun, Object... arguments ) throws REXPMismatchException, REngineException {
		JRIEngine r = (JRIEngine)RPlugin.getR(); 
		if( !r.supportsReferences() ){
			throw new REngineException( RPlugin.getR(), "references are not supported by the R engine" ) ;
		}
		
		Vector<REXP> objects = new Vector<REXP>() ;
		objects.add( new REXPSymbol( fun ) ) ;
		for( Object o: arguments){
			objects.add( wrap( o ) ) ;
		}
		REXP[] args = new REXP[ objects.size() ] ;
		objects.toArray(args) ;
		REXPLanguage lan = new REXPLanguage( new RList( args ) ) ;
		return lan ;
	}
	
	/**
	 * Wraps an object into a REXP. basic types are handled by creating the
	 * appropriate R objects (REXPInteger, REXPDouble, REXPRaw, REXPString)
	 * other types are transferred as "jobjRef" objects in R (references to the java object) 
	 * 
	 * @param o object to wrap
	 * @return 
	 */
	public static REXP wrap( Object o){
		
		REXP ans = null ; 
		Class<?> clazz = o.getClass() ;
		if( o instanceof REXPReference){
			ans = ( (REXPReference)o ).resolve() ;
		} else if( o instanceof REXP){
			ans = (REXP)o; 
		} else if( clazz == Integer.class ){
			ans = new REXPInteger( ((Integer)o).intValue() ) ;
		} else if( clazz == Double.class ){
			ans = new REXPDouble( ((Double)o).doubleValue() ) ;
		} else if( clazz == String.class ){
			ans = new REXPString( (String)o ) ;
		} else if( clazz == Short.class ){
			ans = new REXPInteger( (Short)o ) ;
		} else if( clazz == int_ARRAY ){
			ans = new REXPInteger( (int[])o ) ; 
		} else if( clazz == byte_ARRAY ){
			ans = new REXPRaw( (byte[])o ) ; 
		} else if( clazz == Integer_ARRAY ){
			Integer[] integers = (Integer[])o;
			int n = integers.length ;
			int[] ints = new int[integers.length];
			for( int i=0; i<n; i++){
				ints[i] = integers[i].intValue() ;
			}
			ans = new REXPInteger( ints ); 
		} else if( clazz == String_ARRAY ){
			ans = new REXPString( (String[])o ); 
		} else if( clazz == Byte_ARRAY ){
			Byte[] b = (Byte[])o;
			int n = b.length ;
			byte[] bytes = new byte[b.length];
			for( int i=0; i<n; i++){
				bytes[i] = b[i].byteValue() ;
			}
			ans = new REXPRaw( bytes ); 
		} else if( clazz == Short_ARRAY ){
			Short[] shorts = (Short[])o;
			int n = shorts.length ;
			int[] ints = new int[shorts.length];
			for( int i=0; i<n; i++){
				ints[i] = shorts[i].intValue() ;
			}
			ans = new REXPInteger( ints ); 
		} else if( clazz == Double_ARRAY ){
			Double[] doubles = (Double[])o;
			double n = doubles.length ;
			double[] d = new double[doubles.length];
			for( int i=0; i<n; i++){
				d[i] = doubles[i].doubleValue() ;
			}
			ans = new REXPDouble( d ); 
		} else if(clazz == double_ARRAY ) {
			ans = new REXPDouble( (double[])o ) ;
		} else if(clazz == short_ARRAY ){
			short[] data = (short[])o;
			int[] ints   = new int[data.length] ;
			for( int i=0; i<data.length; i++){
				ints[i] = (int)data[i] ;
			}
			ans = new REXPInteger( ints ); 
		} else if( RPlugin.getR() instanceof JRIEngine ){
			JRIEngine r = (JRIEngine)RPlugin.getR();
			ans = createRJavaRef(o, r ) ;
		} else{
			Log.log( Log.ERROR, null, "unhandled class" + o.getClass() ) ;
			ans = new REXPNull() ;
		}
		return ans; 
	}
	
	/**
	 * Creates a "jObjRef" object in R that references the object
	 * @param o object to wrap into a "jobjRef"
	 * @param r the R engine
	 * @return a reference to the object
	 */
    public static REXPReference createRJavaRef(Object o, JRIEngine r) {
    	org.rosuda.JRI.REXP old_rx = r.getRni().createRJavaRef( o ) ;
    	REXPReference rx = new REXPReference( r, new Long(old_rx.xp) )  ;
    	return rx ;
    }

}
