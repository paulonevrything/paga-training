package com.apposit.training.video.rental.data;

import com.apposit.training.video.rental.data.archiving.ArchivingStatus;
import com.apposit.training.video.rental.data.auditing.Auditable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Locale;

/**
 * @author Eric Chijioke
 *
 */
public abstract class AbstractObject implements Auditable{

	private static final long serialVersionUID = -7971402657098346095L;

	protected Object id;
	
	protected String updatedBy;
	
	protected Date updatedDate;

	protected String createdBy;
	
	protected Date createdDate;
	
	private Locale locale;
	
	private ArchivingStatus archivingStatusId;
	
	private Integer masterEntityId;
	
	private boolean isIdentified = false;
		
	public AbstractObject(){
		super();
	}

	public AbstractObject(Locale locale){
		super();
		this.locale = locale;
	}
	
	public AbstractObject(Object id) {
		
		this.id = id;
	}
	
	public AbstractObject(Object id, Locale locale) {
		
		this(id);
		this.locale = locale;
	}

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id){
		this.id = id;
	}

	//allow Jaxb serialization of Locale which doesn't have no-arg constructor
	@XmlJavaTypeAdapter(JaxbLocaleAdapter.class)
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setLocale(String locale) {
		
		this.locale = new Locale(locale);
	}

	public void setLocale(String language, String country) {

		this.locale = new Locale(language, country);
	}
			
	/**
	 * Overrides this method to equate documents that have the same path.
	 */
	@Override
	public boolean equals(Object other) {

		if(other == null) return false;
		
		if(other instanceof AbstractObject) {
			
			AbstractObject otherDomainObject = (AbstractObject) other;

			if(otherDomainObject.id == null) return false;
			
			return otherDomainObject.id.equals(this.id);
			
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		
		int hash = 7;
		hash = 31 * hash + (id == null? 0 : id.toString().hashCode());
		return hash;
	}
	
	
	@Override
	public String getUpdatedBy() {
		return updatedBy;
	}

	@Override
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public Date getUpdatedDate() {
		return updatedDate;
	}

	@Override
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
