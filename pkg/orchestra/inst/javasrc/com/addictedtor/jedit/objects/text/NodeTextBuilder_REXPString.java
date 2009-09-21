package com.addictedtor.jedit.objects.text;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

public class NodeTextBuilder_REXPString extends NodeTextBuilder_REXPVector{

    protected Object[] getData(REXP object) throws REXPMismatchException {
        return object.asStrings();
    }

    protected boolean isNA(Object o) {
        // todo: this is bad! I remember this was missing from JRI?
        return o.toString().equals("NA");
    }

    protected String getBaseTypeName() {
        return "chr";
    }
}
