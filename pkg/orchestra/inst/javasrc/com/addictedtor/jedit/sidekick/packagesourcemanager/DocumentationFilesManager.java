package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.util.Date;

import com.addictedtor.jedit.sidekick.RdParser;

import errorlist.DefaultErrorSource;

public class DocumentationFilesManager extends Directory {

	@SuppressWarnings("unused")
	private static final RdFileFilter filesFilter = new RdFileFilter();

	private static final DocumentationDirectoryFileFilter dirFilter = new DocumentationDirectoryFileFilter();

	public DocumentationFilesManager(String root) {
		super(root);
		directory = (new File(root)).listFiles(dirFilter)[0];
	}

	public void check(DefaultErrorSource errorSource, Date lastCheck) {
		File[] docFiles = listFiles();
		for (int i = 0; i < docFiles.length; i++) {
			if (lastCheck == null
					|| lastCheck.before(new Date(docFiles[i].lastModified()))) {
				RdParser.check(docFiles[i], errorSource, lastCheck);
			}
		}
	}
}
