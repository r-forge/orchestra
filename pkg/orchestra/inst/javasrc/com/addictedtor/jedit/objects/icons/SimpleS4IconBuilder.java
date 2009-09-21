package com.addictedtor.jedit.objects.icons;

import javax.swing.Icon;

import org.rosuda.REngine.REXP;

public class SimpleS4IconBuilder extends S4IconBuilder {

	private String clazz ;
	public SimpleS4IconBuilder( String clazz ){
		this.clazz = clazz; 
	}
	@Override
	public Icon getIcon(REXP object) {
		return SimpleIconBuilder.getIcon( "classes/" + clazz, true) ;
	}

}
