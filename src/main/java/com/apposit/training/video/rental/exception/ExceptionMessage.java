package com.apposit.training.video.rental.exception;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.Serializable;

/**
 * @author Eric Chijioke
 *
 */
public class ExceptionMessage implements Serializable {
	
	private static final long serialVersionUID = -7678745536735373804L;


	private String key;
	
	private String[] args;
	
	public ExceptionMessage (String key, String[] args){
		
		this.key = key;
		this.args = args;
	}
	

	public ExceptionMessage (String key, Throwable cause ){

		String message = null;
		
		if(cause != null) {

			message = cause.getMessage();
			
			cause = ExceptionUtils.getRootCause(cause);
			
			if(cause != null) {
				
				message = cause.getMessage();
			}
		}
		
		this.key = key;
		this.args = new String[]{message};
	}

	public ExceptionMessage (String key){

		this(key, (String[]) null);
	}

	public ExceptionMessage (String key, String arg1){

		this(key, new String[]{arg1});
	}

	public ExceptionMessage (String key, String arg1, String arg2){

		this(key, new String[]{arg1, arg2});
	}
	
	@Override
	public String toString() {
		
		return "Key: " + (key != null ? key : "") + "\n"
				+ "Args : " + (args != null ? StringUtils.join(args, ",") : "" ) + "\n";
	}
	
	public String getKey() {
		return key;
	}

	public String[] getArgs() {
		return args;
	}

}
