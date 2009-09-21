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
import com.addictedtor.jedit.tools.FilenameTools;

public class RoxygenParamCompletion extends Completion {
	private String value;
	private String token;
	
	public RoxygenParamCompletion(String value, String token) {
		this.value = value;
		this.token = token;
	}

	public String getValue() {
		return value;
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

	public static RoxygenParamCompletion[] complete(String file, int line,
			String currentLine) {
		RoxygenParamCompletion[] comps = null;
		RList result = null;
		REngine r = RPlugin.getR() ;
		int key = r.lock(); 
		
		String cmd = "svTools:::roxygenParamComplete(\""
			+ FilenameTools.cleanFilename(file) + "\", " + line
			+ ", \"" + currentLine + "\")";
		
		try {
			result = r.parseAndEval(cmd).asList(); 		
		} catch (REngineException e1) {
		} catch( REXPMismatchException e2){
		} finally {
			r.unlock(key);
		}
		if( result == null){
			return null ; 
		}

		try{
			String[] completions = result.at("completions").asStrings();
			String token = result.at("token").asString();
			int n = completions.length;
			comps = new RoxygenParamCompletion[n];

			for (int i = 0; i < n; i++) {
				comps[i] = new RoxygenParamCompletion(completions[i], token);
			}
		} catch( REXPMismatchException e){}
		return comps;
	}

}
