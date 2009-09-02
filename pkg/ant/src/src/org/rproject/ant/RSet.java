package org.rproject.ant;

import org.apache.tools.ant.BuildException;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

/**
 * Ant task that sets a property to contain the result of
 * an R expression.
 * 
 * <r-set code="R.home()" property="r.home" />
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class RSet extends RTask {

	private String code = "" ; 
	private String property = "r.default.property" ;
	
	@Override
	public void execute() throws BuildException {
		REXP res = run( code ) ;
		String out;
		try {
			out = res.asString();
		} catch (REXPMismatchException e) {
			throw new REXPMismatchBuildException( "converting to String", e ) ;
		}
		getProject().setProperty( property, out ) ;
	}
	
	/**
	 * The R code to execute. At the moment, no checking is done
	 * to ensure that the result is a character string, it just has to be 
	 * 
	 * @param code R expression
	 */
	public void setCode( String code){
		this.code = code ; 
	}
	
	/**
	 * The name of the property to set
	 * @param property the name of the property to set
	 */
	public void setProperty( String property ){
		this.property = property ; 
	}
	
	public void addText(String code){
		this.code = code;
	}

}
