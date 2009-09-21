package com.addictedtor.jedit.tools;

import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Factory for icons to get icons from the icons directory of the 
 * jar file of this plugin. icons are cached.
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class IconFactory {
	
	/**
	 * Cache of icons
	 */
	private static HashMap<String,Icon> map = new HashMap<String,Icon>(); 
	
	private static final Icon emptyIcon = new EmptyIcon() ;
	
	/**
	 * Gets an icon corresponding to the path relative to the /icons directory 
	 * of the jar file of this plugin
	 * 
	 * @param path path relative to the the /icons directory of R.jar
	 * @param useCache true if this has to look in the cache first
	 * @return an Icon for the specified path
	 */
	public static Icon icon( String path, boolean useCache ){
		
		if( useCache ){
			if( map.containsKey( path) ){
				return map.get(path); 
			}
		}
		
		Icon icon = new ImageIcon(IconFactory.class
				.getResource("/icons/" + path));
		if( icon == null){
			icon = emptyIcon; 
		}
		map.put( path, icon) ;
		
		return icon ; 
	}
	
	/**
	 * Gets an icon corresponding to the path relative to the /icons directory 
	 * of the jar file of this plugin. This does not look first in the cache.
	 * 
	 * @param path path relative to the the /icons directory of R.jar
	 * @see IconFactory#icon(String, boolean)
	 * 
	 * @return an Icon for the specified path
	 */
	public static Icon icon( String path){
		return icon( path, true) ;
	}
	
	/**
	 * A dummy icon used when the path does not correspond to an image file 
	 * 
	 * @author Romain Francois <francoisromain@free.fr>
	 */
	private static class EmptyIcon implements Icon{

		@Override
		public int getIconHeight() {
			return 16;
		}

		@Override
		public int getIconWidth() {
			return 16;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			
		}
		
	}

	/**
	 * Returns an empty icon of size 16x16
	 * @return
	 */
	public static Icon getEmptyIcon() {
		return emptyIcon; 
	}
	
}
