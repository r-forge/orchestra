package com.addictedtor.jedit.objects;

import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.text.SimpleNodeTextBuilder;
import com.addictedtor.jedit.tools.RServiceManager;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

public class NodeTextFactory {

	public static final String SERVICE = "com.addictedtor.jedit.objects.NodeTextBuilder" ; 
	
	public static String getText( REXP object, String name ){
		NodeTextBuilder builder; 
		if( object == null){
			builder = new SimpleNodeTextBuilder();  
		} else {
			builder = (NodeTextBuilder) RServiceManager.getService( SERVICE, object.getClass().getSimpleName() ) ;
		}
		if( builder == null){
			builder = new SimpleNodeTextBuilder(); 
		}
        try {
            return builder.getText(object, name) ;
        } catch (REXPMismatchException e) {
            ExceptionManager.send(e);
            return null;
        }
    }
	
}
