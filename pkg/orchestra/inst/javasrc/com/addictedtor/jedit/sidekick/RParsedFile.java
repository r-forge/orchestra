/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * This file is part of the biocep editor plugin.
 *
 * The biocep editor plugin is free software: 
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The biocep editor plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the biocep editor plugin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.addictedtor.jedit.sidekick;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.tools.FilenameTools;
import com.addictedtor.jedit.tools.PositionTools;

import errorlist.DefaultErrorSource;
import errorlist.ErrorSource;

/**
 * Representation of results from parse to be displayed on the sidekick browser
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class RParsedFile {

	/**
	 * Nodes of the sidekick tree
	 */
	private DefaultMutableTreeNode[] nodes;

	/**
	 * R assets displayed in the nodes
	 */
	private RAsset[] assets;

	/**
	 * The text area associated with the parsed buffer
	 */
	private JEditTextArea textArea;

	/**
	 * nodes identifiers
	 */
	private int[] id;

	/**
	 * identifiers for parent of nodes
	 */
	private int[] parent;

	public RParsedFile(String file, DefaultMutableTreeNode root, DefaultErrorSource errorSource) {
		nodes = null;
		assets = null;
		textArea = jEdit.getActiveView().getTextArea();

		String encoding = textArea.getBuffer().getStringProperty(
				JEditBuffer.ENCODING);
		String cmd = "try( sidekick( '" + FilenameTools.cleanFilename(file) + "', encoding = '" + encoding + "' ) , silent = TRUE )";

		REngine r = RPlugin.getR();
		int lock_id = r.lock(); 
		try {
			REXP result = r.parseAndEval(cmd);
			if( result.isString() ){
				// error message
				// String error = result.asString(); 
			} else{
				RList res = result.asList() ;
				String type = res.at("type").asString(); 
				RList data = res.at("data").asList(); 
				if( type.equals( "error" )){
					sendError( errorSource, data, file ) ;
				} else {
					buildParseTree( root, data ) ;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally {
			r.unlock( lock_id ) ;
		}

	}


	/**
	 * @param root
	 * @param result
	 */
	private void buildParseTree(DefaultMutableTreeNode root, RList result) {
		String[] descriptions;
		try {
			descriptions = result.at("description").asStrings();
		} catch (REXPMismatchException e1) {
			return ; 
		}
		int n = descriptions.length;

		nodes = new DefaultMutableTreeNode[n];
		assets = new RAsset[n];
		if (n > 0) {
			nodes = new DefaultMutableTreeNode[n];

			try{
				int[] rowstart = result.at("srcref1").asIntegers() ;
				int[] colstart = result.at("srcref2").asIntegers() ;
				int[] rowend   = result.at("srcref3").asIntegers() ;
				int[] colend   = result.at("srcref4").asIntegers() ;
				String[] modes = result.at("mode").asStrings() ;
				id             = result.at("id").asIntegers();
				parent         = result.at("parent").asIntegers();

				for (int i = 0; i < n; i++) {
					assets[i] = new RAsset(descriptions[i], modes[i]);
					assets[i].setStart( PositionTools.createPosition( textArea, rowstart[i], colstart[i] ) ) ;
					assets[i].setEnd( PositionTools.createPosition( textArea, rowend[i], colend[i] + 1 ) ) ; 
					nodes[i] = new DefaultMutableTreeNode(assets[i]);
				}
				buildTree(root, 0);

			} catch( REXPMismatchException e){
				return; 
			}

		}
	}

	/**
	 * @param root
	 * @param item
	 */
	private void sendError(DefaultErrorSource error, RList result, String file) {
		String [] message;
		int[] line ; 
		try{
			message = result.at("message").asStrings();
			line    = result.at("line").asIntegers() ;
		} catch(REXPMismatchException e){
			return ; 
		}
		int n = message.length;
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				error.addError(
						ErrorSource.ERROR, file, line[i] - 1, 0, 0,
						message[i]);
			}
		}
	}


	private void buildTree(DefaultMutableTreeNode node, int p) {
		int childId;
		int[] childIds = getChilds(p);
		if (childIds != null) {
			for (int i = 0; i < childIds.length; i++) {
				childId = childIds[i];
				buildTree(nodes[childId - 1], childId);
				if (!((RAsset) nodes[childId - 1].getUserObject()).getName()
						.equals("{")) {
					node.add(nodes[childId - 1]);
				}

			}
		}
	}

	private int[] getChilds(int p) {

		Vector<Integer> childs = new Vector<Integer>();
		if (p >= id.length) {
			return null;
		}

		for (int i = p; i < id.length; i++) {
			if (parent[i] == p) {
				childs.add(new Integer(id[i]));
			}
		}
		if (childs.isEmpty()) {
			return null;
		}
		int[] out = new int[childs.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = childs.get(i).intValue();
		}
		return out;
	}

}
