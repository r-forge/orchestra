package com.addictedtor.jedit.objects.text;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;

public class NodeTextBuilder_REXPDouble extends NodeTextBuilder_REXPVector {

    protected Object[] getData(REXP object) throws REXPMismatchException {
        double[] data1 = object.asDoubles();
        Object[] data2 = new Object[data1.length];
        for (int i=0; i<data1.length; i++) {
            data2[i] = data1[i];
        }
        return data2;
    }

    protected boolean isNA(Object o) {
        return REXPDouble.isNA((Double) o);
    }

    protected String getBaseTypeName() {
        return "num";
    }
}
