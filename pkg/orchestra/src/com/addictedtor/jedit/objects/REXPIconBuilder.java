package com.addictedtor.jedit.objects;

import javax.swing.Icon;

import org.gjt.sp.jedit.ServiceManager;
import org.rosuda.REngine.REXP;

/**
 * icon builder service. This is handled by the {@link ServiceManager} system.
 * Implementations of this service are provided for most REXP subclasses
 * in the com.addictedtor.jedit.objects.icons package
 * 
 * @See {@link ServiceManager}
 * @author Romain Francois <francoisromain@free.fr>
 */
public abstract class REXPIconBuilder {

	/**
	 * Returns an icon suitable for the object
	 * 
	 * @return the icon
	 */
	public abstract Icon getIcon( REXP object ) ;
	
}
