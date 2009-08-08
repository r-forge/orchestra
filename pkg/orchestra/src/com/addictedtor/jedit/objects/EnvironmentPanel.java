package com.addictedtor.jedit.objects;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.jEdit;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.editbus.AddToBusRunnable;
import com.addictedtor.jedit.editbus.EBCommandDone;
import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.debug.DebugEnvironment;
import com.addictedtor.jedit.objects.nodes.DebugNodeBuilder;
import com.addictedtor.jedit.objects.nodes.DebugTreeNode;
import com.addictedtor.jedit.objects.nodes.REXPReferenceNodeBuilder;
import com.addictedtor.jedit.rengine.jri.BrowseContextFlush;
import com.addictedtor.jedit.rengine.jri.BrowseContextUpdate;

/**
 * A panel that displays recursively the content of several R 
 * environments
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
@SuppressWarnings("serial")
public class EnvironmentPanel extends JPanel implements EBComponent {

	private DefaultMutableTreeNode root ; 
	private JTree tree; 
	private Map<String,REXPReference> environments ; 
	private Map<REXPReference,TreeNode> nodes; 

	@SuppressWarnings("unused")
	private String name ;
	private JScrollPane scroller; 

	/**
	 * Constructor, with no environments
	 */
	public EnvironmentPanel(){
		this( new HashMap<String,REXPReference>() ) ;
	}

	/**
	 * Constructor with several environments
	 * @param environments map of environments with their name
	 */
	public EnvironmentPanel( Map<String,REXPReference> environments ) {
		super( new BorderLayout() ) ;
		this.environments = environments;
		nodes = new HashMap<REXPReference,TreeNode>();

		root = new REXPTreeNode( null, "" ) ;
		tree = new JTree( new DefaultTreeModel(root) );
		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer( new REXPCellRenderer( ) ) ;
		
		/* TODO: is it worth having a real class for this */
        /* tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
                if (tp != null) {
                    REXPTreeNode node = (REXPTreeNode) tp.getLastPathComponent();
                    REXP object = node.getObject();
                    String name = node.getName();
                    EditBus.send( new DisplayMessage( NodeDisplayFactory.getComponent(object, name) ) ) ;
                }
            }
        }); */

        scroller = new JScrollPane(tree);
		add( scroller, BorderLayout.CENTER ); 
		refreshAll(); 
		SwingUtilities.invokeLater(new AddToBusRunnable( this ) ); 
	}

	/**
	 * Constructor with a single environment
	 * @param env the environment to display
	 * @param name the name of the environment
	 */
	public EnvironmentPanel(REXPReference env, String name) {
		this( createMap( env, name ) ) ;
	}

	/**
	 * @return the root of the environment tree
	 */
	public DefaultMutableTreeNode getRoot(){
		return root; 
	}

	/**
	 * The tree 
	 * @return the tree
	 */
	public JTree getTree(){
		return tree; 
	}

	/**
	 * Refresh all the environments
	 */
	public void refreshAll() {
		root.removeAllChildren(); 
		nodes = new HashMap<REXPReference,TreeNode>(); 

		for( Map.Entry<String, REXPReference> e : environments.entrySet() ){
			REXPReferenceNodeBuilder nb = new REXPReferenceNodeBuilder( true ) ;
			REXPReference ref = e.getValue() ;
			DefaultMutableTreeNode newnode = nb.getNode( ref , e.getKey()) ;
			root.add( newnode  ); 
			nodes.put( ref, newnode ); 
		}
		tree.setModel( new DefaultTreeModel( root ) ) ;
		tree.expandRow(0) ;
		tree.setRootVisible( false ) ;
		repaint() ;
	}

	/**
	 * Refresh the environment called name
	 * @param name name of the environment
	 */
	public void refresh(String name) {
		REXPReference reference = environments.get( name ) ;
		if( reference == null){
			return; 
		}

		int index = root.getIndex( nodes.get(reference)  ) ;
		if( index >= 0){
			root.remove( index ) ;
		}

		REXPReferenceNodeBuilder nb = new REXPReferenceNodeBuilder( true ) ;
		DefaultMutableTreeNode newnode = nb.getNode( reference , name ) ;
		nodes.put( reference, newnode );
		if( index >= 0){
			root.insert( newnode, index) ;
		} else{
			root.add( newnode ) ;
		}
		tree.setModel( new DefaultTreeModel( root ) ) ;
		tree.scrollPathToVisible( new TreePath(newnode.getLastLeaf() ) ) ;
		tree.expandRow(0) ;
		tree.setRootVisible( false ) ;
	}

	/**
	 * Adds a new environment to the tree
	 * @param name the name to give to the environment
	 * @param reference the reference to the environment
	 */
	public void add( String name, REXPReference reference){
		environments.put( name, reference ) ;
		REXPReferenceNodeBuilder nb = new REXPReferenceNodeBuilder( true ) ;
		DefaultMutableTreeNode newnode = nb.getNode( reference , name ) ;
		nodes.put( reference, newnode );
		root.add( newnode ) ;
		tree.expandRow(0) ;
		tree.setModel( new DefaultTreeModel( root ) ) ;
		tree.setRootVisible( false ) ;
	}

	/** 
	 * utility to create a map with a single value
	 * @param env enviroment
	 * @param name name
	 * @return a map name -> environment
	 */
	private static Map<String, REXPReference> createMap(REXPReference env,
			String name) {
		Map<String,REXPReference> map = new HashMap<String, REXPReference>();
		map.put( name, env) ;
		return map; 
	}

	@Override
	public void handleMessage(EBMessage message) {
		// TODO: need to do better than this
		if( message instanceof EBCommandDone ){
			refresh( ".GlobalEnv" ) ; 
		} else if( message instanceof BrowseContextUpdate){
			refreshDebugEnvironments( ); 
		} else if( message instanceof BrowseContextFlush){
			flushDebugEnvironments() ; 
		}
	}

	private void flushDebugEnvironments() {
		if( root.getChildCount() > 0){
			// Map<String,Buffer> buffers = getBuffers() ;
			for( int i=root.getChildCount()-1; i>=0; i--){
				REXPTreeNode node = (REXPTreeNode)root.getChildAt(i) ;
				String nodename = node.getName() ;
				if( node instanceof DebugTreeNode ){
					nodes.remove( environments.get(nodename) ) ;
					environments.remove(nodename) ;
					root.remove(i);
					
//					String file = ((DebugTreeNode)node).getDebugEnvironment().getFile(); 
//					if( buffers.containsKey( file  ) ){
//						buffers.get(file).setProperty("R-debuginfo", null) ;
//					}
					
				}
			}
		}

	}
	
	@SuppressWarnings("unused")
	private Map<String,Buffer> getBuffers(){
		Map<String,Buffer> buffers = new HashMap<String, Buffer>() ;
		for( Buffer buffer: jEdit.getBuffers() ){
			buffers.put( buffer.getPath(), buffer ) ;
		}
		return buffers; 
	}

	private void refreshDebugEnvironments() {
		flushDebugEnvironments() ; 
		DebugNodeBuilder nb = new DebugNodeBuilder( ) ;
		Map<String,DebugEnvironment> frames = getDebugEnvironments() ; 
		int i= 0;
		if( frames.size() > 0){
			for( Map.Entry<String,DebugEnvironment> e: frames.entrySet() ){
				i++;
				DebugEnvironment deb = e.getValue() ;
				DebugTreeNode newnode = (DebugTreeNode) nb.getNode(deb.getEnvironment(), deb.getName() ) ;
				newnode.setDebugEnvironment( deb ) ;
				nodes.put( deb.getEnvironment() , newnode );
				root.add( newnode ) ;
			}
		}
		tree.expandRow(0) ;
		tree.setModel( new DefaultTreeModel( root ) ) ;
		tree.setRootVisible( false ) ;
	}

	public Map<String,DebugEnvironment> getDebugEnvironments(){

		JRIEngine r = (JRIEngine)RPlugin.getR() ;
		int key = r.lock() ;

		String[] names ;
		int n ;
		String cmd ; 
		Map<String,DebugEnvironment> out = new TreeMap<String,DebugEnvironment>(); 
		RList locations ; 

		try {
			names     = r.parseAndEval("orchestra:::.getBrowseCallStackNames()").asStrings() ;
			n         = r.parseAndEval("orchestra:::.getBrowseFrameStackSize()").asInteger() ;
			locations = r.parseAndEval("orchestra:::.getBrowseCallStackLocations()").asList() ;
			for( int i=0; i<n; i++){
				cmd = "orchestra:::.getBrowseFrame("+(i+1)+")" ;
				REXPReference env = (REXPReference)r.parseAndEval(cmd, r.globalEnv, false ) ;
				REXP stackloc = locations.at(i) ;
				DebugEnvironment deb = new DebugEnvironment( env, names[i], stackloc ) ; 
				out.put( "_[" + (i+1) +"] "+ names[i], deb ) ;
			}

		} catch (REngineException e) {
			ExceptionManager.send(e) ;
			return null; 
		} catch (REXPMismatchException e) {
			ExceptionManager.send(e) ;
			return null;
		} finally{
			r.unlock( key ) ;
		}
		return out ; 


	}


}
