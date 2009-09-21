package com.addictedtor.jedit.objects.text;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;

public class NodeTextBuilder_REXPLogical extends NodeTextBuilder_REXPVector {

    protected Object[] getData(REXP object) throws REXPMismatchException {
        byte[] data1 = object.asBytes();
        Object[] data2 = new Object[data1.length];
        for (int i=0; i<data1.length; i++) {
            data2[i] = data1[i];
        }
        return data2;
    }

    protected boolean isNA(Object o) {
        return REXPLogical.isNA((Byte) o);
    }

    protected String getBaseTypeName() {
        return "log";
    }

}
