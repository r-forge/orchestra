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

/**
 * Simple Wrapper around Color
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class ColorObject {

	/**
	 * The color associated with this object
	 */
	private Color color;

	/**
	 * R name of the color
	 */
	private String name;

	/**
	 * Simple Constructor (no R name)
	 * 
	 * @param color
	 */
	public ColorObject(Color color) {
		this.color = color;
		name = "";
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name of the color
	 * @param red
	 *            red channel
	 * @param green
	 *            green channel
	 * @param blue
	 *            blue channel
	 */
	public ColorObject(String name, int red, int green, int blue) {
		this.name = name;
		this.color = new Color(red, green, blue);
	}

	/**
	 * Getter function for the field color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the name of the color
	 * 
	 * @return the R name
	 */
	public String getName() {
		return name;
	}

}
