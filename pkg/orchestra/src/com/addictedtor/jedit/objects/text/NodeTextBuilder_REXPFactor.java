package com.addictedtor.jedit.objects.text;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPMismatchException;

public class NodeTextBuilder_REXPFactor extends NodeTextBuilder_REXPVector{
    protected Object[] getData(REXP object) throws REXPMismatchException {
      int[] data1 = object.asIntegers();
        Object[] data2 = new Object[data1.length];
        for (int i=0; i<data1.length; i++) {
            data2[i] = data1[i];
        }
        return data2;
    }

    protected boolean isNA(Object o) {
        return REXPFactor.isNA((Integer) o);
    }

    protected String getBaseTypeName() {
        return "fact";
    }

    @Override
    protected String elementToString(REXP object, Object o) throws REXPMismatchException {
        return object.asFactor().levelAtIndex((Integer) o);
    }
}
