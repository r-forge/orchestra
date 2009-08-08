package com.addictedtor.jedit.objects.debug;

import java.awt.Color;
import java.awt.Graphics2D;

import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaExtension;

/**
 * Text area extension that paints the information about the 
 * location of the expression that is currently debugged
 *  
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class DebugTextAreaExtension extends TextAreaExtension {

	private JEditTextArea textArea ; 

	private Color color ; 
	private Graphics2D gfx ; 

	public DebugTextAreaExtension( JEditTextArea textArea) {
		this.textArea = textArea ;
		color = Color.ORANGE ;
	}

	@Override
	public void paintValidLine(Graphics2D gfx, int screenLine,
			int physicalLine, int start, int end, int y){

		JEditBuffer buffer = textArea.getBuffer( ) ;
		if( buffer.isLoading() ){
			return ; 
		}
		if( ! buffer.getMode().getName().equals("R" ) ){
			return ;
		}

		Object o = buffer.getProperty( "R-debuginfo" ) ;
		if( o == null | ! (o instanceof DebugEnvironment) ){ return;  }
		DebugEnvironment deb = (DebugEnvironment)o; 
		String file = deb.getFile();
		if( file == null ){ return ; }

		int[] loc = deb.getSrcLocation() ;
		if( loc == null ){ return ;  }

		int first_line = loc[0] - 1; 
		int first_col  = loc[1] - 1; 

		int last_line = loc[2] - 1; 
		int last_col  = loc[3] - 1; 

		if( last_line >= buffer.getLineCount() ){ return ;}

		// the physical lines needs to be between the first line and the last line
		if( first_line < physicalLine ){ return ; }
		if( last_line > physicalLine ){ return ; }

		int start_offset = textArea.getLineStartOffset(first_line) + first_col ;
		int end_offset = textArea.getLineStartOffset(last_line) + last_col ;

		int height = textArea.getPainter().getFontMetrics().getHeight() ; 
		this.gfx = gfx; 

		if( first_line == last_line ){
			// is the expression only one one line
			decorate( textArea.offsetToXY( start_offset ).x, textArea.offsetToXY( end_offset ).x, y, height ) ;

		} else if( physicalLine == first_line) {
			// this is the first line - need to start at the start offset and go to the end of the line
			decorate( textArea.offsetToXY( start_offset ).x, textArea.getWidth(), y, height ) ;

		} else if( physicalLine == last_line ){
			// this is the last line - need to start from 0 and end at the end_offset
			decorate( 0, textArea.offsetToXY( end_offset ).x, y, height ) ;

		} else{
			// this is between , decorate the while line
			decorate( 0, textArea.getWidth(), y, height ) ;
		}
	}

	private void decorate(int x1, int x2, int y, int height ) {
		Color before = gfx.getColor(); 
		gfx.setColor( color ) ;
		gfx.fillRect( x1 , y, x2 - x1, height ) ;
		gfx.setColor( before ) ;
	}          



}
