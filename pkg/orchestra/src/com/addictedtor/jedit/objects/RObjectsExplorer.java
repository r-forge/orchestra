package com.addictedtor.jedit.objects;

//{{{ imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.DefaultFocusComponent;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.editbus.EBCommandDone;

//}}}

public class RObjectsExplorer extends JPanel implements EBComponent,
		DefaultFocusComponent, ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JTable content;

	private ObjectsExplorerTableModel model;

	// private TableRowSorter<ObjectsExplorerTableModel> sorter ;

	private JLabel envLabel;

	public JComboBox envCombo;

	// }}}

	// {{{ Constructor
	public RObjectsExplorer(View view) {
		super(new BorderLayout());
		Box topBox = new Box(BoxLayout.Y_AXIS);
		GridBagLayout layout = new GridBagLayout();
		JPanel envAndFilterPanel = new JPanel(layout);

		GridBagConstraints cons = new GridBagConstraints();
		cons.gridwidth = cons.gridheight = 1;
		cons.gridx = cons.gridy = 0;
		cons.fill = GridBagConstraints.BOTH;
		cons.anchor = GridBagConstraints.EAST;
		envLabel = new JLabel("env", SwingConstants.RIGHT);
		envLabel.setToolTipText("R Environment to browse");
		envLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
		layout.setConstraints(envLabel, cons);
		envAndFilterPanel.add(envLabel);

		envCombo = new JComboBox(searchpath());
		envCombo.addActionListener(this);

		Dimension prefSize = envCombo.getPreferredSize();
		prefSize.width = 0;
		envCombo.setPreferredSize(prefSize);
		envCombo.addActionListener(this);
		cons.gridx = 1;
		cons.weightx = 1.0;
		cons.gridwidth = GridBagConstraints.REMAINDER;

		layout.setConstraints(envCombo, cons);

		envAndFilterPanel.add(envCombo);

		topBox.add(envAndFilterPanel);
		this.add(topBox, BorderLayout.NORTH);

		model = new ObjectsExplorerTableModel();
		// sorter = new TableRowSorter<ObjectsExplorerTableModel>(model);
		content = new JTable(model);
		// content.setRowSorter( sorter ) ;

		initColumnSize(content);
		content.getSelectionModel().addListSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(content);
		// content.setFillsViewportHeight(true);

		this.add(scrollPane, BorderLayout.CENTER);

		
		setVisible(true);
		addNotify();
		refresh();

	}

	private void initColumnSize(JTable table) {

		// icon
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(20);
		column.setMaxWidth(20);

		// name
		column = table.getColumnModel().getColumn(1);
		column.setPreferredWidth(200);

		// dimension
		column = table.getColumnModel().getColumn(2);
		column.setPreferredWidth(60);
		column.setMaxWidth(100);

		// group
		column = table.getColumnModel().getColumn(3);
		column.setPreferredWidth(100);
		column.setMaxWidth(150);

	}

	public void valueChanged(ListSelectionEvent e) {
	}

	public static String[] searchpath() {
		String[] out = null;
		REngine r = RPlugin.getR(); 
		int key = r.lock(); 
		try {
			out = r.parseAndEval("search()").asStrings() ; 
		} catch(REngineException re) {
		} catch(REXPMismatchException rm){
		} finally {
			r.unlock( key );
		}
		return out;
	}

	// {{{ focusOnDefaultComponent
	public void focusOnDefaultComponent() {
	}

	public void actionPerformed(ActionEvent evt) {
		refresh();
	}

	// }}}

	// EBComponent implementation

	// {{{ handleMessage
	public void handleMessage(EBMessage message) {
		if (message instanceof EBCommandDone) {
			refresh();
		}
	}

	// }}}

	// {{{ propertiesChanged
	@SuppressWarnings("unused")
	private void propertiesChanged() {
	}

	// }}}

	// These JComponent methods provide the appropriate points
	// to subscribe and unsubscribe this object to the EditBus.
	// {{{ addNotify
	@Override
	public void addNotify() {
		super.addNotify();
		EditBus.addToBus(this);
	}

	// }}}

	// {{{ removeNotify
	@Override
	public void removeNotify() {
		super.removeNotify();
		EditBus.removeFromBus(this);
	}

	// }}}

	public void refresh(String text) {
		model.refresh(text);
	}

	public void refresh() {
		Object ob = envCombo.getSelectedItem();
		refresh(ob == null ? ".GlobalEnv" : (String) ob);
	}

	private class ObjectsExplorerTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -5798827464489249334L;

		private String[] columnNames = { "", "Name", "Dims", "Group" };
		private RObjectExplorerObject[] data = null;

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data == null ? 0 : data.length;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public Class<?> getColumnClass(int c) {
			Class<?> colClass = String.class;
			if (c == 0) {
				colClass = ImageIcon.class;
			}
			return colClass;
		}

		public Object getValueAt(int row, int col) {
			if (data == null)
				return null;
			if (col == 0)
				return data[row].getIcon();
			if (col == 1)
				return data[row].getName();
			if (col == 2)
				return data[row].getDim();
			if (col == 3)
				return data[row].getGroup();

			return null;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			boolean result = false;
			return result;
		}

		public void refresh(String text) {

			int n;
			String[] names = null;
			String[] classes = null;
			String[] groups = null;
			String[] dims = null;
			RList out = null; 
			
			REngine r = RPlugin.getR(); 
			int key = r.lock(); 
			String cmd = "as.data.frame( objList( path=NULL, all.info=TRUE, compare = FALSE, env = '"
				+ text
				+ "' )[, c('Name','Class','Group','Dims')] ) "; 
			try {
				out = r.parseAndEval(cmd).asList() ;
			} catch(REngineException e) {
			} catch(REXPMismatchException rm){
			} finally {
				r.unlock(key); 
			}

			try{
				if( out == null){
					data = null; 
				} else{
					n = out.at(0).length() ;
					if( n == 0 ){
						data = null; 
					} else{
						data = new RObjectExplorerObject[n];
						names   = out.at("Name").asStrings() ;
						classes = out.at("Class").asStrings(); 
						groups  = out.at("Group").asStrings(); 
						dims    = out.at("Dims").asStrings() ;

						for (int i = 0; i < n; i++) {
							data[i] = new RObjectExplorerObject(names[i], classes[i],
									groups[i], dims[i]);
						}
					}
				}
			} catch( REXPMismatchException rm){
				data = null; 
			}
			fireTableDataChanged();
		}
		
	}

	private static class RObjectExplorerObject {
		private String name;
		private String Class;
		private String Group;
		private String Dim;

		private static final HashMap<String, ImageIcon> icons;
		static {
			icons = new HashMap<String, ImageIcon>();
			icons.put("function", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/function.png")));
			icons.put("character", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/character.png")));
			icons.put("data.frame", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/data.frame.png")));
			icons.put("factor", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/factor.png")));
			icons.put("list", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/list.png")));
			icons.put("logical", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/logical.png")));
			icons.put("matrix", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/matrix.png")));
			icons.put("numeric", new ImageIcon(RObjectsExplorer.class
					.getResource("/icons/explorer/numeric.png")));
		}

		public RObjectExplorerObject(String name, String Class, String group,
				String dim) {
			this.name = name;
			this.Dim = dim;
			this.Group = group;
			this.Class = Class;
		}

		/**
		 * Getter function for the field name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter function for the field Dim
		 */
		public String getDim() {
			return Dim;
		}

		/**
		 * Getter function for the field Class
		 */
		public String getRClass() {
			return Class;
		}

		/**
		 * Getter function for the field Group
		 */
		public String getGroup() {
			return Group;
		}

		public ImageIcon getIcon() {
			if (icons.containsKey(Class))
				return icons.get(Class);
			return null;
		}

	}

}