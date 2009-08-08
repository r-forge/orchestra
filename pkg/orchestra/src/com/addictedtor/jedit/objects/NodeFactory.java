package com.addictedtor.jedit.objects;

import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.nodes.SimpleNodeBuilder;
import com.addictedtor.jedit.tools.RServiceManager;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class NodeFactory {
	
	private static final String SERVICE = "com.addictedtor.jedit.objects.NodeBuilder" ;
	
	public static DefaultMutableTreeNode getNode( REXP object, String name ){
        System.out.println("Getting service for node: " + object.getClass() + " " + name);
		Object o = RServiceManager.getService( SERVICE, object.getClass().getSimpleName() ) ;
		if( o == null) {
			o = new SimpleNodeBuilder( );  
		}
        try {
            return ((NodeBuilder)o).getNode(object, name) ;
        } catch (REXPMismatchException e) {
            ExceptionManager.send(e) ;
            return null;
        }
    }
	
	
}
