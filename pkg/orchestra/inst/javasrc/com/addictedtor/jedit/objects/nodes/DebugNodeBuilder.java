package com.addictedtor.jedit.objects.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import org.rosuda.REngine.REXP;

import com.addictedtor.jedit.objects.REXPTreeNode;


public class DebugNodeBuilder extends REXPReferenceNodeBuilder {

	public DebugNodeBuilder(){
		super( true ) ;
	}
	
	@Override
	public DefaultMutableTreeNode getNode(REXP object, String name) {
		DefaultMutableTreeNode node = super.getNode(object, name) ;
		return new DebugTreeNode( (REXPTreeNode) node ) ; 
	}
	
}
