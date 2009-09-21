package com.addictedtor.jedit.objects.nodes;

import javax.swing.Icon;

import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.objects.REXPTreeNode;
import com.addictedtor.jedit.objects.icons.SimpleIconBuilder;

/**
 * A node for a reference that could not be resolved by the
 * R engine. This might just be because the engine does not 
 * attempt to resolve all types of SXP, for example 
 * CLOSXP is not resolved 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
@SuppressWarnings("serial")
public class UnknownNode extends REXPTreeNode {

	protected REXPReference reference; 
	
	public UnknownNode(String name, REXPReference reference) {
		super(null, name);
		this.reference = reference; 
	}
	
	/**
	 * Tries to make an icon based on the expression type
	 * @return an icon or null
	 */
	public Icon getIcon(){
		int type = ( (JRIEngine)RPlugin.getR() ).getRni().rniExpType( ((Long)reference.getHandle()).longValue() ) ;
		Icon icon = null;
		if( type == org.rosuda.JRI.REXP.FUNSXP | type == org.rosuda.JRI.REXP.CLOSXP | type == org.rosuda.JRI.REXP.BUILTINSXP ){
			icon = SimpleIconBuilder.getIcon( "function", true) ;
		} else if( type == org.rosuda.JRI.REXP.ENVSXP ){
			icon = SimpleIconBuilder.getIcon( "environment", true) ;
		} else if( type == org.rosuda.JRI.REXP.PROMSXP ){
			icon = SimpleIconBuilder.getIcon( "promise", true) ;
		}
		return icon ;
	}

}
