package com.addictedtor.jedit.R ;

import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.ServiceManager;
import org.gjt.sp.jedit.bsh.NameSpace;
import org.gjt.sp.jedit.bsh.UtilEvalError;
import org.gjt.sp.jedit.msg.EditPaneUpdate;
import org.gjt.sp.jedit.msg.ViewUpdate;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;

import com.addictedtor.jedit.matcher.RStructureMatcher;
import com.addictedtor.jedit.rengine.REngineService;
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
	 * Start method of the plugin. starts the R engine, and load the jeditr package
	 */
	@Override
	public void start() {
		SwingUtilities.invokeLater( new Runnable(){
				public void run(){
					r = ( (REngineService) ServiceManager.getService("com.addictedtor.jedit.rengine.REngineService", "JRI") ).getEngine() ;
					try {
						r.parseAndEval("require( 'jeditr', quiet = TRUE )") ;
					} catch (REngineException e) {
					} catch (REXPMismatchException e) {
					}
					try{
						NameSpace ns = org.gjt.sp.jedit.BeanShell.getNameSpace(); 
						ns.setVariable("r", r) ;
					} catch(UtilEvalError e){}
					
				}
		} ); 
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
		return r ;
	}

}

