package org.rproject.ant;

public interface MessageListener {

	public void send( Message message) ;
	
	public void flush() ; 
}
