package com.addictedtor.jedit.objects.text;

import com.addictedtor.jedit.objects.NodeTextBuilder;
import org.rosuda.REngine.REXP;

public class SimpleNodeTextBuilder extends NodeTextBuilder {

	public SimpleNodeTextBuilder(){}
	
	@Override
	public String getText(REXP object, String name) {
		return super.getDefaultText(object, name, "");
	}

}
