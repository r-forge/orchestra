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

package com.addictedtor.jedit.sidekick;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.CompletionPopup;
import org.gjt.sp.jedit.gui.VariableGridLayout;

import sidekick.SideKickActions;
import sidekick.SideKickCompletion;
import sidekick.SideKickCompletionPopup;
import sidekick.SideKickParser;

import com.addictedtor.jedit.tools.PrivateAccessor;

/**
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
@SuppressWarnings("serial")
public class PowerEditorCompletionPopup extends SideKickCompletionPopup {

	/**
	 * The editor pane that displays additional information about
	 * the selected completion
	 */
	private JEditorPane editor ;
	
	/**
	 * The list that displays completion items, at the moment, we steal 
	 * this from the CompletionPopup class
	 */
	private JList _list;   
	
	/**
	 * The view this belongs to
	 */
	private View view; 
	
	/**
	 * completion
	 */
	private SideKickCompletion complete ; 
	
	private SideKickParser parser; 
	/**
	 * @param view
	 * @param parser
	 * @param caret
	 * @param complete
	 */
	public PowerEditorCompletionPopup(View view, SideKickParser parser,
			int caret, SideKickCompletion complete, boolean active) {
		
		super( view, parser, caret, complete, active ) ;
		this.view = view;
		this.complete = complete;
		this.parser = parser ; 
		_list = (JList)PrivateAccessor.getPrivateField(this, "list", CompletionPopup.class ) ;
		editor = new JEditorPane( "text/plain", "" ) ;
		Dimension editorSize = new Dimension( 500, 250);
		editor.setSize( editorSize ) ;
		editor.setMaximumSize( editorSize ) ;
		editor.setMinimumSize( editorSize ) ; 
		editor.setPreferredSize( editorSize ) ; 
		
		_list.setSize( 200, _list.getHeight( ) ) ; 
		JPanel p = new JPanel( new VariableGridLayout(VariableGridLayout.FIXED_NUM_ROWS, 1)  );
		JScrollPane list_scroller = new JScrollPane(_list, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		p.add( list_scroller );
		
		JScrollPane editor_scroller = new JScrollPane(editor, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		p.add( editor_scroller );
		p.setSize( new Dimension(750, 500) ) ;
		setContentPane(p);
		pack();
	
	}
	
	/**
	 * Updates the editor so that it shows the text
	 *
	 * @param text text to display in the editor
	 */
	public void setDescription( String text ){
		editor.setBackground( new Color( 245, 245, 181 ) ); 
		editor.setContentType("text/plain") ;
		editor.setText(text ) ;
	}
	
	
	/**
	 * @param help
	 */
	public void setHelpPage(RHelpPage help) {
		editor.setContentType("text/html") ;
		URL url = help.getURL() ;
		if( ! help.getFile().exists() || url == null  ){
			setDescription( "No available help page " + help.getFile().getAbsolutePath() ) ;
		} else{
			try{
				editor.setPage( help.getURL() ) ;
				editor.setBackground( new Color( 245, 245, 181 ) );
			} catch( Exception e){
				setDescription( "No available help page " + help.getFile().getAbsolutePath() ) ;
			}
		}
		
	}
	
	/** 
	 * This is overriden so that our own updateCompletion is called
   * so that the popup is regenerated, otherwise the JEditorPane
	 * does silly things
	 */
	@Override
	public void keyTyped(KeyEvent evt) {
		char ch = evt.getKeyChar();
		if(ch == '\b' && !parser.canHandleBackspace()){
			evt.consume();
			return;
		}

		_keyTyped(ch);

		evt.consume();
	}
	
	private void _keyTyped(char ch){
		// If no completion is selected, do not pass the key to
		// handleKeystroke() method. This avoids interfering
		// between a bit intermittent user typing and automatic
		// completion (which is not selected initially).
		int selected = getSelectedIndex();
		if(selected == -1) {
			view.getTextArea().userInput(ch);
			updateCompletion(false);
		} else if(complete.handleKeystroke(selected, ch)) {
			updateCompletion(true);
		} else {
			dispose();
		}
	}
	
	private void updateCompletion(boolean active){
		dispose(); 
		SwingUtilities.invokeLater( new Runnable(){
				public void run(){
					SideKickActions.complete( view, SideKickActions.COMPLETE_COMMAND ) ; 
				}
		} ); 
		
	}

	
}
