package com.addictedtor.jedit.objects.nodes;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.NodeBuilder;
import com.addictedtor.jedit.objects.NodeFactory;
import com.addictedtor.jedit.objects.REXPTreeNode;

/**
 * Node builder for REXPReference objects. This only 
 * makes sense when the REXPReference is a reference
 * to an environment 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class REXPReferenceNodeBuilder extends NodeBuilder {

	/**
	 * should we resolve the content of the environment
	 */
	protected boolean resolve ; 
	
	/**
	 * Constructor. 
	 * @param resolve should the content be resolved
	 */
	public REXPReferenceNodeBuilder( boolean resolve ){
		this.resolve = resolve ;
	}
	
	/**
	 * Default constructor. Does not resolve the objects in the environment
	 */
	public REXPReferenceNodeBuilder(  ){
		this( false ) ;
	}
	
	// TODO: return a dummy node if object is not a REXPReference pointing
	//       to an environment
	@Override
	public DefaultMutableTreeNode getNode(REXP object, String name) {

		// object is a REXPReference that is supposed to be an 
		// environment

		DefaultMutableTreeNode node = new REXPTreeNode( object, name ) ;
		REngine r = RPlugin.getR(); 

		if( resolve ){
			String[] variables = getListEnv( (REXPReference)object, true ) ;
			if( variables != null){

				/* first get the objects */
				Map<String,REXPReference> data = new HashMap<String,REXPReference>(); 
				for( String v: variables){
					int key = r.lock();  
					try {
						REXPReference ob = (REXPReference)r.get( v, object, false) ;
						data.put( v, ob ) ;
						
					} catch (REngineException re) {
						ExceptionManager.send(re) ;
						re.printStackTrace() ;
					} catch (REXPMismatchException rm) {
						ExceptionManager.send(rm) ;
						rm.printStackTrace() ;
					} finally{
						r.unlock( key ) ;
					}
				}

				/* then make nodes */
				// TODO: get only references
				for( Map.Entry<String, REXPReference> e : data.entrySet() ){
					REXPReference ref = e.getValue() ; 
					String nam = e.getKey() ;
					REXPTreeNode newnode ;
					REXP o = ref.resolve() ;
					if( o == null){
						newnode = new UnknownNode(nam, ref) ;
					} else {
						newnode = (REXPTreeNode) NodeFactory.getNode( o, nam ) ;
					}
					node.add( newnode ) ;
				}
			}
		} 
		return node; 

	}

	/**
	 * Names of the objects in this environment
	 * @param env a reference to an environment
	 * @param all extract all names (even those starting with a period)
	 * @return the names of the objects
	 */
	@Deprecated
	private String[] getListEnv( REXPReference env, boolean all ){

		JRIEngine r = (JRIEngine)RPlugin.getR();
		Rengine rni = r.getRni(); 
		long h = ((Long)env.getHandle()).longValue(); 
		long l = rni.rniListEnv(h, all) ;
		REXPReference ref = new REXPReference( r, new Long(l) ) ;
		int key = r.lock( ) ;
		REXP res; 
		try {
			res = r.resolveReference(ref) ;
		} catch (REngineException re) {
			ExceptionManager.send(re) ;
			return null; 
		} catch (REXPMismatchException rm) {
			ExceptionManager.send(rm) ;
			return null; 
		} finally {
			r.unlock( key ) ;
		}
		if( res == null ){
			return null; 
		}
		try {
			String[] out = res.asStrings() ;
			return out ;
		} catch (REXPMismatchException e) {
			ExceptionManager.send(e) ;
		}

		return null; 
	}



}
