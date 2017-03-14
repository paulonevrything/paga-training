package com.apposit.training.video.rental.exception;

/**
 * @author Eric Chijioke
 *
 */
public interface Exception {

	public ExceptionCode getCode();
	
	public Class<Object> getThrower();
	
	public ExceptionMessage getExceptionMessage();
}
