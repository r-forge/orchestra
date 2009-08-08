package com.addictedtor.jedit.objects;

import com.addictedtor.jedit.tools.IconFactory;
import com.addictedtor.jedit.tools.RServiceManager;
import org.rosuda.REngine.REXP;

import javax.swing.*;

public class REXPIconFactory {

	private static final String SERVICE = "com.addictedtor.jedit.objects.REXPIconBuilder" ;
	
	public static Icon getIcon( REXP object ){
		Object o = RServiceManager.getService( SERVICE , object.getClass().getSimpleName() ) ;
		if( o == null) {
			return IconFactory.getEmptyIcon(); 
		}
		return ((REXPIconBuilder)o).getIcon(object) ;
	}
	
}
