package com.addictedtor.jedit.objects.nodes;

import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPList;

import com.addictedtor.jedit.objects.NodeBuilder;
import com.addictedtor.jedit.objects.NodeFactory;
import com.addictedtor.jedit.objects.REXPTreeNode;

public class REXPS4NodeBuilder extends NodeBuilder {

	@SuppressWarnings("unchecked")
	@Override
	public DefaultMutableTreeNode getNode(REXP object, String name) {

		DefaultMutableTreeNode node = new REXPTreeNode( object, name ) ;
		
		/* deal with attributes */
		REXPList attrs = object._attr() ;
		if( attrs != null){
			
			DefaultMutableTreeNode attr_node = new AttributesNode( attrs, "attributes" ) ;
			
			Iterator<String> keys = (Iterator<String>)attrs.asList().keySet().iterator() ;
			while( keys.hasNext() ){
				String key = keys.next() ;
				attr_node.add( NodeFactory.getNode(object.getAttribute(key), key ) ) ; 
			}
			node.add( attr_node ) ;
			
		}
		
		return node ;
	}
	
}
