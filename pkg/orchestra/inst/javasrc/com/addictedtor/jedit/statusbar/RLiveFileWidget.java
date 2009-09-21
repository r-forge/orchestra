package com.addictedtor.jedit.statusbar;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.statusbar.Widget;
import org.gjt.sp.jedit.msg.BufferUpdate;
import org.gjt.sp.jedit.msg.EditPaneUpdate;
import org.gjt.sp.jedit.msg.ViewUpdate;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.rengine.jri.RBusyMessage;

/**
 * Status widget to display R status 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class RLiveFileWidget implements Widget, EBComponent {

	private RSyncButton button_sync ;
	private RParseButton button_uptodate ;
	
	@SuppressWarnings("unused")
	private View view; 
	
	public RLiveFileWidget( final View view){
		button_sync = new RSyncButton( this ) ;
		button_uptodate = new RParseButton( this ); 
		this.view = view; 
		final EBComponent w = this ;
		SwingUtilities.invokeLater( new Runnable(){
			public void run(){
				EditBus.addToBus(w) ;
			}
		} ); 
	}
	
	/** 
	 * Returns the label that displays R status information
	 * 
	 * @see org.gjt.sp.jedit.gui.statusbar.Widget#getComponent()
	 */
	@Override
	public JComponent getComponent() {
		JPanel panel = new JPanel( new FlowLayout() ) ;
		panel.add( button_sync ) ;
		panel.add( button_uptodate ) ;
		panel.setBorder(BorderFactory.createEmptyBorder()) ;
		return panel; 
	}

	/**
	 * Dummy implementation.
	 */
	@Override
	public void propertiesChanged() {}

	/** 
	 * Dummy implementation
	 * @see org.gjt.sp.jedit.gui.statusbar.Widget#update()
	 */
	@Override
	public void update() {}
	
	/**
	 * Respond to RBusyMessage by setting the icon 
	 * @see RBusyMessage
	 */
	@Override
	public void handleMessage(EBMessage message) {
		if( message instanceof BufferUpdate ){
			handle_bufferupdate( (BufferUpdate)message ) ;
		} else if( message instanceof EditPaneUpdate ){
			handle_editpaneupdate( (EditPaneUpdate)message ) ;
		} else if( message instanceof ViewUpdate){
			handle_viewupdate( (ViewUpdate)message ) ;
		}
	}
	
	private void handle_viewupdate(ViewUpdate message) {
		Object what = message.getWhat() ;
		if( what == ViewUpdate.EDIT_PANE_CHANGED || what == ViewUpdate.ACTIVATED ||  what == ViewUpdate.CREATED){
			handle_bufferchange(message.getView().getBuffer()) ;
		}
	}

	private void handle_editpaneupdate(EditPaneUpdate message) {
		if( message.getWhat() == EditPaneUpdate.BUFFER_CHANGED || message.getWhat() == EditPaneUpdate.CREATED){
			handle_bufferchange( message.getEditPane().getBuffer() ); 
		}
	}
	
	private void handle_bufferchange( Buffer buffer){
		if( buffer.isLoading() ){
			return ; 
		}
		if( buffer.getMode().getName().equals("R")){
			Object o = buffer.getProperty("R-livebuffer") ;
			RLiveBufferProperty live;  
			if( o == null || ! (o instanceof RLiveBufferProperty) ){
				live = new RLiveBufferProperty() ;
				buffer.setProperty( "R-livebuffer" , live ) ;
			} else{
				live = (RLiveBufferProperty)o; 
			}
			button_sync.set( live ) ;
		} else{
			button_sync.empty(  ) ;
			button_uptodate.empty() ;
		}
	}
	
	
	private void handle_bufferupdate( BufferUpdate message){
		Buffer buffer = message.getBuffer() ;
		if( !buffer.isLoaded()){
			return ; 
		}
		if( buffer.getMode().getName().equals( "R") ){
			if( message.getView() == BufferUpdate.LOADED ){
				buffer.setProperty( "R-livebuffer" , new RLiveBufferProperty() ) ;
			} else if( message.getWhat() == BufferUpdate.SAVED ){
				sourceBuffer( buffer ) ;
			}
		} 
	}
	
	private void sourceBuffer(Buffer buffer){
		Object o = buffer.getProperty("R-livebuffer") ;
		RLiveBufferProperty live;  
		if( o == null || ! (o instanceof RLiveBufferProperty) ){
			live = new RLiveBufferProperty() ;
			buffer.setProperty( "R-livebuffer" , live ) ;
		} else{
			live = (RLiveBufferProperty)o; 
		}
		if( live.isLive() ){
			REngine r = RPlugin.getR() ;
			int key = r.lock(); 
			String encoding = (String)buffer.getProperty(Buffer.ENCODING) ;
			String cmd = "try( source( '"+buffer.getPath()+"', encoding = '"+ encoding+ "' ), silent = TRUE )" ;
			// TODO: collaborate with sidekick
			try{
				r.parseAndEval(cmd) ;
			} catch( REngineException e){
				button_uptodate.set( false ) ;
			} catch (REXPMismatchException e) {
				button_uptodate.set( false ) ;
			} finally{
				r.unlock(key ) ;
			}
			button_uptodate.set( true ) ;
		}
	}
	
	public boolean toggleLive( Buffer buffer ){
		Object o = buffer.getProperty("R-livebuffer") ;
		RLiveBufferProperty live;  
		if( o == null || ! (o instanceof RLiveBufferProperty) ){
			live = new RLiveBufferProperty() ;
			buffer.setProperty( "R-livebuffer" , live ) ;
		} else{
			live = (RLiveBufferProperty)o; 
		}
		boolean status = live.toggle() ;
		button_sync.set( live ) ;
		if( status ){
			sourceBuffer( buffer ) ;
		} else {
			button_uptodate.empty() ;
		}
		return status ; 
	}
	
	
}