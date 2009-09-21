package com.addictedtor.jedit.statusbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.jEdit;

import com.addictedtor.jedit.tools.IconFactory;

@SuppressWarnings("serial")
public class RSyncButton extends JButton {

	/**
	 * The icon used when synchronization is on
	 */
	private static final Icon ON  = IconFactory.icon( "statusbar/sync-on.png" );
	
	/**
	 * The icon used when synchronization is off
	 */
	private static final Icon OFF = IconFactory.icon( "statusbar/sync-off.png" );
	
	/**
	 * Tool tip text when the sync is on
	 */
	private static final String on_message = "disable live synchronization of this buffer" ;
	
	/**
	 * Tool tip text when the sync is on
	 */
	private static final String off_message = "enable live synchronization of this buffer" ;
	
	public RSyncButton( final RLiveFileWidget widget ){
		super( "" ) ;
		setBorder( BorderFactory.createEmptyBorder() ) ;
		setIcon( IconFactory.getEmptyIcon() ) ;
		setAction( new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Buffer buffer = jEdit.getActiveView().getBuffer() ;
				if( buffer.getMode().getName().equals("R")){
					widget.toggleLive( buffer ) ;
				}
			}
			
		} );
	}
	
	public void set( RLiveBufferProperty prop){
		set( prop.isLive() ) ;
	}
	
	public void set( boolean live ){
		setVisible( true) ;
		setIcon( live ? ON : OFF ) ;
		setToolTipText(live? on_message : off_message) ;
	}

	public void empty() {
		setVisible( false ) ;
	}
	
	
}
