package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.io.FileFilter;

public class Directory {

	protected File directory;
	protected static FileFilter dirFilter;
	protected static FileFilter filesFilter;

	public Directory(String root) {
		directory = (new File(root)).listFiles(dirFilter)[0];
	}

	public File[] listFiles() {
		return directory.listFiles(filesFilter);
	}

	public String[] list() {
		File[] sourceFiles = listFiles();
		String[] files = new String[sourceFiles.length];
		for (int i = 0; i < sourceFiles.length; i++) {
			files[i] = sourceFiles[i].getAbsolutePath();
		}
		return files;
	}

}
