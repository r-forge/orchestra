package com.addictedtor.jedit.console;

import java.awt.Color;

public class ErrorAction extends OutputAction {

	public ErrorAction(String data) {
		super(data);
	}

	public Color getForeground( ){
		return Color.RED ;
	}
}
