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

/**
 * Completion class for lines of a DESCRIPTION file
 * 
 * @author Romain Francois <francoisromain@free.fr>
 * 
 */
public class DescriptionFileCompletion extends Completion {

	/**
	 * Value of the completion
	 */
	private String value;

	/**
	 * type of completion
	 */
	private String type;

	/**
	 * Constructor
	 * 
	 * @param line
	 *            line of text this is attempting to complete
	 * @param type
	 *            type of completion
	 */
	public DescriptionFileCompletion(String field, String completion, String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

}
