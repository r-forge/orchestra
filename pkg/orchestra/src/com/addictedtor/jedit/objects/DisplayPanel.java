package com.addictedtor.jedit.objects;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;

import com.addictedtor.jedit.editbus.AddToBusRunnable;

@SuppressWarnings("serial")
public class DisplayPanel extends JPanel implements EBComponent {

	@SuppressWarnings("unused")
	private JScrollPane scroller ; 

	public DisplayPanel(){
		super( ) ;
		scroller = new JScrollPane( new JLabel( "" ) ) ;
		SwingUtilities.invokeLater( new AddToBusRunnable( this ) );
	}

	public void setComponent(Component component) {
		scroller = new JScrollPane( component == null ? new JLabel("") : component )  ;
		revalidate(); 
		repaint() ; 
	}

	@Override
	public void handleMessage(EBMessage message) {
		if( message instanceof DisplayMessage ){
			final Component component = ( (DisplayMessage)message).getComponent() ; 
			SwingUtilities.invokeLater( new Runnable(){
				public void run(){
					setComponent( component ) ;
				}
			} );
		}
	}

}
