package com.addictedtor.jedit.objects;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class NodeBuilder {

	public abstract DefaultMutableTreeNode getNode(REXP object, String name) throws REXPMismatchException; 
	
}
