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

import java.io.File;

import javax.swing.JOptionPane;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EditPane;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import sidekick.SideKickCompletion;
import sidekick.SideKickCompletionPopup;
import sidekick.SideKickParsedData;
import sidekick.SideKickParser;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.tools.FilenameTools;

import errorlist.DefaultErrorSource;
import errorlist.ErrorSource;

import org.gjt.sp.util.Log ;

/**
 * Parser for R files
 * 
 * @author Romain Francois <francoisromain@free.fr>
 * 
 */
public class RParser extends SideKickParser {

	@SuppressWarnings("unused")
	private static final DefaultErrorSource errorSourceDir = getErrorSource();

	private static String directory = "";

	public RParser(String serviceName) {
		super(serviceName);
		directory = "";
	}

	public RParser() {
		this("R");
	}

	private static DefaultErrorSource getErrorSource() {
		DefaultErrorSource error = new DefaultErrorSource("RParser.DIR");
		ErrorSource.registerErrorSource(error);
		return error;
	}

	@Override
	public SideKickParsedData parse(Buffer buffer,
			DefaultErrorSource errorSource) {
		String file = buffer.getPath();
		SideKickParsedData data = new SideKickParsedData(file);

		String filepath = FilenameTools.cleanFilename(file);
		if (!(new File(filepath)).exists()) {
			return data;
		}
		checkUsage(buffer, errorSource);
		new RParsedFile(file, data.root, errorSource );
		return data;
	}

	public static void checkUsage(Buffer buffer, DefaultErrorSource errorSource) {
		checkUsage(buffer.getPath(), errorSource, buffer
				.getStringProperty(JEditBuffer.ENCODING));
	}

	public static void checkUsage(String file, DefaultErrorSource errorSource) {
		checkUsage(file, errorSource, "utf-8");
	}

	private static void checkUsage(String file, DefaultErrorSource errorSource,
			String encoding) {
		RList result = null;
		String filepath = FilenameTools.cleanFilename(file);
		if (!(new File(filepath)).exists())
			return ;
		String cmd = "checkUsageFile('" + filepath + "', encoding = '"
				+ encoding + "') ";
		
		REngine r = RPlugin.getR();
		int lock_id = r.lock() ; 
		try {
			result = r.parseAndEval(cmd).asList();
		} catch (REXPMismatchException e) {
			return;
		} catch (REngineException e) {
			e.printStackTrace();
		} finally {
			r.unlock(lock_id); 
		}
	
		try{
			int[] lines = result.at("line").asIntegers() ;
			String[] types = result.at("type").asStrings() ;
			String[] messages = result.at("message").asStrings() ;

			int n = lines.length;

			if (n > 0) {
				for (int i = 0; i < n; i++) {
					errorSource.addError(
							types[i].equals("warning") ? ErrorSource.WARNING
									: ErrorSource.ERROR, file, lines[i] - 1, 0, 0,
							messages[i]);
				}
			}
		} catch (REXPMismatchException e) {}
		
	}

	public String getDirectory() {
		return directory;
	}

	@Override
	public boolean supportsCompletion() {
		return true;
	}

	@Override
	public boolean canCompleteAnywhere() {
		return true;
	}

	@Override
	public SideKickCompletion complete(EditPane editPane, int caret) {
		JEditTextArea ta = editPane.getTextArea();
		int start = ta.getLineStartOffset(ta.getCaretLine());

		String currentLine = ta.getText(start, caret - start);
		Log.log( Log.ERROR, this, currentLine ) ;
		View view = editPane.getView();
		String token;

		if (currentLine.matches("^.*pch(\\.[^ ]*)? *= *[0-9]*$")) {
			PchCompletion[] pchcomp = PchCompletion.complete(currentLine);
			if (pchcomp == null)
				return null;
			token = pchcomp[0].getToken();
			return (new PchSideKickCompletion(view, token, pchcomp));
		} else if (currentLine
				.matches("^.*(col|bg|border)(\\.[^ ]*)? *= *[^,(){}]*$")) {
			ColCompletion[] colcomp = ColCompletion.complete(currentLine);
			if (colcomp == null)
				return null;
			token = colcomp[0].getToken();
			return (new ColSideKickCompletion(view, token, colcomp));
		} else if (currentLine
				.matches("^.*(lty) *= *([0-9]*|\"'['\"a-zA-Z ]*)$")) {
			LtyCompletion[] ltycomp = LtyCompletion.complete(currentLine);
			if (ltycomp == null)
				return null;
			token = ltycomp[0].getToken();
			return (new LtySideKickCompletion(view, token, ltycomp));
		} else if (currentLine.startsWith("#' @param ")) {
			RoxygenParamCompletion[] rxcomp = RoxygenParamCompletion.complete(
					editPane.getBuffer().getPath(), ta.getCaretLine(),
					currentLine);
			if (rxcomp == null)
				return null;
			token = rxcomp[0].getToken();
			return (new RoxygenParamSideKickCompletion(view, token, rxcomp));
		} else if (currentLine.startsWith("#' @")) {
			RoxygenCompletion[] rxcomp = RoxygenCompletion
					.complete(currentLine);
			if (rxcomp == null)
				return null;
			token = rxcomp[0].getToken();
			return (new RoxygenSideKickCompletion(view, token, rxcomp));
		} else if (currentLine.matches("^.*#.*")) {
			return null;
		} else {
			RCompletion[] rcomp = RCompletion.complete(currentLine);
			if (rcomp == null)
				return null;
			token = rcomp[0].getToken();
			return (new RSideKickCompletion(view, token, rcomp, this));
		}
	}

	PowerEditorCompletionPopup popup = null ;
	public SideKickCompletionPopup getCompletionPopup(View view, int caretPosition, SideKickCompletion complete, boolean active){
		popup= new PowerEditorCompletionPopup(view, this, caretPosition, complete, active) ;
		return popup ;
	}
	
	public PowerEditorCompletionPopup getCurrentCompletionPopup( ){
		return popup ;
	}
	
}
