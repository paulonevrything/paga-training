package com.apposit.training.video.rental.data;

import java.io.Serializable;
import java.util.Locale;

/**
 * This is the base data object interface that the DAO framework works with.
 * it supports identifiable, internationalized objects
 * 
 * @author Eric Chijioke
 *
 */
public interface DAOObject extends Serializable {

	public Object getId();
	
	public void setId(Object id);

	public Locale getLocale();

	public void setLocale(Locale locale);
	
	/**
	 * Determines if this object can be modified (updated, deleted or inserted) (true).
	 * This property will advise data layer frameworks when dealing with a domain object to prevent invalid writes
	 * 
	 * @return
	 */
	public abstract boolean isReadOnly();
	
	//TODO:
	//public abstract boolean supports(Class<? extends DAOObject> clazz);
}
