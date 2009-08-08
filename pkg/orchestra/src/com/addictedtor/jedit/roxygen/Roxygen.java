package com.addictedtor.jedit.roxygen ;

import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.Selection;
import org.gjt.sp.jedit.textarea.Selection.Range;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.error.ExceptionManager;

/**
 * Simple utility class that generates a Roxygen template
 * for documenting a function
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class Roxygen {

	private static void generateRoxygenTemplate_(JEditTextArea textArea) throws REngineException, REXPMismatchException {
		int row = textArea.getCaretLine();
		int col = textArea.getCaretPosition()
		- textArea.getLineStartOffset(row) + 1;
		String file = textArea.getView().getBuffer().getPath();

		String rcmd = "generateRoxygenTemplate( '" + file + "', row = " + row
		+ ", col = " + col + ", type = 'supperabbrev' ) ";

		REngine r = RPlugin.getR();
		RList list = null;  
		int key = r.lock() ;
		try{
			list = r.parseAndEval( rcmd ).asList() ;
		} finally{
			r.unlock( key ) ;
		}
		if( list == null ){
			return ;
		}
		int ok = list.at("ok").asInteger()  ;
		if (ok == 1) {
			row = list.at("row").asInteger() - 1 ;
			int pos = textArea.getLineStartOffset(row);
			Selection sel = new Range(pos, pos);
			String template = list.at("template").asString() ;
			textArea.setSelectedText(sel, template);
		}
	}

	/**
	 * Generates a Roxygen template for the function currently being edited
	 * in the buffer of the text area. 
	 * 
	 * Most of the work is actually done by the R function 
	 * generateRoxygenTemplate (svTools), which identifies which function 
	 * the caret is in, or which function is right after the caret
	 * if it is not within a function, and generate the template.
	 * 
	 * The jedit plugin SupperAbbrevs must be installed for
	 * the resulting template to make sense
	 * 
	 * @param textArea the text area
	 */
	public static void generateRoxygenTemplate( JEditTextArea textArea ) {
		try{
			generateRoxygenTemplate_(textArea) ;
		} catch (Exception e) {
			e.printStackTrace() ;
			ExceptionManager.send(e) ;
		}
	}

}
