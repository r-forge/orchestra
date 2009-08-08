package com.addictedtor.jedit.console;

import java.awt.Color;

public class PromptAction extends RConsoleAction {

	public PromptAction(String data) {
		super(data);
	}
	
	@Override
	public Color getForeground(){
		return Color.gray; 
	}

}
