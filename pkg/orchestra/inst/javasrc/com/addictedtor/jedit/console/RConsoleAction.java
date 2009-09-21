package com.addictedtor.jedit.console;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import console.Output;

/**
 * An action that is sent to the console output area
 * 
 * @author romain
 */
public class RConsoleAction {
	
	/**
	 * The text of this action
	 */
	private String data; 
	
	/**
	 * Constructor
	 * @param data the text of this action
	 */
	public RConsoleAction(String data){
		this.data = data;
	}
	
	/**
	 * @return the attribute set to use to print the text of this action 
	 */
	public AttributeSet getAttributeSet(){
		MutableAttributeSet set = new SimpleAttributeSet( ) ;
		StyleConstants.setForeground(set, getForeground() ) ;
		return set; 
	}
	
	/**
	 * The foreground color to use. This is ignored if the getAttributeSet 
	 * method is overridden 
	 * @return the foreground color that is used
	 */
	public Color getForeground(){
		return Color.BLACK; 
	}
	
	/**
	 * prints the text to the console output area
	 * @param output the output area to use
	 */
	public void print( Output output ){
		output.writeAttrs( getAttributeSet(), data ) ;
	}
	
	public String toString(){
		return data; 
	}

	
}
