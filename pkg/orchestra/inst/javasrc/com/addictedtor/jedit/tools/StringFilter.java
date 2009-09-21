package com.addictedtor.jedit.tools;

import java.util.regex.Pattern;

public class StringFilter {
	private Pattern pattern;
	private boolean acceptAll;

	public StringFilter(String content) {
		if (content.equals("")) {
			acceptAll = true;
		} else {
			try {
				pattern = Pattern.compile("^.*" + content + ".*$");
				acceptAll = false;
			} catch (Exception e) {
				acceptAll = true;
			}
		}
	}

	public StringFilter(boolean acceptAll) {
		this.acceptAll = acceptAll;
		pattern = null;
	}

	public boolean accept(String s) {
		return acceptAll ? true : pattern.matcher(s).matches();
	}
}
