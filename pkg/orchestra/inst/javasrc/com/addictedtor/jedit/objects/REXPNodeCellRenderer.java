package com.addictedtor.jedit.objects;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.rosuda.REngine.REXP;

@SuppressWarnings("serial")
public class REXPNodeCellRenderer extends DefaultTreeCellRenderer {

	public REXPNodeCellRenderer() {}

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        if( value instanceof REXP){
        	setIcon( REXPIconFactory.getIcon((REXP)value) ) ;
        }
        return this;
    }

}
