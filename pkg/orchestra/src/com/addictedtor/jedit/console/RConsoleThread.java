package com.addictedtor.jedit.console;

import org.gjt.sp.jedit.jEdit;

import console.Console;
import console.ConsolePlugin;
import console.Output;

public class RConsoleThread extends Thread {

	private RConsoleThreadSync sync; 
	
	public RConsoleThread( RConsole shell ){
		sync = new RConsoleThreadSync( shell ) ; 
		setName( "JRI-consoleSync" ) ; 
	}
	
	public void run(){
		while( true){
			RConsoleAction nextaction = sync.waitForAction() ;
			nextaction.print( getOutput() ) ;
		}
	}

	private Output getOutput(){
		Console console = ConsolePlugin.getConsole(jEdit.getActiveView()) ;
		return console.getOutput("R") ;
	}

	public RConsoleThreadSync getSync() {
		return sync;
	}
	
}
