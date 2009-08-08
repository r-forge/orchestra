package com.addictedtor.jedit.objects.nodes;

import org.rosuda.REngine.REXP;

import com.addictedtor.jedit.objects.NodeTextBuilder;
import com.addictedtor.jedit.objects.REXPTreeNode;
import com.addictedtor.jedit.objects.debug.DebugEnvironment;

/**
 * Node that represents one environment in the call stack 
 * when debugging 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
@SuppressWarnings("serial")
public class DebugTreeNode extends REXPTreeNode {

	private DebugEnvironment deb ;
	
	/**
	 * Constructor 
	 * @param object the environment reference
	 * @param name the name of the stack 
	 */
	public DebugTreeNode(REXP object, String name) {
		super(object, name);
	}

	/**
	 * Makes a DebugTreeNode by copying another node
	 * @param node tree node to copy
	 */
	public DebugTreeNode(REXPTreeNode node) {
		this( node.getObject(), node.getName() ) ;
		int childs = node.getChildCount() ;
		if( childs > 0){
			for( int i=childs-1; i>=0; i--){
				insert( (REXPTreeNode)node.getChildAt(i), 0 ) ;
			}
		}
	}
	
	public DebugEnvironment getDebugEnvironment() {
		return deb;
	}
	
	public void setDebugEnvironment(DebugEnvironment deb){
		this.deb = deb ;
	}

	public String getText() {
		String file = deb.getFile() ;
		int[] loc = deb.getSrcLocation() ;
		
		String info = null ; 
		if( file != null ){
			info = file ; 
		}
		if( loc != null){
			info = info + " (line " + loc[0] + ")" ;  
		}
		
		return NodeTextBuilder.getDefaultText(deb.getEnvironment(), deb.getName(), info ) ;
	}
}
