package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter for Rd directories 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class DocumentationDirectoryFileFilter implements FileFilter {

	/**
	 * @return true if the path name is "man"
	 */
	@Override
	public boolean accept(File pathname) {
		boolean out = pathname.getName().equals( "man" );
		return out;
	}

}
