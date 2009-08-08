package com.addictedtor.jedit.objects.icons;

import javax.swing.Icon;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.RList;

public class S4IconBuilder_classRepresentation extends SimpleS4IconBuilder {

	public S4IconBuilder_classRepresentation(){
		super( "classRepresentation" ) ;
	}
	
	@Override
	public Icon getIcon(REXP object) {
		
		String name = "class" ;
		if( isVirtual( object ) ){
			name = name + "-virtual" ;
		}
		if( isSealed( object ) ){
			name = name + "-sealed" ;
		}
				
		return SimpleIconBuilder.getIcon( "classes/" + name , true);
	}

	/** 
	 * is the class represented by the object virtual
	 * @return true if the class is virtual
	 * @deprecated this should be a method of REXPS4
	 */
	@Deprecated
	private boolean isVirtual(REXP object){
		return is( object, "virtual") ;
	}
	
	/** 
	 * is the class represented by the object sealed
	 * @return true if the class is sealed
	 * @deprecated this should be a method of REXPS4
	 */
	@Deprecated
	private boolean isSealed( REXP object){
		return is( object, "sealed") ;
	}
	
	private boolean is( REXP object, String what  ){
		try{
			REXPList attributes = object._attr() ;
			if( attributes == null){
				return false; 
			}
			RList attribute_list = attributes.asList() ;
			if( attribute_list == null){
				return false; 
			}
			REXP at = attribute_list.at( what ) ; 
			if( at == null){
				return false; 
			} else{
				if( ! at.isLogical( ) ){
					return false; 
				} else{
					return ((REXPLogical)at).isTRUE()[0] ;
				}
			}
		}
		catch( Exception e){
			return false; 
		}
	}

}
