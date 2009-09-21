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

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.error.ExceptionManager;

public class RoxygenCompletion extends Completion {
	private String value;
	private String title;
	private String token;

	public RoxygenCompletion(String value, String title, String token) {
		this.value = value;
		this.title = title;
		this.token = token;
	}

	public String getValue() {
		return value;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return getValue();
	}

	@Override
	public String toString() {
		return getValue();
	}

	public String getToken() {
		return token;
	}

	public static RoxygenCompletion[] complete(String currentLine) {
		RoxygenCompletion[] comps = null;

		RList result = null;
		REngine r = RPlugin.getR(); 
		int key = r.lock(); 
		String cmd = "svTools:::roxygenComplete(\"" + currentLine + "\")" ; 
		try {
			result = r.parseAndEval(cmd).asList() ;
		} catch (REXPMismatchException e1) {
			return null;
		} catch(REngineException e2){
			return null; 
		} finally {
			r.unlock(key);
		}

		try{
			String[] completions = result.at( "completions").asStrings();
			String[] title = result.at( "title").asStrings();
			String token = result.at("token").asString() ;
			int n = completions.length;
			comps = new RoxygenCompletion[n] ;
			for (int i = 0; i < n; i++) {
				comps[i] = new RoxygenCompletion(completions[i], title[i], token);
			}
		} catch(REXPMismatchException e){
			ExceptionManager.send(e);
		}
		return comps;
		
	}

	@Override
	public void setSelected( PowerEditorCompletionPopup popup ){
		popup.setDescription( title ) ;
	}
	
	@Override
	public boolean needsEditor( ){
		return true ; 
	}
	
}
