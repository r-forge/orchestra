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

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EditPane;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.REXPLogical; 

import sidekick.SideKickCompletion;
import sidekick.SideKickParsedData;
import sidekick.SideKickParser;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.sidekick.packagesourcemanager.Description;

import errorlist.DefaultErrorSource;

/**
 * Sidekick Parser for DESCRIPTION files
 * 
 * @author Romain Francois <francoisromain@free.fr>
 * 
 */
public class DescriptionFileParser extends SideKickParser {

	/**
	 * Error source used by the parser
	 */
	public static final DefaultErrorSource error = new DefaultErrorSource(
			"DescriptionFileParser");

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            only "DESCRIPTION" makes sense
	 */
	public DescriptionFileParser(String serviceName) {
		super(serviceName);
	}

	/**
	 * Default constructor
	 */
	public DescriptionFileParser() {
		this("DESCRIPTION");
	}

	/**
	 * Parse a DESCRIPTION files looking for errors
	 * 
	 * @param buffer
	 *            buffer this is parsing
	 * @param errorSource
	 *            Where to send errors
	 * @see Description#check(String, DefaultErrorSource) which does the actual
	 *      checking
	 */
	@Override
	public SideKickParsedData parse(Buffer buffer, DefaultErrorSource errorSource) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("DESCRIPTION");
		String file = buffer.getPath();
		SideKickParsedData data = new SideKickParsedData(file);

		// PackageSourceManager packageManager = Description.packageManager(
		// file ) ;
		// if( packageManager == null) {
		Description.check(file, errorSource);
		// } else{
		// packageManager.check() ;
		// }
		data.root.add(node);
		return data;
	}

	@Override
	public boolean supportsCompletion() {
		return true;
	}

	@Override
	public boolean canCompleteAnywhere() {
		return true;
	}

	/**
	 * Attempts to complete the current line of the buffer that is in the edit
	 * pane
	 * 
	 * @param editPane
	 *            Edit Pane containing the buffer this is attempting to complete
	 * @param caret
	 *            position of the caret
	 */
	@Override
	public SideKickCompletion complete(EditPane editPane, int caret) {
		JEditTextArea ta = editPane.getTextArea();
		View view = editPane.getView();

		REngine r = RPlugin.getR();
		int lock_id = r.lock();
		RList result = null ;
		try {
			// FIXME: need to find another way (don't like using variables in .GlobalEnv)
			r.assign(".description", ta.getText(0, caret ) ) ;
			result = r.parseAndEval( "svTools::completeDescription(text = .description)" ).asList() ;
			r.parseAndEval("rm( .description )" ); 
		} catch (REXPMismatchException e) {
			// conversion problem
			return null; 
		} catch (REngineException e) {
			e.printStackTrace();
		} finally {
			r.unlock( lock_id );
		}
		
		boolean ok;
		try {
			ok = ( (REXPLogical) result.at("ok")).isTRUE()[0] ;
			DescriptionFileCompletion[] comps = null ;
			if( ok ){
				String type;
				type = result.at("type").asStrings()[0];
				if( type.equals( "fields" )){
					String[] fields = result.at("data").asList().at("field").asStrings();
					String[] completions = result.at("data").asList().at("").asStrings();
					int ncomp = fields.length; 
					String token = result.at("token").asString() ;
					
					comps = new DescriptionFileCompletion[ncomp] ;
					for( int i=0; i<ncomp; i++){
						comps[i] = new DescriptionFileCompletion(
								fields[i], completions[i], type ) ;
					}
					return new DescriptionFileSideKickCompletion( view, token, comps) ;
				}
				
			}
		} catch (REXPMismatchException e) {} 
		return null ;
	}

}
