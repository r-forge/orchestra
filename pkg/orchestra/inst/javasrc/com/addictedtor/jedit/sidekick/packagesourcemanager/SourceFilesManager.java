package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.util.Date;

import com.addictedtor.jedit.sidekick.RParser;

import errorlist.DefaultErrorSource;

public class SourceFilesManager extends Directory {

	@SuppressWarnings("unused")
	private static final SourceFileFilter filesFilter = new SourceFileFilter();

	private static final SourceDirectoryFileFilter dirFilter = new SourceDirectoryFileFilter();
	
	@SuppressWarnings("unused")
	private static Date lastCheck;

	public SourceFilesManager(String root) {
		super(root);
		lastCheck = null;
		directory = (new File(root)).listFiles(dirFilter)[0];
	}

	@SuppressWarnings("unused")
	public static PackageSourceManager packageManager(String file) {
		File f = new File(file);

		String root = null;
		try {
			root = f.getParentFile().getParentFile().getAbsolutePath();
		} catch (Exception e) {
			return null;
		}

		String basename = f.getName();
		if (!basename.matches(SourceFileFilter.REGEX)) {
			return null;
		}

		if (PackageSourceManager.elements.containsKey(root)) {
			return PackageSourceManager.elements.get(root);
		}

		File sourceDir = f.getParentFile();
		if (!sourceDir.getName().matches(SourceDirectoryFileFilter.REGEX)) {
			return null;
		}

		File pkgroot = sourceDir.getParentFile();
		File descriptionFile = new File(pkgroot.getAbsolutePath()
				+ "/DESCRIPTION");
		if (!descriptionFile.exists()) {
			return null;
		}

		File docDir = new File(pkgroot.getAbsolutePath() + "/man");
		if (!descriptionFile.exists()) {
			return null;
		}

		if (!PackageSourceManager.elements.containsKey(root)) {
			PackageSourceManager pack = new PackageSourceManager(root);
			return pack;
		}
		return null;
	}

	public void check(DefaultErrorSource errorSource, Date lastCheck) {
		File[] sourceFiles = listFiles();
		String file;
		for (int i = 0; i < sourceFiles.length; i++) {
			if (lastCheck == null
					|| lastCheck
							.before(new Date(sourceFiles[i].lastModified()))) {
				file = sourceFiles[i].getAbsolutePath();
				if (errorSource.getFileErrorCount(file) > 0) {
					errorSource.removeFileErrors(file);
				}
				RParser.checkUsage(file, errorSource);
			}
		}
	}

}
