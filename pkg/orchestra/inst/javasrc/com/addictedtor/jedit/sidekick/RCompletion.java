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

import java.awt.Color;

import javax.swing.ImageIcon;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.R.RPlugin;

public class RCompletion extends Completion {
	private String completion;
	private String info;
	private String type;
	private String position;
	private String token;

	public static final ImageIcon ARGUMENT_ICON = new ImageIcon(
			RCompletion.class.getResource("/icons/sidekick/argument.png"));

	public static final ImageIcon PACKAGE_ICON = new ImageIcon(
			RCompletion.class.getResource("/icons/sidekick/package.png"));

	public static final ImageIcon FUNCTION_ICON = new ImageIcon(
			RCompletion.class.getResource("/icons/sidekick/function.png"));

	public RCompletion(String completion, String position, String info,
			String type, String token) {
		this.completion = completion;
		this.position = position;
		this.info = info;
		this.type = type;
		this.token = token;

	}

	public String getCompletion() {
		return completion;
	}

	public String getInfo() {
		return info;
	}

	public String getType() {
		return type;
	}

	public String getPosition() {
		return position;
	}

	public String getDescription() {
		return getCompletion();
	}

	@Override
	public String toString() {
		return getCompletion();
	}

	public String getToken() {
		return token;
	}

	public Color getBackground() {
		Color color = Color.WHITE;
		if (type.equals("argument")) {
			color = Color.WHITE;
		} else if (type.equals("function")) {
			color = new Color(241, 249, 255);
		} else if (type.equals("package")) {
			color = new Color(255, 253, 220);
		}
		return (color);
	}

	public ImageIcon getImageIcon() {
		ImageIcon icon = null;
		if (type.equals("argument")) {
			icon = ARGUMENT_ICON;
		} else if (type.equals("function")) {
			icon = FUNCTION_ICON;
		} else if (type.equals("package")) {
			icon = PACKAGE_ICON;
		}
		return icon;

	}

	public RHelpPage getHelpPage(){
		if( position == null || position.equals("") ){
			return null ; 
		}	
		RHelpPage page = null ;
		REngine r = RPlugin.getR() ; 
		int lock_id = r.lock(); 

		try{
			String cmd = "help( '" + completion + "' , package = '"+ position +"', help_type='html' )[1] " ;
			page = new RHelpPage( r.parseAndEval(cmd).asString() ) ;
		} catch( Exception e){

		} finally{
			r.unlock(lock_id) ;
		}
		return page ;
	}

	public String getFunction(){
		StringBuilder b = new StringBuilder("");
		REngine r = RPlugin.getR() ; 
		int lock_id = r.lock(); 
		try{
			String cmd = "deparse( " + completion + ") " ;
			String[] lines = r.parseAndEval(cmd).asStrings(); 
			if( lines != null ){
				for( String line: lines){
					b.append( line + "\n" ); 
				}
			}
		} catch( REXPMismatchException e1){
		} catch( REngineException e2){
		} finally{
			r.unlock(lock_id) ;
		}
		return b.toString(); 
	}

	public static RCompletion[] complete(String currentLine) {
		RList result = null;
		REngine r = RPlugin.getR() ;

		int lock_id = r.lock(); 
		try {
			result = r.parseAndEval( "CompletePlusWrap('" + currentLine + "') ").asList() ;
		} catch(REXPMismatchException e1) {
		} catch(REngineException e2){
		} finally {
			r.unlock(lock_id);
		}
		if( result == null){
			return null; 
		}
		
		RCompletion[] comps = null;
		try{
			String token = result.at("token").asString() ;
			String[] data = result.at( "data" ).asStrings(); 
			if ( data.length > 0) {
				int n = data.length / 4 ;

				comps = new RCompletion[n];
				for (int i = 0; i < n; i++) {
					comps[i] = new RCompletion(data[i], data[i + n],
							data[i + 2 * n], data[i + 3 * n], token);
				}
			}
		} catch( REXPMismatchException e1){}

		return comps;
	}

	@Override
	public void setSelected( PowerEditorCompletionPopup popup ){
		if( type.equals("function") ){
			RHelpPage help = getHelpPage() ;
			if( help == null ){
				popup.setDescription( getFunction() ); 
			} else {
				popup.setHelpPage( help ) ;
			}
		} else {
			popup.setDescription( getInfo() ) ;
		}
	}
	
	@Override
	public boolean needsEditor( ){
		return true ; 
	}
	
	
}
