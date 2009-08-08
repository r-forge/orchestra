package com.addictedtor.jedit.statusbar;

import org.gjt.sp.jedit.Buffer;

/**
 * Captures the "live" property of an R file. 
 * When a buffer is "live", saving the buffer [Ctrl]+[S]
 * will also source the file in the R session
 * 
 * These objects are used in the "R-livebuffer" property 
 * of a Buffer object
 *  
 * @author Romain Francois <francoisromain@free.fr>
 * @see Buffer#getProperty(Object)
 */
public class RLiveBufferProperty{
	
	/**
	 * Is the related buffer live
	 */
	private boolean live ; 
	
	/**
	 * Constructor. buffers are not live by default
	 */
	public RLiveBufferProperty(){
		this(false); 
	}
	
	/**
	 * Constructor with a live status
	 * @param live true if the buffer is live
	 */
	public RLiveBufferProperty(boolean live){
		this.live = live; 
	}
	
	/**
	 * @return true if the buffer is live
	 */
	public boolean isLive(){
		return live ; 
	}
	
	/**
	 * Toggles the live status
	 * @return the new live status
	 */
	public boolean toggle(){
		live = !live; 
		return live;
	}
}