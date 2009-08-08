/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * This file is part of the biocep editor plugin.
 *
 * The biocep editor plugin is free software: 
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The biocep editor plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the biocep editor plugin. If not, see <http://www.gnu.org/licenses/>.
 */

package com.addictedtor.jedit.sidekick;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import com.addictedtor.jedit.tools.StringFilter;

/**
 * Completion related to the lty argument (line type).
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public class LtyCompletion extends Completion {
	
	/**
	 * Value of the completion
	 */
	private String value;
	
	/**
	 * Token
	 */
	private String token;
	
	/**
	 * Pattern that indicates that we are within the quotes
	 */
	private static final Pattern oneQuotePattern = Pattern
			.compile("^.*['\"].*$");
	
	/**
	 * Pattern that indicates that we are outside the quotes
	 */
	private static final Pattern twoQuotePattern = Pattern
			.compile("^.*(['\"]).*\\1.*$");

	/**
	 * The map of the icons
	 */
	private static final HashMap<String, ImageIcon> icons;
	static {
		icons = new HashMap<String, ImageIcon>();
		icons.put("blank", new ImageIcon(LtyCompletion.class
				.getResource("/icons/lty/lty_blank.png")));
		icons.put("dashed", new ImageIcon(LtyCompletion.class
				.getResource("/icons/lty/lty_dashed.png")));
		icons.put("dotdash", new ImageIcon(LtyCompletion.class
				.getResource("/icons/lty/lty_dotdash.png")));
		icons.put("dotted", new ImageIcon(LtyCompletion.class
				.getResource("/icons/lty/lty_dotted.png")));
		icons.put("longdash", new ImageIcon(LtyCompletion.class
				.getResource("/icons/lty/lty_longdash.png")));
		icons.put("solid", new ImageIcon(LtyCompletion.class
				.getResource("/icons/lty/lty_solid.png")));
		icons.put("twodash", new ImageIcon(LtyCompletion.class
				.getResource("/icons/lty/lty_twodash.png")));
	}

	/**
	 * Constructor 
	 * @param lty line type 
	 * @param token token
	 */
	public LtyCompletion(String lty, String token) {
		this.value = lty;
		this.token = token;
	}

	/**
	 * Returns the value
	 * @return the value of the completion
	 */
	public String getValue() {
		return value;
	}

	/**
	 * The description of the value, what will be used to replace the
	 * token if the completion is chosen
	 * 
	 * @return the description of the replacement
	 */
	public String getDescription() {
		return '"' + getValue() + '"';
	}

	@Override
	public String toString() {
		return getDescription();
	}

	/**
	 * Returns the token
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Returns the icon associated with this value
	 * @return
	 */
	public ImageIcon getIcon() {
		if (icons.containsKey(value))
			return (icons.get(value));
		return null;
	}

	/**
	 * Completes a line of code
	 * @param line line of R code
	 * @return an array of possible lty completions
	 */
	public static LtyCompletion[] complete(String line) {
		LtyCompletion[] comps;
		String token = line.replaceFirst("^.*= *", "");
		if (twoQuotePattern.matcher(token).matches()) {
			return null;
		}
		StringFilter filter;
		if (oneQuotePattern.matcher(token).matches()) {
			filter = new StringFilter(token.replaceAll("['\" ]", ""));
		} else {
			filter = new StringFilter(true);
		}

		Vector<LtyCompletion> vec = new Vector<LtyCompletion>();
		Object[] keys = icons.keySet().toArray();
		String lty;
		for (int i = 0; i < keys.length; i++) {
			lty = (String) keys[i];
			if (filter.accept(lty)) {
				vec.add(new LtyCompletion(lty, token));
			}
		}
		if (vec.size() == 0) {
			return null;
		} else {
			comps = new LtyCompletion[vec.size()];
			vec.toArray(comps);
			return comps;
		}

	}

}
