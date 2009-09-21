package com.addictedtor.jedit.tools;

public class FilenameTools {

	// to make windows happy
	public static String cleanFilename(String f) {
		String out = f.replace("\\", "\\\\");
		return out;
	}

}
