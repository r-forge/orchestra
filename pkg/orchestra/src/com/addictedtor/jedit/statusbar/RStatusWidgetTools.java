package com.addictedtor.jedit.statusbar;

import java.util.StringTokenizer;

import org.gjt.sp.jedit.jEdit;

public class RStatusWidgetTools {

	/**
	 * Checks if the R widget is present in the list of widgets and force it otherwise
	 */
	public static void install( ) {
		String prop = jEdit.getProperty("view.status") ;
		StringTokenizer widgets = new StringTokenizer( prop, " " ) ;
		boolean hasRWidget = false; 
		boolean hasRliveWidget = false ;
		while( widgets.hasMoreTokens() ){
			String w = widgets.nextToken() ;
			if( "R".equals( w ) ){
				hasRWidget = true; 
			} else if( "Rlive".equals(w) ){
				hasRliveWidget = true; 
			}
			if( hasRWidget && hasRliveWidget ){
				break; 
			}
		}
		
		if( hasRliveWidget && hasRWidget ){
			return ; 
		}
		if( ! hasRWidget ){
			prop = prop + " R" ;
		}
		if( ! hasRliveWidget ){
			prop = prop + " Rlive" ;
		}
		
		jEdit.setProperty("view.status", prop) ;
		jEdit.propertiesChanged() ;
	
		
	}
	
}
