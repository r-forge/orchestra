package com.addictedtor.jedit.editbus;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import org.gjt.sp.jedit.EBComponent;

public class REditBus {

	protected static Queue<REBMessage> messages  = new LinkedList<REBMessage>() ; 
	
	protected static Vector<EBComponent> components = new Vector<EBComponent>() ;
	
	public static synchronized void addToBus( EBComponent component ){
		if( !components.contains( component) ) {
			components.add( component ) ;
		}
	}
	
	public static synchronized void removeFromBus( EBComponent component){
		if( components.contains(component) ){
			components.remove( component ) ;
		}
	}
	
	// TODO; maybe send should take EBMEssage as well, check if the message
	//       is an instance of REBMessage and if not just send the message to the standard
	//       bus
	public static synchronized void send(REBMessage message ){
		messages.add( message ) ;
	}
	
	// TODO: make a Thread that loops around, waits for something to do
	//       ! messages.isEmpty(), poll the message from the queue messages.poll()
	//       send the message to each component
}
