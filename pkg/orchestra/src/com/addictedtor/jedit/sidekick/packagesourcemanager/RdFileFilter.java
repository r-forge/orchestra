package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.io.FileFilter;

public class RdFileFilter implements FileFilter {

	public boolean accept(File pathname) {
		boolean out = pathname.getName().matches("^.*\\.Rd$");
		return out;
	}

}
