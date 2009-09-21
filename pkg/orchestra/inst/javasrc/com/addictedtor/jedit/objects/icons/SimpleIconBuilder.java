package com.addictedtor.jedit.objects.icons;

import javax.swing.Icon;

import org.gjt.sp.jedit.GUIUtilities;
import org.rosuda.REngine.REXP;

import com.addictedtor.jedit.objects.REXPIconBuilder;
import com.addictedtor.jedit.tools.IconFactory;

/**
 * Simple implementation of the REXPIconBuilder used when the Icon is always the same 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class SimpleIconBuilder extends REXPIconBuilder {

	/**
	 * The icon used systematically 
	 */
	private Icon icon ; 
	
	/**
	 * Constructor. holds the icon
	 * @param icon the icon to always use
	 */
	public SimpleIconBuilder(Icon icon){
		this.icon = icon; 
	}
	
	/**
	 * Use {@link GUIUtilities} to look for a suitable icon
	 * @param path path to the icon
	 * @see {@link GUIUtilities#loadIcon(String)} for the meaning of path
	 */
	public SimpleIconBuilder(String path, boolean relative){
		this( getIcon(path, relative) ) ; 
	}
	
	/** 
	 * Default constructor, uses an empty icon
	 * @see IconFactory#getEmptyIcon() for the empty icon
	 */
	public SimpleIconBuilder(){
		this( IconFactory.getEmptyIcon() ); 
	}
	
	/**
	 * @return the icon supplied by the constructor
	 */
	@Override
	public Icon getIcon(REXP object) {
		return icon; 
	}

	private static String expand(String path, boolean relative ){
		if( relative ){
			path = "jeditresource:/R.jar!/icons/objects/" + path  + ".png" ;
		}
		return path ;
	}
	
	/**
	 * @param path path of the icon
	 * @param relative if true, the path is relative to the /icons/objects directory of the 
	 * jar of this plugin, otherwise it is supplied as is to {@link GUIUtilities#loadIcon(String)}
	 * @return the icon for the specified path
	 * @see GUIUtilities#loadIcon(String)
	 */
	public static Icon getIcon( String path, boolean relative){
		Icon icon = GUIUtilities.loadIcon(expand( path, relative ) ); 
		return icon; 
	}
}
