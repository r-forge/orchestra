package com.addictedtor.jedit.objects;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.rosuda.REngine.REXP;

import com.addictedtor.jedit.objects.icons.SimpleIconBuilder;
import com.addictedtor.jedit.objects.nodes.AttributesNode;
import com.addictedtor.jedit.objects.nodes.DebugTreeNode;
import com.addictedtor.jedit.objects.nodes.SlotNode;
import com.addictedtor.jedit.objects.nodes.UnknownNode;

@SuppressWarnings("serial")
public class REXPCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree,
			Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus){
			
			super.getTreeCellRendererComponent(tree,value,sel,
				expanded,leaf,row,hasFocus);

			REXPTreeNode node = (REXPTreeNode)value;
			if( node.getParent() != null){
				REXP object = node.getObject();
				String name = node.getName() ;
				if( node instanceof DebugTreeNode ){
					setText( ((DebugTreeNode)node).getText() ) ;
				} else {
					setText( NodeTextFactory.getText( object, name ) ) ;
				}
				
				if( node instanceof UnknownNode || node.getObject() == null){
					Icon icon = (( UnknownNode )node).getIcon() ;
					if( icon == null ){
						icon = SimpleIconBuilder.getIcon("unknown", true) ;
					} setIcon( icon ) ;
				} else if( node instanceof AttributesNode ){
					setIcon( SimpleIconBuilder.getIcon("attribute", true) ) ;
				} else if( node instanceof SlotNode ){
					setIcon( SimpleIconBuilder.getIcon("slot", true) ) ;
				} else if( node instanceof DebugTreeNode ){
					setIcon( SimpleIconBuilder.getIcon("debug", true) ) ;
				} else{
					setIcon( REXPIconFactory.getIcon(object) ) ;
				}
			}
			
			if( node instanceof DebugTreeNode ){
				/* setBackgroundNonSelectionColor( Color.red ) ; */
			}
			
			return this;
		}
}
