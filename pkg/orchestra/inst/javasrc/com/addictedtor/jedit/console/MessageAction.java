package com.addictedtor.jedit.console;

import java.awt.Color;

public class MessageAction extends RConsoleAction {

	public MessageAction(String data) {
		super(data);
	}
	
	public Color getColor(){
		return Color.red ;
	}
	
}
