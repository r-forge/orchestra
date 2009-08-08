package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.io.FileFilter;

public class SourceFileFilter implements FileFilter {

	public static final String REGEX = "^.*\\.([rRqQ]|ssc|SSC)$";

	public boolean accept(File pathname) {
		boolean out = pathname.getName().matches(REGEX);
		return out;
	}

}
