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

import java.awt.Color;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import com.addictedtor.jedit.tools.StringFilter;

public class PchCompletion extends Completion {
	private int value;
	private String token;
	private static final Pattern oneQuotePattern = Pattern
			.compile("^.*['\"].*$");

	private static final ImageIcon[] icons = icons();

	private static ImageIcon[] icons() {
		ImageIcon[] map = new ImageIcon[25];
		for (int i = 0; i < 25; i++) {
			map[i] = new ImageIcon(PchCompletion.class
					.getResource("/icons/pch/pch_" + (i + 1) + ".png"));
		}
		return map;
	}

	public PchCompletion(int value, String token) {
		this.value = value;
		this.token = token;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return "" + getValue();
	}

	@Override
	public String toString() {
		return getDescription();
	}

	public String getToken() {
		return token;
	}

	public Color getBackground() {
		Color color = Color.WHITE;
		return (color);
	}

	public ImageIcon getIcon() {
		if (value < 1 || value > 25) {
			return null;
		} else {
			return icons[value - 1];
		}
	}

	public static PchCompletion[] complete(String line) {
		PchCompletion[] comps;

		String token = line.substring(line.lastIndexOf("=") + 1).replaceAll(
				" ", "");
		if (oneQuotePattern.matcher(token).matches()) {
			return null;
		}

		StringFilter filter = new StringFilter(token);
		Vector<PchCompletion> vec = new Vector<PchCompletion>();
		for (int i = 1; i < 26; i++) {
			if (filter.accept("" + i)) {
				vec.add(new PchCompletion(i, token));
			}
		}
		if (vec.size() == 0) {
			return null;
		} else {
			comps = new PchCompletion[vec.size()];
			vec.toArray(comps);
			return comps;
		}

	}

}
