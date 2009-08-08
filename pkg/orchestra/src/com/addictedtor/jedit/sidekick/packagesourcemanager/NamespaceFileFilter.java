package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.io.FileFilter;

/**
 * File filter for NAMESPACE files
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class NamespaceFileFilter implements FileFilter {

	/**
	 * @return true if the name of the file is "NAMESPACE"
	 */
	public boolean accept(File pathname) {
		boolean out = pathname.getName().equals( "NAMESPACE");
		return out;
	}

}
