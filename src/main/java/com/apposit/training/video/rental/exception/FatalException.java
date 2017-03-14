package com.apposit.training.video.rental.exception;

/**
 * @author Eric Chijioke
 *
 */
public class FatalException extends RuntimeException implements Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8676070865808704510L;

	Class<Object> thrower = null;
	
	ExceptionMessage message =null;
	
	ExceptionCode code;
	
	/**
	 * Constructor FatalException.
	 * @param Exception
	 */
	public FatalException(String key, Object object, ExceptionCode code) {
		
		this(new ExceptionMessage(key),object,code);
		
	}
	
	/**
	 * Constructor FatalException.
	 * @param Exception
	 */
	public FatalException(Throwable throwable, Object object, ExceptionCode code) {
		
		this(new ExceptionMessage(throwable.getMessage()), object, code);
		
	}

	/**
	 * Constructor FatalException.
	 * @param string
	 */
	@SuppressWarnings("unchecked")
	public FatalException(ExceptionMessage message, Object object, ExceptionCode code) {
		
		super(message.getKey());
		
		thrower = object == null ? null : (Class<Object>) object.getClass();
		
		this.code = code;
		
		this.message = message;
				
	}

	/**
	 * Returns the code.
	 * @return int
	 */
	public ExceptionCode getCode() {
		return code;
	}

	/**
	 * Returns the thrower.
	 * @return String
	 */
	public Class<Object> getThrower() {
		return thrower;
	}
	
	public ExceptionMessage getExceptionMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		
		if(message == null) {
			return super.getMessage();
		}
		
		return message.toString();
	}
	
	
}
