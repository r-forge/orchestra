package com.addictedtor.jedit.console;

import java.awt.Color;

public class OutputAction extends RConsoleAction {
	
	public OutputAction(String data){
		super(data);
	}
	
	public Color getForeground(){
		return Color.BLUE ;
	}
	
}
