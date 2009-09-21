package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.util.Date;
import java.util.HashMap;

import org.gjt.sp.util.Log;

import errorlist.DefaultErrorSource;
import errorlist.ErrorSource;

public class PackageSourceManager {

	private final String root;
	private SourceFilesManager sourceFiles;
	private DocumentationFilesManager docFiles;
	private Namespace NAMESPACE;
	private Description DESCRIPTION;
	private DefaultErrorSource errorSource;
	private Date lastCheck;

	public static final HashMap<String, PackageSourceManager> elements = new HashMap<String, PackageSourceManager>();

	public PackageSourceManager(String root) {
		this.root = root;
		lastCheck = null;
		sourceFiles = new SourceFilesManager(root);
		docFiles = new DocumentationFilesManager(root);
		try {
			NAMESPACE = new Namespace(root);
		} catch (Exception e) {
		}

		DESCRIPTION = new Description(root);

		errorSource = new DefaultErrorSource("PackageSourceManager [" + root
				+ "]");
		ErrorSource.registerErrorSource(errorSource);

		elements.put(root, this);
	}

	public static boolean contains(String root) {
		return elements.containsKey(root);
	}

	/**
	 * Getter function for the field lastCheck
	 */
	public Date getLastCheck() {
		return lastCheck;
	}

	/**
	 * Getter function for the field NAMESPACE
	 */
	public Namespace getNAMESPACE() {
		return NAMESPACE;
	}

	/**
	 * Getter function for the field DESCRIPTION
	 */
	public Description getDESCRIPTION() {
		return DESCRIPTION;
	}

	/**
	 * Getter function for the field docFiles
	 */
	public DocumentationFilesManager getDocFiles() {
		return docFiles;
	}

	/**
	 * Getter function for the field sourceFiles
	 */
	public SourceFilesManager getSourceFiles() {
		return sourceFiles;
	}

	public void check() {
		Log.log(Log.WARNING, this, "checking namespace");
		NAMESPACE.check(errorSource, lastCheck);
		Log.log(Log.WARNING, this, "checking description");
		DESCRIPTION.check(errorSource, lastCheck);
		Log.log(Log.WARNING, this, "checking docfiles");
		docFiles.check(errorSource, lastCheck);
		Log.log(Log.WARNING, this, "cheking source files");
		sourceFiles.check(errorSource, lastCheck);
		Log.log(Log.WARNING, this, "done");
		lastCheck = new Date();
	}

	/**
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

}
