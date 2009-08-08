package com.addictedtor.jedit.actions;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DockableWindowManager;
import org.gjt.sp.jedit.textarea.JEditTextArea;

import sidekick.Asset;
import sidekick.SideKickParsedData;

import com.addictedtor.jedit.console.RConsole;

import console.Console;

/**
 * Various actions 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class REditAction {

	private static final String CONSOLE = "console" ;
	
	/**
	 * Sends the text to the R console
	 * 
	 * @param textArea the text area
	 * @see #getSelectedTextOrCurrentLine(JEditTextArea) 
	 */
	public static void send(String text, JEditTextArea textArea){
		if( text == null || text.equals( "" ) ) {
			return ;
		}
		View view = textArea.getView() ;
		RConsole rconsole = getRConsole(view) ;
		rconsole.getSync().addInput( text, true ) ;
	}
	
	/**
	 * Activates the console and the R shell and return the RConsole object
	 * 
	 * @param view the view
	 * @return the instance of the R shell (RConsole)
	 */
	private static RConsole getRConsole(View view){
		DockableWindowManager wm = view.getDockableWindowManager() ;
		wm.addDockableWindow( CONSOLE ) ;
		Console console = (Console)wm.getDockableWindow( CONSOLE ) ;
		console.setShell( "R" ) ;
		return (RConsole)console.getShell() ;
	}
	
	/**
	 * @param textArea the current text area 
	 * @return the currently selected text, or the text of the current line
	 * if there is no selection
	 */
	public static String getSelectedTextOrCurrentLine( JEditTextArea textArea ){
		String text = textArea.getSelectedText() ; 
		if( text == null ) {
			text = textArea.getLineText( textArea.getCaretLine() ) ;
		}
		return text ; 
	}
	
	/**
	 * Sends the current selection or the current line to R
	 * 
	 * @param textArea the text area
	 * @see #getSelectedTextOrCurrentLine(JEditTextArea) 
	 */
	public static void sendSelection(JEditTextArea textArea ){
		send( getSelectedTextOrCurrentLine(textArea ), textArea ) ; 	
	}
	
	/**
	 * Sends the current current line to R
	 * 
	 * @param textArea the text area
	 * @see #getSelectedTextOrCurrentLine(JEditTextArea) 
	 */
	public static void sendCurrentLine(JEditTextArea textArea, boolean move ){
		String line = textArea.getLineText( textArea.getCaretLine() ); 
		send( line , textArea ) ;
		if( move ){
			focusOnNextLine( textArea );
		}
	}
	
	/**
	 * Focus on the line below the current line
	 *
	 */
	private static void focusOnNextLine(JEditTextArea textArea){
		int line = textArea.getCaretLine(); 
		int offset = textArea.getLineStartOffset(line+1) ;
		textArea.scrollTo( offset, false ); 
		textArea.setCaretPosition( offset );
		textArea.requestFocus(); 
	}
	
	
	private static String getTopLevelExpression( final JEditTextArea textArea, boolean move ){
		int pos = textArea.getCaretPosition() ;
		SideKickParsedData data = SideKickParsedData.getParsedData(textArea.getView());
		if( data == null){
			return null ;
		}
		final TreePath path = data.getTreePathForPosition( pos ) ;
		if( path == null || path.getPathCount() <2 ){
			return null; 
		}
		Asset asset = (Asset)( (DefaultMutableTreeNode)path.getPathComponent(1) ).getUserObject();
		if( asset == null){
			return null; 
		}
		int start = asset.getStart().getOffset() ;
		int end   = asset.getEnd().getOffset() ;
		String text = textArea.getText(start, end - start );
		if( move ){
			SwingUtilities.invokeLater( new Runnable(){
				public void run(){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getPathComponent(1) ;
					TreeNode parent = node.getParent() ;
					int index = parent.getIndex(node) ;
					if( parent.getChildCount() > index ){
						DefaultMutableTreeNode next = (DefaultMutableTreeNode) parent.getChildAt( index + 1 ) ;
						Asset nextasset = (Asset)next.getUserObject() ;
						int start = nextasset.getStart().getOffset() ;
						textArea.setCaretPosition( start ) ;
					} else{
						textArea.setCaretPosition( textArea.getText().length() ) ;
					}
					textArea.requestFocus() ;
				}
			}) ;
		}
		return text;
	}
	
	public static void sendCurrentTopLevelExpression(JEditTextArea textArea, boolean move ){
		String text = getTopLevelExpression(textArea, move) ;
		send( text , textArea ) ;
	}
	
	
}
