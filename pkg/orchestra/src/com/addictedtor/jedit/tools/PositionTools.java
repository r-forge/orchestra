/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * This file is part of the biocep editor plugin.
 *
 * 
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

package com.addictedtor.jedit.tools;

import javax.swing.text.Position;

import org.gjt.sp.jedit.textarea.JEditTextArea;

/**
 * Utilities to create positions in files
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class PositionTools {

	
	/**
	 * Creates a position using the line number and column number
	 * 
	 * @param row line number (starts at 1)
	 * @param col column number (starts at 1)
	 * @return the position
	 */
	public static Position createPosition( JEditTextArea textArea, int row, int col){
		return textArea.getBuffer().createPosition( 
				textArea.getLineStartOffset(row - 1) + col - 1 );
	}
	
	/**
	 * Creates a position that represents the end of the given line
	 * 
	 * @param row line number (starts at 1)
	 * @return the position
	 */
	public static Position createEndOfLinePosition( JEditTextArea textArea, int row ){
		return textArea.getBuffer().createPosition(
				textArea.getLineEndOffset(row - 1) );
	}
	
	/**
	 * Creates a position that represents the start of the given line
	 * @param row line number (starts at 1)
	 * @return the position
	 */
	public static Position createStartOfLinePosition( JEditTextArea textArea, int row ){
		return textArea.getBuffer().createPosition(
				textArea.getLineStartOffset(row - 1) );
	}

	
}
