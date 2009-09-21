package com.addictedtor.jedit.objects.icons;

import javax.swing.Icon;

import org.gjt.sp.jedit.ServiceManager;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.REXPIconBuilder;
import com.addictedtor.jedit.tools.IconFactory;

/**
 * Icon builder for S4 objects. This uses the service provided by the class
 * {@link S4IconBuilder} that you may implement to get a different icon. 
 * Currently this service is implemented for the "classRepresentation" class
 * which represents classes in the S4 world. If no appropriate service is found, 
 * a default icon is used
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class IconBuilder_REXPS4 extends REXPIconBuilder {

	/**
	 * The class symbol
	 */
	public static final String CLASS  = "class" ;
	
	/**
	 * Default icon used when no suitable service implementation of {@link S4IconBuilder} is found
	 */
	public static final Icon DEFAULT = SimpleIconBuilder.getIcon("s4object", true) ;
	
	/**
	 * This uses the service provided by the {@link S4IconBuilder}, to which the 
	 * class of the object is given. If no appropriate service is found, it will return the
	 * default icon
	 */
	@Override
	public Icon getIcon(REXP object) {
		REXPList attrs = ((REXP)object)._attr() ;
		if( attrs == null ){
			return IconFactory.getEmptyIcon(); 
		}
		RList attributes = attrs.asList() ;
		String clazz; 
		try {
			REXP clazz_ = attributes.at( CLASS ) ;
			if( clazz_ == null){
				return DEFAULT ;
			} else{
				clazz = clazz_.asStrings()[0] ;
				if( clazz == null){
					return DEFAULT ;
				}
			}
		} catch (REXPMismatchException e) {
			ExceptionManager.send( e ) ;
			return DEFAULT; 
		}
		S4IconBuilder o = (S4IconBuilder)ServiceManager.getService( "com.addictedtor.jedit.objects.icons.S4IconBuilder" , clazz) ;
		if( o == null){
			return DEFAULT ;
		}
		return ((S4IconBuilder)o).getIcon( object ) ;
		
	}
	
}

