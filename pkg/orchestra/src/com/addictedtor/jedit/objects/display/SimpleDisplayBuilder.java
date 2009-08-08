package com.addictedtor.jedit.objects.display;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.error.ExceptionManager;
import com.addictedtor.jedit.objects.NodeDisplayBuilder;
import com.addictedtor.jedit.tools.REngineWrapper;

/**
 * Simple component node display, creates a text area that contains the 
 * result of : capture.output( str( . ) ) on the object
 */
public class SimpleDisplayBuilder extends NodeDisplayBuilder{
    
	public Component getComponent(REXP object, String name) throws REXPMismatchException, REngineException {
        JRIEngine re = (JRIEngine)RPlugin.getR();
        REXP str_call = REngineWrapper.call( "str", object ) ;
        REXP capture_call = REngineWrapper.call( "capture.output", str_call ) ;
        REXP res = re.eval( capture_call, re.globalEnv, true ) ; 
        String[] data  = null ;
        try{
        	data = res.asStrings() ;
        } catch (REXPMismatchException e) {
			ExceptionManager.send( e ) ;
		}
        if( data == null){
        	return new JLabel( "not str for the object" ) ;
        }
        JTextArea ta = new JTextArea();
        for (int i=0; i<data.length; i++){
            ta.append(data[i] + "\n");
        }
        return new JScrollPane(ta);
    }
    
}
