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
 * Representation of a DESCRIPTION file
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class Description {

	private static final DescriptionFileFilter filter = new DescriptionFileFilter();
	
	/**
	 * The actual file
	 */
	private File DESCRIPTION;
	
	/**
	 * @param packageRoot the full name of the directory where the DESCRIPTION file is
	 */
	public Description(String packageRoot) {
		DESCRIPTION = (new File(packageRoot)).listFiles(filter)[0];
	}

	/**
	 * @param file the DESCRIPTION file
	 */
	public Description(File file) {
		DESCRIPTION = file;
	}

	/**
	 * @return the absolute name of the DESCRIPTION file
	 */
	public String getFilename() {
		return DESCRIPTION.getAbsolutePath();
	}

	/**
	 * Makes a package source manager from this file
	 * @param file 
	 * @return
	 */
	@Deprecated
	public static PackageSourceManager packageManager(String file) {
		File f = new File(file);

		String root = null;
		try {
			root = f.getParentFile().getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
		if (!new DescriptionFileFilter().accept(f) ){
			return null;
		}

		if (PackageSourceManager.elements.containsKey(root)) {
			return PackageSourceManager.elements.get(root);
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

	/**
	 * checks the file for errors
	 * @param file a file, assumed to be a DESCRIPTION file
	 * @param errorSource where to send errors
	 */
	public static void check(String file, DefaultErrorSource errorSource) {
		if (errorSource.getFileErrorCount(file) > 0) {
			errorSource.removeFileErrors(file);
		}
		
		REngine r = RPlugin.getR() ;
		String cmd = "check_description( '"+ FilenameTools.cleanFilename(file) + "') "; 
		
		int lock_id = r.lock() ;
		RList result = null; 
		try{
			r.lock() ;
			result = r.parseAndEval( cmd ).asList() ;
		} catch(REngineException ree){
		} catch( REXPMismatchException rme){
		} finally{
			r.unlock( lock_id ) ;
		}
		
		String[] messages = null;
		int []  lines = null ;
		try {
			messages = result.at("message").asStrings();
			lines = result.at("line").asIntegers();
		} catch (REXPMismatchException e) {
			return ;
		}
		 
		int n = messages.length;
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				errorSource.addError(ErrorSource.ERROR, file, lines[i], 0, 0,
						messages[i]);
			}
		}

	}

	/**
	 * Checks this DESCRIPTION file. 
	 * 
	 * A simple cache mechanism is used so that the file is not checked if it 
	 * is not needed, i.e. if it was last checked before it was last modified 
	 * 
	 * @param errorSource where to send errors
	 * @param lastCheck when was this last checked for errors
	 */
	public void check(DefaultErrorSource errorSource, Date lastCheck) {
		if (lastCheck == null
				|| lastCheck.before(new Date(DESCRIPTION.lastModified()))) {
			Description.check(getFilename(), errorSource);
		}
	}

}
