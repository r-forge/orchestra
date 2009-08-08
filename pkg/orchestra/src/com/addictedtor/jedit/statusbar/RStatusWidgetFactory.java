/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * This file is part of the "jeditr" project
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

package com.addictedtor.jedit.statusbar;

import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.gui.statusbar.StatusWidgetFactory;
import org.gjt.sp.jedit.gui.statusbar.Widget;

import com.addictedtor.jedit.rengine.jri.RBusyMessage;
import com.addictedtor.jedit.tools.IconFactory;

/**
 * @author Romain Francois <francoisromain@free.fr>
 */
public class RStatusWidgetFactory implements StatusWidgetFactory {

	/**
	 * The icon used when R is busy
	 */
	public static final Icon BUSY  = IconFactory.icon( "statusbar/R-busy.png" );
	
	/**
	 * The icon used when R is not busy
	 */
	public static final Icon READY = IconFactory.icon( "statusbar/R-ready.png" );
	
	/**
	 * @return the R widget
	 */
	@Override
	public Widget getWidget(View view) {
		RStatusWidget widget = new RStatusWidget( view ) ;
		return widget;
	}
	
	/**
	 * Checks if the R widget is present in the list of widgets and force it otherwise
	 */
	public static void install() {
		String prop = jEdit.getProperty("view.status") ;
		StringTokenizer widgets = new StringTokenizer( prop, " " ) ;
		boolean hasRWidget = false; 
		while( widgets.hasMoreTokens() ){
			String w = widgets.nextToken() ;
			if( "R".equals( w ) ){
				hasRWidget = true; 
				break; 
			}
		}
		if( ! hasRWidget ){
			jEdit.setProperty("view.status", prop + " R") ;
			jEdit.propertiesChanged() ;
		}
		
	}
	
	/**
	 * Status widget to display R status 
	 * @author Romain Francois <francoisromain@free.fr>
	 */
	private static class RStatusWidget implements Widget, EBComponent {

		private JButton button ; 
		
		@SuppressWarnings("unused")
		private View view; 
		
		public RStatusWidget( final View view){
			button = new JButton( READY ) ;
			button.setBorder( BorderFactory.createEmptyBorder() ) ;
			this.view = view; 
			final EBComponent w = this ;
			SwingUtilities.invokeLater( new Runnable(){
				public void run(){
					EditBus.addToBus(w) ;
				}
			} ); 
		}
		
		/** 
		 * Returns the label that displays R status information
		 * 
		 * @see org.gjt.sp.jedit.gui.statusbar.Widget#getComponent()
		 */
		@Override
		public JComponent getComponent() {
			return button ; 
		}

		/**
		 * Dummy implementation.
		 */
		@Override
		public void propertiesChanged() {}

		/** 
		 * Dummy implementation
		 * @see org.gjt.sp.jedit.gui.statusbar.Widget#update()
		 */
		@Override
		public void update() {}
		
		/**
		 * Respond to RBusyMessage by setting the icon 
		 * @see RBusyMessage
		 */
		@Override
		public void handleMessage(EBMessage message) {
			if( message instanceof RBusyMessage){
				setBusy( (RBusyMessage)message ) ;
			}
		}

		private void setBusy( RBusyMessage message ){
			button.setIcon( message.getBusy() ? BUSY : READY ) ;
		}

		
	}


}
