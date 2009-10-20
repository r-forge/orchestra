package com.addictedtor.jedit.editbus;

import org.gjt.sp.jedit.EBMessage;

/**
 * message sent when the browse context has to be flushed 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class BrowseContextFlush extends EBMessage {

	public BrowseContextFlush( ){
		super(null) ;
	}
	
}
