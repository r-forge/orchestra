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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer for a cell of a JTable with specified background clor
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class ColorTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ColorTableCellRenderer() {
		super();
	}

	/**
	 * Grabs a default table cell renderer and change its background color to
	 * the color in the value
	 * 
	 * @param value
	 *            a color object, the color is used as background color of the
	 *            rendered cell
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		Component c = renderer.getTableCellRendererComponent(table, "",
				isSelected, hasFocus, row, column);
		c.setBackground(((ColorObject) value).getColor());
		return c;
	}
}
