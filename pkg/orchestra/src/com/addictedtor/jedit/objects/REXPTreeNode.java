package com.addictedtor.jedit.objects;

import javax.swing.tree.DefaultMutableTreeNode;

import org.rosuda.REngine.REXP;

@SuppressWarnings("serial")
public class REXPTreeNode extends DefaultMutableTreeNode {

	private REXP object ;
	private String name; 
	
	public REXPTreeNode( REXP object, String name ){
		super( name ) ;
		this.object = object ;
		this.name = name; 
	}
	
	public REXP getObject(){
		return object; 
	}
	
	public String getName(){
		return name; 
	}
	
}
