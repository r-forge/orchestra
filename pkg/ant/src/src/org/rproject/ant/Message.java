package org.rproject.ant;

public class Message {
	public static final int MESSAGE = 0; 
	public static final int ERROR = 1; 
	
	private String message ; 
	private int type = MESSAGE ;
	
	public Message( String message, int type){
		this.message=message; 
		this.type = type ;
	}
	
	public Message( String message){
		this( message, MESSAGE ) ;
	}
	
	public String getMessage(){
		return message ;
	}
	
	public int getType(){
		return type ;
	}
	
}
