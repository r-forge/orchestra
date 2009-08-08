package com.addictedtor.jedit.error;

import java.util.LinkedList;
import java.util.Queue;

public class ExceptionManager {

	// TODO: Should the queue be made synchronized
	@SuppressWarnings("unused")
	private static Queue<Throwable> exceptions = new LinkedList<Throwable>(); 
	
	public static synchronized void send( Throwable exception ){
		exception.printStackTrace() ;
	}
	
	public static synchronized Throwable poll( ){
		/* return exceptions.poll() ; */
		return null; 
	}
	
}
