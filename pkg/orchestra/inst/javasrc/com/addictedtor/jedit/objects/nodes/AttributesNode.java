package com.addictedtor.jedit.objects.nodes;

import org.rosuda.REngine.REXP;

import com.addictedtor.jedit.objects.REXPTreeNode;

/**
 * Node specific to attributes 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
@SuppressWarnings("serial")
public class AttributesNode extends REXPTreeNode {

	public AttributesNode(REXP object, String name) {
		super(object, name);
		
	}

}
