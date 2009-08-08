package com.addictedtor.jedit.objects;

import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * The toolbar of the environment panel  
 * @author Romain Francois <francoisromain@free.fr>
 */
@SuppressWarnings("serial")
public class EnvironmentToolbar extends JToolBar {
	
	/**
	 * Constructor. Dummy currently
	 */
	public EnvironmentToolbar(){
		super( ) ;
		setFloatable(false); 
		add( new JButton("to") ) ;
		add( new JButton("do") ) ;
	}
	
}
