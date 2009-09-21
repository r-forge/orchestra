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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import sidekick.Asset;

public class RAsset extends Asset {
	private String mode;

	public static final ImageIcon FUNCTION_ICON = new ImageIcon(RAsset.class
			.getResource("/icons/sidekick-tree/function.png"));
	public static final ImageIcon IFTRUE_ICON = new ImageIcon(RAsset.class
			.getResource("/icons/sidekick-tree/if-true.png"));
	public static final ImageIcon IFFALSE_ICON = new ImageIcon(RAsset.class
			.getResource("/icons/sidekick-tree/if-false.png"));
	public static final ImageIcon NAME_ICON = new ImageIcon(RAsset.class
			.getResource("/icons/sidekick-tree/name.png"));
	public static final ImageIcon ERROR_ICON = new ImageIcon(RAsset.class
			.getResource("/icons/sidekick-tree/error.png"));
	private static HashMap<String,ImageIcon> icons ;
	static{
		icons = new HashMap<String,ImageIcon>();
		icons.put( "function", FUNCTION_ICON) ; 
		icons.put( "if:TRUE", IFTRUE_ICON) ; 
		icons.put( "if:FALSE", IFFALSE_ICON) ; 
		icons.put( "name", NAME_ICON) ; 
		icons.put( "error", ERROR_ICON) ; 
	}
	
	public RAsset(String name, String mode) {
		super(name);
		this.mode = mode;
		if (mode.equals("if:TRUE") || mode.equals("if:FALSE"))
			setName("");
	}

	public String getLongString() {
		return getName();
	}

	public String getShortString() {
		return getName();
	}

	public Icon getIcon() {
		ImageIcon icon = null;
		if( icons.containsKey(mode)){
			icon = icons.get(mode); 
		}
		return icon;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Getter function for the field mode
	 */
	public String getMode() {
		return mode;
	}

}
