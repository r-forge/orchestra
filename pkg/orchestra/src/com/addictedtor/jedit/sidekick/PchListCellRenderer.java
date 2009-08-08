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

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class PchListCellRenderer extends JTable implements ListCellRenderer {

	private static final long serialVersionUID = 7949181007710638564L;

	public PchListCellRenderer() {
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setModel(new PchListCellRendererTableModel((PchCompletion) value));
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		if (isSelected) {
			getSelectionModel().setSelectionInterval(0, 0);
		}
		initColumnSize();
		return this;
	}

	private void initColumnSize() {

		// icon
		TableColumn column = getColumnModel().getColumn(0);
		column.setPreferredWidth(20);
		column.setMaxWidth(20);

		// value
		column = getColumnModel().getColumn(1);
		column.setPreferredWidth(50);
		column.setMaxWidth(50);

		// dimension
		column = getColumnModel().getColumn(2);
		column.setPreferredWidth(200);
		column.setMaxWidth(200);

	}

	private static class PchListCellRendererTableModel extends
			AbstractTableModel {

		private static final long serialVersionUID = 691278883181721229L;

		private PchCompletion comp;

		public PchListCellRendererTableModel(PchCompletion comp) {
			this.comp = comp;
		}

		public int getRowCount() {
			return 1;
		}

		public int getColumnCount() {
			return 3;
		}

		@Override
		public Class<?> getColumnClass(int col) {
			if (col == 0)
				return ImageIcon.class;
			return String.class;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return comp.getIcon();
			case 1:
				return comp.getValue();
			case 2:
				return "";
			}
			return null;
		}
	}

}
