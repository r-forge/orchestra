package com.addictedtor.jedit.editbus;

import org.gjt.sp.jedit.EBMessage;

/**
 * Super class for all editbus messages specific to this plugin
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class REBMessage extends EBMessage {

	/**
	 * Constructor 
	 * @param source the source of the message
	 */
	public REBMessage(Object source) {
		super(source);
	}
	
	/**
	 * Constructor (no source)
	 */
	public REBMessage() {
		this(null) ;
	}
	
}
