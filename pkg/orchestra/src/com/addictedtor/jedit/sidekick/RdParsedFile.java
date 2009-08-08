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

import javax.swing.tree.DefaultMutableTreeNode;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.tools.FilenameTools;

public class RdParsedFile {

	private DefaultMutableTreeNode[] nodes;
	private RdAsset[] assets;
	private String file; 

	public RdParsedFile(String file, DefaultMutableTreeNode root) {
		
		this.file = FilenameTools.cleanFilename(file) ;
		nodes = null;
		assets = null;

		/* init(root); */ 

	}
	
	// FIXME: rdparse seems to be broken
	@SuppressWarnings("unused")
	private void init( DefaultMutableTreeNode root ){
		RList result = null;
		REngine r = RPlugin.getR(); 
		int lock_id = r.lock() ;
		String cmd = "svTools:::rdparse( '"	+ file + "' ) " ;
		try {
			result = r.parseAndEval(cmd).asList();
		} catch(REXPMismatchException e1) {
			return ; 
		} catch(REngineException e2){
			return; 
		} finally {
			r.unlock(lock_id);
		}
		

		try{
			String[] names = result.at("names").asStrings(); 
			int n = names.length;
			nodes = new DefaultMutableTreeNode[n];
			assets = new RdAsset[n];
			int[] start = result.at("offset.start").asIntegers() ;
			int[] end   = result.at("offset.end").asIntegers() ;
			
			for (int i = 0; i < n; i++) {
				assets[i] = new RdAsset(names[i]);
				assets[i].setStart((new RdPosition(start[i])));
				assets[i].setEnd((new RdPosition(end[i])));
				nodes[i] = new DefaultMutableTreeNode(assets[i]);
				root.add(nodes[i]);
			}

		} catch( REXPMismatchException e){}
		
	}

}
