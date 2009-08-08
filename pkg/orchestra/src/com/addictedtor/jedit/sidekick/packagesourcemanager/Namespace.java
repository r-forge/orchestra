package com.addictedtor.jedit.sidekick.packagesourcemanager;

import java.io.File;
import java.util.Date;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.tools.FilenameTools;

import errorlist.DefaultErrorSource;
import errorlist.ErrorSource;

/**
 * Representation of a NAMESPACE file
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class Namespace {

	private static final NamespaceFileFilter filter = new NamespaceFileFilter() ;
	
	private File NAMESPACE ;
	
	public Namespace(String packageRoot) {
		NAMESPACE = (new File(packageRoot)).listFiles(filter)[0];
	}

	public Namespace(File NAMESPACE) {
		this.NAMESPACE = NAMESPACE;
	}

	public String getFilename() {
		return NAMESPACE.getAbsolutePath();
	}

	/**
	 * Performs various checks on a NAMESPACE file
	 * @param file the path name of a NAMESPACE file
	 * @param errorSource where to send errors
	 */
	public static void check(String file, DefaultErrorSource errorSource) {
		if (errorSource.getFileErrorCount(file) > 0) {
			errorSource.removeFileErrors(file);
		}
		
		String cmd = "svTools:::namespaceParser( '" + FilenameTools.cleanFilename(file) + "' )" ; 
		RList result = null ;
		REngine r = RPlugin.getR() ;
		int lock_id = r.lock(); 
		try{
			result = r.parseAndEval(cmd).asList(); 
		} catch( REXPMismatchException e1){
		} catch( REngineException e2){
		} finally{
			r.unlock(lock_id); 
		}
		if( result == null){
			return ; 
		}
		String[] messages = null;
		int[] lines = null ; 
		try{
			messages = result.at( "message").asStrings() ;
			lines = result.at( "line").asIntegers() ; 
		} catch(REXPMismatchException e){
			
		}
		if( messages != null ){
			for (int i = 0; i < messages.length; i++) {
				errorSource.addError(ErrorSource.ERROR, file, lines[i], 0, 0,
						messages[i]);
			}
		}

	}

	/**
	 * Checks this NAMESPACE file. 
	 * A simple cache mechanism is used to prevent the file from being checked again if 
	 * it was not modified since the last time it was checked
	 * 
	 * @param errorSource where to send errors
	 * @param lastCheck when was this last checked
	 */
	public void check(DefaultErrorSource errorSource, Date lastCheck) {
		if (lastCheck == null
				|| lastCheck.before(new Date(NAMESPACE.lastModified()))) {
			check(getFilename(), errorSource);
		}
	}

	public static PackageSourceManager packageManager(String file) {
		File f = new File(file);

		String root = null;
		try {
			root = f.getParentFile().getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
		if (!new NamespaceFileFilter().accept(f) ){
			return null; 
		}

		if (PackageSourceManager.elements.containsKey(root)) {
			return PackageSourceManager.elements.get(root);
		}

		File description = new File(f.getParentFile().getAbsolutePath()
				+ "/DESCRIPTION");
		if (!description.exists()) {
			return null;
		}

		File sourceDir = new File(f.getParentFile().getAbsolutePath() + "/R");
		if (!sourceDir.exists()) {
			return null;
		}

		File docDir = new File(f.getParentFile().getAbsolutePath() + "/man");
		if (!docDir.exists()) {
			return null;
		}

		if (!PackageSourceManager.elements.containsKey(root)) {
			PackageSourceManager pack = new PackageSourceManager(root);
			return pack;
		}
		return null;
	}

}
