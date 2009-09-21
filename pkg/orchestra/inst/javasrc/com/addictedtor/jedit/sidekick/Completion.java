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
 * Abstract superclass of all Completion classes used in the plugin
 * <p>
 * This is currently a placeholder
 * </p>
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public abstract class Completion {
	
	public void setSelected( PowerEditorCompletionPopup popup ){
		
	}
	
	public boolean needsEditor( ){
		return false ; 
	}
	
}
