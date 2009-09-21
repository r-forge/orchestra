package com.addictedtor.jedit.objects.icons;

import javax.swing.Icon;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.JRI.JRIEngine;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.objects.REXPIconBuilder;

public class IconBuilder_REXPReference extends REXPIconBuilder {

	@Override
	public Icon getIcon(REXP object) {
		Icon icon ; 
		if( object.equals( ( (JRIEngine) RPlugin.getR()).globalEnv ) ){
			icon = SimpleIconBuilder.getIcon( "global-env" , true) ;
		} else{
			icon = SimpleIconBuilder.getIcon( "environment" , true) ;
		}
		return icon ;
		
	}
	
}

