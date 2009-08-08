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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class RHelpPage {

	private File file ;
	private int type ;
	
	public static final int HTML = 0 ;
	public static final int HELP = 1 ;
	
	private static final Pattern htmlPattern = Pattern.compile( "[.]html$", Pattern.CASE_INSENSITIVE ) ;
	
	public RHelpPage( String txt ){
		type = htmlPattern.matcher(txt).find() ? HTML : HELP ;
		try{
			file = new File( txt ) ;
		} catch(Exception e){
			file = null ;
		}
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @return the url
	 */
	public File getFile() {
		return file ;
	}
	
	/**
	 * get the url of the help file
	 * 
	 * @return the help file (as an URL)
	 */
	public URL getURL(){
		URL url = null ;
		try {
			url = new URL( "file", "localhost", file.getAbsolutePath() ) ;
		} catch( MalformedURLException e){
			url = null ;
		}
		return url; 
	}
	
}
