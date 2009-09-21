package com.addictedtor.jedit.objects.text;

import com.addictedtor.jedit.objects.NodeTextBuilder;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;

public class NodeTextBuilder_REXPList extends NodeTextBuilder{
    public String getText(REXP object, String name) throws REXPMismatchException {
        RList list = object.asList();
        return super.getDefaultText(object, name, "List of " + list.size());
    }
}
