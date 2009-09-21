package com.addictedtor.jedit.editbus;

import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EditBus;

/**
 * Small utilitity to delay the adding of one component to the edit bus
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class AddToBusRunnable implements Runnable {

	private EBComponent component; 
	
	public AddToBusRunnable( EBComponent component){
		this.component = component; 
	}
	
	@Override
	public void run() {
		EditBus.addToBus(component) ;
	}

}
