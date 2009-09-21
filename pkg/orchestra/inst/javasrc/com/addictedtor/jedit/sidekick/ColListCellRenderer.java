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

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/**
 * List Renderer for color completion. This is used to render an item of the
 * list of color completion. A JTable is used as the renderer of the item of the
 * list
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class ColListCellRenderer extends JTable implements ListCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * Placeholder Constructor
	 */
	public ColListCellRenderer() {
	}

	/**
	 * Returns the component used to render the color completion, using a JTable
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setModel(new ColListCellRendererTableModel((ColCompletion) value));
		setDefaultRenderer(ColorObject.class, new ColorTableCellRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		if (isSelected) {
			getSelectionModel().setSelectionInterval(0, 0);
		}
		initColumnSize();
		return this;
	}

	/**
	 * Initialize the size of each column of the JTable that is used to render
	 * the completion
	 */
	private void initColumnSize() {

		// icon
		TableColumn column = getColumnModel().getColumn(0);
		column.setPreferredWidth(20);
		column.setMaxWidth(20);

		// value
		column = getColumnModel().getColumn(1);
		column.setPreferredWidth(150);
		column.setMaxWidth(150);

	}

	/**
	 * Renderer for each cell of the table
	 * 
	 * @author Romain Francois <francoisromain@free.fr>
	 */
	private static class ColListCellRendererTableModel extends
			AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private ColCompletion comp;

		/**
		 * Constructor
		 * 
		 * @param comp
		 */
		public ColListCellRendererTableModel(ColCompletion comp) {
			this.comp = comp;
		}

		/**
		 * One row
		 */
		@Override
		public int getRowCount() {
			return 1;
		}

		/**
		 * Three columns
		 */
		public int getColumnCount() {
			return 2;
		}

		/**
		 * The first column is of class ColorObject, and the other columns are
		 * simple text (String)
		 */
		@Override
		public Class<?> getColumnClass(int col) {
			if (col == 0)
				return ColorObject.class;
			return String.class;
		}

		/**
		 * The object represented in each cell of the renderer
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return comp.getColor();
			case 1:
				return comp.getValue();
			}
			return null;
		}

	}

}
