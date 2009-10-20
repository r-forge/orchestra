package com.addictedtor.jedit.editbus;

import org.gjt.sp.jedit.EBMessage;

/**
 * edit bus message that indicates that R is busy
 * 
 * @author romain
 *
 */
public class RBusyMessage extends EBMessage {

	private boolean busy ;
	
	/**
	 * Constructor 
	 * @param busy true if R is busy 
	 */
	public RBusyMessage(boolean busy) {
		super(null); 
		this.busy = busy; 
	}
	
	/**
	 * Is R busy
	 * @return true if this message informs that R is busy
	 */
	public boolean getBusy(){
		return busy ;
	}

}