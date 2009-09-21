package com.addictedtor.jedit.objects;

import java.awt.BorderLayout;

import com.addictedtor.jedit.R.RPlugin;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DefaultFocusComponent;
import org.rosuda.REngine.JRI.JRIEngine;

import javax.swing.*;

@SuppressWarnings("serial")
public class REnvironmentExplorer extends JPanel implements DefaultFocusComponent {
	
	private EnvironmentPanel env_panel ; 
	
	private DisplayPanel display_panel ; 
	
	private EnvironmentToolbar toolbar; 
	
	public REnvironmentExplorer(View view ){
		super( new BorderLayout() ) ;
		
		toolbar = new EnvironmentToolbar( ) ;
		add( toolbar, BorderLayout.NORTH  ) ;
		
		/* JSplitPane split = new JSplitPane( JSplitPane.VERTICAL_SPLIT ) ; */
		env_panel = new EnvironmentPanel( ((JRIEngine)RPlugin.getR()).globalEnv, ".GlobalEnv" ) ;
		/*split.setTopComponent( env_panel ) ;*/
		
		/*display_panel = new DisplayPanel( ) ;
		split.setBottomComponent( display_panel ) ;
		
		add( split, BorderLayout.CENTER ) ;
		*/
		add( env_panel, BorderLayout.CENTER) ;
	}

	
	public EnvironmentPanel getEnvironmentPanel(){
		return env_panel; 
	}
	
	public DisplayPanel getDisplayPanel(){
		return display_panel ;
	}
	
	public EnvironmentToolbar getToolbar(){
		return  toolbar; 
	}

	@Override
	public void focusOnDefaultComponent() {
		getEnvironmentPanel().getTree().requestFocus() ; 
	}
		
}
