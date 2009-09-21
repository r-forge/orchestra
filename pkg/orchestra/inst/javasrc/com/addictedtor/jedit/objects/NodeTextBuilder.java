package com.addictedtor.jedit.objects;

import org.apache.commons.lang.StringUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

/**
 * Text builder service
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public abstract class NodeTextBuilder {

	/**
	 * Returns the text that is displayed in the node for the given object
	 * @param object R object
	 * @param name name of the object
	 * @return the text to use in the tree
	 */
	public abstract String getText(REXP object, String name) throws REXPMismatchException;

    public static String getDefaultText(REXP object, String name, String simpleDesc) {
//        return "<html><font size=\"6\" color=\"000000\">" + name  + "</font></html>";
        String na2 = StringUtils.abbreviate(name, 15);
        // na2 = StringUtils.rightPad(na2, 15).replace(" ", "&nbsp;");
        
        String out ; 
        out = "<html>" +"" +
                "<span style='font-family:Courier; font-size:10px; color:#ff0000;'>" + na2 + "</span>"; 
        if( simpleDesc != null ){
               out = out + "&nbsp;<span style='font-family:Courier; font-size:8px; color:#AAAAAA;'>" +  simpleDesc  + "</span>" ;
        }
        out = out + "</html>";
        return out ;
    }
}
