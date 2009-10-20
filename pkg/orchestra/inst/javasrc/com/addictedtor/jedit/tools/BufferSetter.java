package com.addictedtor.jedit.tools;

import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.jEdit;

public class BufferSetter {

	public static void set( final String file ){
		SwingUtilities.invokeLater( new Runnable(){
			
			public void run(){
				jEdit.openFile(jEdit.getActiveView() , file ) ;
			}
			
		} ); 
	}
	
}
