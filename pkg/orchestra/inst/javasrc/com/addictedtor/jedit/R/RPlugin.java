package com.addictedtor.jedit.R ;

import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.msg.EditPaneUpdate;
import org.gjt.sp.jedit.msg.ViewUpdate;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.matcher.RStructureMatcher;
import com.addictedtor.jedit.statusbar.RStatusWidgetFactory;

/**
 * Main class of the PowerEditor plugin for jedit
 * 
 * @author Romain Francois <francoisromain@free.fr>
 * 
 */
public class RPlugin extends EBPlugin {

	/**
	 * the r engine used to communicate with R
	 */
 	private static REngine r = null ; 
	
	/**
	 * Prefix for the menu of the plugin
	 */
	public static final String MENU = "R.menu";

	/**
	 * Prefix for the options
	 */
	public static final String OPTION_PREFIX = "options.R.";

	/**
	 * R Structure matcher
	 */
	private static final RStructureMatcher matcher = new RStructureMatcher();
	
	/**
	 * Start method of the plugin. starts the R engine, and load the orchestra package
	 */
	@Override
	public void start() {
		
		/* 
		 AutomaticFunctionPopupThread popupThread = new AutomaticFunctionPopupThread(); 
		popupThread.start() ; 
		*/
	}

	/**
	 * Stop method of the plugin. Placeholder at the moment.
	 */
	@Override
	public void stop() {
		/* popupThread.requestStop(); */ 
	}

	/**
	 * Handles an EditBus message
	 * 
	 * This listens to the messages EditPaneUpdate of the EditBus in order to
	 * place our structure matcher before jedit usual otherwise it is never used
	 * 
	 * @see EditPaneUpdate.CREATED
	 */
	@Override
	public void handleMessage(EBMessage message) {
		if (message instanceof EditPaneUpdate) {
			EditPaneUpdate message_ = (EditPaneUpdate) message;
			if (message_.getWhat() == EditPaneUpdate.CREATED) {
				matcher.install( message_.getEditPane().getTextArea() ) ;
			}
		} else if(message instanceof ViewUpdate){
			ViewUpdate message_ = (ViewUpdate)message; 
			if( message_.getWhat() == ViewUpdate.CREATED ){
				RStatusWidgetFactory.install() ;
			}
		}
	}
	
	/**
	 * Returns the R engine currently in use
	 */
	public static REngine getR(){
		
		if( r == null){
			// This is a bit hairy but it retrieves the JRIEngine reflectively 
			// this plugin is loaded after the orchestra_trigger plugin
			try{
				Class<?> OP = Class.forName("com.addictedtor.orchestra.OrchestraPlugin") ;
				r = (JRIEngine) OP.getMethod("getREngine", (Class<?>[])null).invoke(null, (Object[])null );
			} catch( Exception e){
				e.printStackTrace() ;
			}
		}
		return r ;
	}

}

