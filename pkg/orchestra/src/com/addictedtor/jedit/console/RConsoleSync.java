package com.addictedtor.jedit.console;

import java.util.LinkedList;
import java.util.Queue;

import org.rosuda.REngine.REngine;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;

/**
 * Console synchronizer. Keeps queue of commands (single strings)
 * synchronized.  
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class RConsoleSync {

	/**
	 * The command queue
	 */
	private Queue<String> input ; 

	private RConsole console ; 
	
	/**
	 * Constructor
	 */
	public RConsoleSync(RConsole console) {
		input = new LinkedList<String>();
		this.console = console; 
	}

	/**
	 * Waits until the queue is not empty and returns its head
	 * @return the head of the command queue
	 */
	public synchronized String waitForInput() {
		while ( input.isEmpty() ){
			try {
				wait(100);
				REngine r = RPlugin.getR() ;
				/* TODO: is this needed ? look what JGR does */
				if( r instanceof JRIEngine ){
					((JRIEngine)r).getRni().rniIdle() ;
				}
			} catch (InterruptedException e) { }
		}
		return input.poll() ; 
	}

	/**
	 * Adds a command to the queue
	 * @param msg Command
	 * @param echo should the command be echoed in the console
	 */
	public synchronized void addInput(String msg, boolean echo) {
		if( echo ){
			console.addAction( new InputAction( msg + "\n" ) ) ;
		}
		input.add(msg); 
		notifyAll();
	}
	
}
