package com.addictedtor.jedit.objects;

import java.awt.Component;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPEnvironment;

import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.display.SimpleDisplayBuilder;
import com.addictedtor.jedit.tools.RServiceManager;


public class NodeDisplayFactory {

	public static final String SERVICE = "com.addictedtor.jedit.objects.NodeDisplayBuilder" ;

	public static Component getComponent(REXP object, String name){
		NodeDisplayBuilder builder;
		
		if( object instanceof REXPEnvironment ){
			return null; 
		} else if( object == null){
			builder = new SimpleDisplayBuilder();
		} else {
			builder = (NodeDisplayBuilder) RServiceManager.getService(SERVICE, object.getClass().getSimpleName()) ;
		}
		if( builder == null){
			builder = new SimpleDisplayBuilder();
		}
        try {
            return builder.getComponent(object, name) ;
        } catch (Exception e) {
            ExceptionManager.send(e);
            return null ;
        }
    }
}
	