package com.apposit.training.video.rental.data.auditing;

import com.apposit.training.video.rental.data.DAOObject;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author Eric Chijioke
 *
 */
public class AuditableObjectMixin extends DelegatingIntroductionInterceptor implements Auditable {

	private static final long serialVersionUID = 5605954154423062517L;
	
	protected String updatedBy;
	
	protected Date updatedDate;

	protected String createdBy;
	
	protected Date createdDate;
	
	private DAOObject delegate;

	public AuditableObjectMixin(DAOObject delegate) {
		
		super(delegate);
		this.delegate = delegate;
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
	
	@Override
	public Object getId() {
		return delegate.getId();
	}
	
	@Override
	public Locale getLocale() {
		return delegate.getLocale();
	}
	
	@Override
	public boolean isReadOnly() {
		return delegate.isReadOnly();
	}
	
	@Override
	public void setId(Object id) {
		delegate.setId(id);
	}
	
	@Override
	public void setLocale(Locale locale) {
		delegate.getLocale();
	}
}
