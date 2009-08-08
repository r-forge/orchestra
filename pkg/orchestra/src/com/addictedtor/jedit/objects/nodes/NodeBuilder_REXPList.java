package com.addictedtor.jedit.objects.nodes;

import com.addictedtor.jedit.objects.NodeBuilder;
import com.addictedtor.jedit.objects.NodeFactory;
import com.addictedtor.jedit.objects.REXPTreeNode;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;

import javax.swing.tree.DefaultMutableTreeNode;

public class NodeBuilder_REXPList extends NodeBuilder {

    public DefaultMutableTreeNode getNode(REXP object, String name) throws REXPMismatchException{
		DefaultMutableTreeNode node = new REXPTreeNode( object, name ) ;

        RList list = object.asList();
        for (int i=0; i<list.size(); i++) {
            node.add(NodeFactory.getNode(list.at(i), list.keyAt(i)));
        }

        return node;
    }
}
