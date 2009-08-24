package com.addictedtor.jedit.rengine.jri;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.browser.VFSFileChooserDialog;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

import com.addictedtor.jedit.console.ErrorAction;
import com.addictedtor.jedit.console.MessageAction;
import com.addictedtor.jedit.console.OutputAction;
import com.addictedtor.jedit.console.PromptAction;
import com.addictedtor.jedit.console.RConsole;
import com.addictedtor.jedit.console.RConsoleAction;
import com.addictedtor.jedit.editbus.EBCommandDone;

import console.ConsolePlugin;

public class JeditRMainLoopCallbacks implements RMainLoopCallbacks {

	private RConsole rconsole ;
	private boolean browse = true ;
	
	public JeditRMainLoopCallbacks(RConsole rconsole) {
		this.rconsole = rconsole; 
	}

	/**
	 * Sends a message to the edit bus to indicate if R is busy
	 * 
	 * @param engine used Rengine
	 * @param which busy (1) or not (0)
	 */
	@Override
	public void rBusy(Rengine engine, int which) {
		EditBus.send( new RBusyMessage( which == 1)) ;
	}

	/**
	 * Waits for user input
	 */
	@Override
	public String rReadConsole(Rengine engine, String prompt, int addToHistory) {
		
		if( prompt.startsWith( "Browse[" ) ){
			browse = true; 
			sendLater( new BrowseContextUpdate() ) ;
		} else if( prompt.equals(rconsole.getContinuePrompt() ) ){
			/* dummy, maybe set something up for completions */
		} else {
			browse = false;
			sendLater( new BrowseContextFlush() ) ;
		}
		
		/* stop the animation when we get a prompt */
		ConsolePlugin.getConsole(jEdit.getActiveView()).getOutput("R").commandDone(); 
		sendLater( new EBCommandDone( ) ) ;
		
		/* print the prompt */
		RConsoleAction action ; 
		action = new PromptAction( prompt ) ;
		rconsole.addAction( action ) ;
		
		/* waits for input and send it back to R */
		return rconsole.getSync().waitForInput() + "\n" ;
	}

	/**
	 * Appends the message to the list of message the console loop 
	 * has to print
	 */
	@Override
	public void rShowMessage(Rengine arg0, String arg1) {
		RConsoleAction action = new MessageAction( arg1) ;
		rconsole.addAction( action ) ;
	}

	/**
	 * Appends the message to the list of messages to be handled by the console loop
	 */
	@Override
	public void rWriteConsole(Rengine engine, String message, int type) {
		RConsoleAction action ; 
		if( type == 0 ){
			action = new OutputAction( message ) ;
		} else{
			action = new ErrorAction( message ); 
		}
		rconsole.addAction( action ) ;
	}
	
	/**
	 * Clears the console
	 * @see console.Console#clear()
	 */
	@Override
	public void rFlushConsole(Rengine arg0) {
		/* ConsolePlugin.getConsole(jEdit.getActiveView()).clear() ; */
	}

	
	/**
	 * Choose a file invoked be file.choose() (R callback).
	 * 
	 * @param engine used Rengine
	 * @param newFile if it's a new file
	 */
	@Override
	public String rChooseFile(Rengine engine, int newFile) {
		File cwd = new File( engine.eval( "getwd()" ).asString() ) ;
		VFSFileChooserDialog dialog = new VFSFileChooserDialog( jEdit.getActiveView(), cwd.getAbsolutePath(), VFSBrowser.OPEN_DIALOG, false ) ;
		String[] selection = dialog.getSelectedFiles();
		if( selection == null){
			return null; 
		} else {
			return selection[0] ;
		}
	}


	// TODO: implement these callbacks
	
	/**
	 * Dummy implementation, does not respond to this call back yet
	 */
	@Override
	public void rLoadHistory(Rengine arg0, String arg1) {}

	/**
	 * Dummy implementation, does not respond to this call back yet
	 */
	@Override
	public void rSaveHistory(Rengine arg0, String arg1) {}
	
	/**
	 * Send the message to the edit bus later
	 * @param message message for the edit bus
	 */
	private void sendLater( final EBMessage message){
		SwingUtilities.invokeLater( new Runnable(){
			public void run(){
				EditBus.send( message ) ;
			}
		}) ;
	}

	/** 
	 * Is R currently inside an an environment browser
	 * @return true if R is currently inside a browser call
	 */
	public boolean isBrowsing(){
		return browse ;
	}
	
}
