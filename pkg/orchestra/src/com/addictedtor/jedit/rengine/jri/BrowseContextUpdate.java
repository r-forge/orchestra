package com.addictedtor.jedit.rengine.jri;

import org.gjt.sp.jedit.EBMessage;

/**
 * Message that indicates edit bus components that the 
 * browse context has changed (used for R debugging)
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class BrowseContextUpdate extends EBMessage {
	public BrowseContextUpdate(){
		super(null) ;
	}
}
