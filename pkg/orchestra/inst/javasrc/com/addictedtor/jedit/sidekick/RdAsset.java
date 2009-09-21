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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import sidekick.Asset;

public class RdAsset extends Asset {

	public RdAsset(String name) {
		super(name);
	}

	public String getLongString() {
		return getName();
	}

	public String getShortString() {
		return getName();
	}

	public Icon getIcon() {
		ImageIcon icon = null;
		return icon;
	}

	@Override
	public String toString() {
		return name;
	}

}
