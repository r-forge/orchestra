package com.addictedtor.jedit.objects;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import java.awt.*;

/**
 * Display builder service
 *
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public abstract class NodeDisplayBuilder {
    /**
     * Returns the Component that displays the information in the node for the given object
     * @param object R object
     * @param name name of the object
     * @return component displayed below the tree
     */
    public abstract Component getComponent(REXP object, String name) throws REXPMismatchException, REngineException;
}


