package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter for DESCRIPTION files
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class DescriptionFileFilter implements FileFilter {

	/**
	 * @return true if the name of the file is "DESCRIPTION"
	 */
	@Override
	public boolean accept(File pathname) {
		boolean out = pathname.getName().equals( "DESCRIPTION" );
		return out;
	}

}
