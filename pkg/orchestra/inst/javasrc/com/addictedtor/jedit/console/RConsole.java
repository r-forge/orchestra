package com.addictedtor.jedit.console;

import java.awt.Color;

import javax.swing.SwingUtilities;

import org.gjt.sp.util.Log;
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.rengine.jri.JeditRMainLoopCallbacks;

import console.Console;
import console.Output;
import console.Shell;

/**
 * The implementation of the Shell for R 
 * @author romain
 *
 */
public class RConsole extends Shell {

	/**
	 * The console loop thread
	 */
	private RConsoleThread loop; 
	
	private JeditRMainLoopCallbacks callbacks; 
	
	private RConsoleSync sync; 
	
	private String continuePrompt ; 
	
	/**
	 * Constructor, holds a reference to the R engine and start the console loop thread
	 */
	public RConsole() {
		super("R");
		callbacks = new JeditRMainLoopCallbacks( this ) ;
		sync = new RConsoleSync(this); 
		loop = new RConsoleThread( this ) ;
		
		REngine r = RPlugin.getR() ;
		if( r instanceof JRIEngine){
			Rengine re = ((JRIEngine)r).getRni() ;
			re.addMainLoopCallbacks(callbacks);
			re.startMainLoop() ;
			try {
				continuePrompt = r.parseAndEval( "getOption( 'continue' )" ).asString() ;
			} catch (REXPMismatchException e) {
			} catch (REngineException e) {
			}
		}
		
		SwingUtilities.invokeLater( new Runnable(){
			public void run(){
				loop.start( ); 
			}
		});
		
	}

	public RConsoleThread getLoop(){
		return loop; 
	}
	
	/**
	 * Prints the R version string: 
	 * &gt; version$version.string
	 * 
	 * @param output the output area to use
	 */
	@Override
	public void printInfoMessage(Output output) {
		String version = "";
		try {
			version = RPlugin.getR().parseAndEval( "version$version.string" ).asString() ;
		} catch (Exception e) {
			Log.log(Log.WARNING, this, e.getMessage());
		} finally {}
		output.print(Color.GREEN, version);
	}

	@Override
	public void execute(Console console, String input, Output output,
			Output error, String command) {
		
		sync.addInput( command, false ) ;
	}
	
	public void addAction( RConsoleAction action){
		getLoop().getSync().add(action); 
	}

	@Override
	public void stop(Console console) {	}

	@Override
	public boolean waitFor(Console console) {
		return true;
	}

	@Override
	public void printPrompt(Console console, Output output) {}

	public CompletionInfo getCompletions(String command) {
		return null;
	}

	public RConsoleSync getSync() {
		return sync ;
	}
	
	public String getContinuePrompt( ){
		return continuePrompt ; 
	}

}
