package com.addictedtor.jedit.objects;

import java.awt.Component;

import org.gjt.sp.jedit.EBMessage;

public class DisplayMessage extends EBMessage {

	private Component component ; 
	public DisplayMessage( Component component ){
		super(null) ;
		this.component = component; 
	}
	public Component getComponent(){
		return component; 
	}
	
}
