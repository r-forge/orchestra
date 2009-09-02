package org.rproject.ant;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 * Dummy implementation of the main loop callbacks, 
 * used to grab output and redirect it to appropriate streams
 * 
 * @author romain
 *
 */
public class AntRMainLoopCallbacks implements RMainLoopCallbacks {

	private MessageListener listener = null ;
	
	public void rBusy(Rengine arg0, int arg1) {}

	public String rChooseFile(Rengine arg0, int arg1) {
		System.err.println( "Cannot choose a file in this context" ) ;
		return null; 
	}

	public void rFlushConsole(Rengine arg0) {
		if( listener != null ){
			listener.flush() ;
		}
	}
	public void rLoadHistory(Rengine arg0, String arg1) {}

	public String rReadConsole(Rengine arg0, String arg1, int arg2) {
		try{
			Thread.sleep( 100 ) ;
		} catch( InterruptedException e){}
		return " " ;
	}

	public void rSaveHistory(Rengine arg0, String arg1) {}

	public void rShowMessage(Rengine arg0, String arg1) {
		if( listener != null ){
			listener.send( new Message( arg1) ) ;
		}
	}

	public void rWriteConsole(Rengine arg0, String arg1, int arg2) {
		if( listener != null){
			listener.send( new Message( arg1, arg2 ) ) ;
		}
	}

	public void setMessageListener( MessageListener listener){
		this.listener = listener ; 
	}
}
