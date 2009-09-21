package com.addictedtor.jedit.objects.text;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;

public class NodeTextBuilder_REXPInteger extends NodeTextBuilder_REXPVector{

    protected Object[] getData(REXP object) throws REXPMismatchException {
        int[] data1 = object.asIntegers();
        Object[] data2 = new Object[data1.length];
        for (int i=0; i<data1.length; i++) {
            data2[i] = data1[i];
        }
        return data2;
    }

    protected boolean isNA(Object o) {
        return REXPInteger.isNA((Integer) o);
    }

    protected String getBaseTypeName() {
        return "int";
    }
}
