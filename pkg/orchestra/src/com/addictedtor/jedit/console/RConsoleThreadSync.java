package com.addictedtor.jedit.console;

import java.util.LinkedList;
import java.util.Queue;

public class RConsoleThreadSync {

	@SuppressWarnings("unused")
	private RConsole shell;

	private Queue<RConsoleAction> actions ;

	public RConsoleThreadSync(RConsole shell) {
		this.shell = shell;
		actions = new LinkedList<RConsoleAction>(); 
	}
	
	public synchronized RConsoleAction waitForAction( ){
		/* if there is an action, return it immediately */
		if( actions.size() > 0){
			return actions.poll() ;
		}
		
		/*
		 * wait until there is an action to perform
		 */
		while( true){
			while( actions.isEmpty() ){
				try {
					wait( 100 );
				} catch (InterruptedException e) {}
			}
			RConsoleAction action = actions.poll() ;
			if( action != null){
				return action ;
			}
		}
		
	}
	
	public synchronized void add( RConsoleAction action){
		actions.add(action) ;
	}
	
	public synchronized boolean hasMoreActions( ){
		return !actions.isEmpty() ;
	}

}
