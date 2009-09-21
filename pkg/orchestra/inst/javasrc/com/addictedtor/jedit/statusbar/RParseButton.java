package com.addictedtor.jedit.statusbar;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

import com.addictedtor.jedit.tools.IconFactory;

@SuppressWarnings("serial")
public class RParseButton extends JButton {
	
	/**
	 * The icon used to show that the file has been source correctly
	 */
	private static final Icon OK = IconFactory.icon( "statusbar/sync-ok.png" );
	
	/**
	 * The icon used to show that there was a problem sourcing the file
	 */
	private static final Icon WRONG = IconFactory.icon( "statusbar/sync-wrong.png" );
	
	private static final String ok_message = "sucessfull source" ;
	
	private static final String wrong_message = "problem sourcing the buffer" ;
	
	public RParseButton( RLiveFileWidget widget ){
		super( "" ) ;
		setBorder( BorderFactory.createEmptyBorder() ) ;
		setIcon( IconFactory.getEmptyIcon() ) ;
	}
	
	public void set(boolean ok){
		setVisible( true ) ;
		setIcon( ok ? OK : WRONG ) ;
		setToolTipText(ok?ok_message:wrong_message) ;
	}
	
	public void empty() {
		setVisible( false ) ;
	}
}
