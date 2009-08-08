package com.addictedtor.jedit.objects.text;

import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.NodeTextBuilder;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

public abstract class NodeTextBuilder_REXPVector extends NodeTextBuilder {

	public static final int LIMIT = 5 ;

    protected abstract Object[] getData(REXP object) throws REXPMismatchException ;
    protected abstract boolean isNA(Object o);
    protected abstract String getBaseTypeName();

    protected String elementToString(REXP object, Object o) throws REXPMismatchException{
        return o.toString();
    }

	@Override
	public String getText(REXP object, String name) throws REXPMismatchException {
		Object[] data = null ;
		try {
            data = getData(object);
		} catch (REXPMismatchException e) {
			ExceptionManager.send( e ) ;
			return name; 
		}
		StringBuilder result = new StringBuilder() ;
		if( data != null){
			if(data.length == 0){
				result.append(getBaseTypeName() + "(0)" );
			} else{
				result.append(getBaseTypeName() + " [1:" + data.length + "] " );
			}
			int top = data.length > LIMIT ? LIMIT : data.length ;
			Object d ;
			for( int i=0; i<top; i++){
				d = data[i] ;
				if( isNA(d)){
					result.append( " NA") ;
				} else{
					result.append( elementToString(object, d) + " " ) ;
				}
			}
			if( data.length > top){
				result.append( " ... " );
			}
		}
		
		return super.getDefaultText(object, name, result.toString());
	}

}
