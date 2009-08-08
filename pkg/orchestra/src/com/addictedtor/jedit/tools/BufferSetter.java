package com.addictedtor.jedit.tools;

import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;

public class BufferSetter {

	public static void set( String file ){
		final String f  = file; 
		SwingUtilities.invokeLater( new Runnable(){
			
			public void run(){
				View view = jEdit.getActiveView(); 
				jEdit.openFile(view, f) ;
			}
			
		} ); 
	}
	
}
