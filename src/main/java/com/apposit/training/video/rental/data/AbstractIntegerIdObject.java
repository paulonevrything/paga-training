package com.apposit.training.video.rental.data;

import java.util.Locale;


/**
 * This is an abstract base class for all DAOObject which use and integer id and require that the id be set (evn if 0)
 * @author Eric Chijioke
 *
 */
public abstract class AbstractIntegerIdObject extends AbstractObject {

	private static final long serialVersionUID = -5787095836740707574L;

	public AbstractIntegerIdObject() {
		this(0);
	}
	
	public AbstractIntegerIdObject(Object id) {
		super(id == null ? 0 : id);
	}

	public AbstractIntegerIdObject(Locale locale) {
		this(0,locale);
	}
	
	public AbstractIntegerIdObject(Object id, Locale locale) {
		super(id == null ? 0 : id,  locale);
	}
	
	@Override
	public void setId(Object id) {

		if(id == null) { throw new IllegalArgumentException("AbstractIntegerIdObject id cannot be null"); }
		
		if(id instanceof String[]) {
			id = Integer.parseInt(((String[])id)[0]);
		}
	
		super.setId(id);
	}

	@Override
	public Object getId() {
		return super.getId() == null ? 0 : super.getId();
	}
}

