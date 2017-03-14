package com.apposit.training.video.rental.data.auditing;

import com.apposit.training.video.rental.data.DAO;
import com.apposit.training.video.rental.data.DAODecorator;
import com.apposit.training.video.rental.data.DAOObject;
import com.apposit.training.video.rental.exception.ExceptionCode;
import com.apposit.training.video.rental.exception.ExceptionMessage;
import com.apposit.training.video.rental.exception.FatalException;
import com.apposit.training.video.rental.security.SecurityService;
import org.springframework.aop.framework.ProxyFactory;

import java.util.List;

/**
 * @author Eric Chijioke
 *
 */
public class AuditingDAO extends DAODecorator {

	private SecurityService securityService;
	private String defaultSystemUserName = "system";
	
	public AuditingDAO(DAO dao){
	
		super(dao);
	}
	
	/* (non-Javadoc)
	 * @see com.apposit.service.data.ConcurrentSQLDomainObjectDAO#delete(com.apposit.service.data.DomainObject)
	 */
	@Override
	public int delete(DAOObject object) {

		if(object == null || !object.getClass().isAnnotationPresent(Audited.class) ) {

			return super.delete(object);
		}
		
		if(!(object instanceof Auditable)) {
			throw new FatalException(new ExceptionMessage("error.daomapper.auditable.invalidobject"),this, ExceptionCode.PERSISTENCE_ERROR);			
		}
		
		String username = securityService.getUsername();

		//Auditable auditable = getAuditableObjectProxy(object);
		((Auditable)object).setUpdatedBy(null != username ? username : this.defaultSystemUserName);
		
		return super.delete(object);
	}

	/* (non-Javadoc)
	 * @see com.apposit.service.data.ConcurrentSQLDomainObjectDAO#insert(com.apposit.service.data.DomainObject)
	 */
	@Override
	public DAOObject insert(DAOObject object) {

		if(object == null || !object.getClass().isAnnotationPresent(Audited.class) ) {

			return super.insert(object);
			//throw new FatalException(new ExceptionMessage("error.daomapper.auditable.invalidobject"),this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		if(!(object instanceof Auditable)) {
			throw new FatalException(new ExceptionMessage("error.daomapper.auditable.invalidobject"),this, ExceptionCode.PERSISTENCE_ERROR);			
		}
		
		String username = securityService.getUsername();

		//Auditable auditable = getAuditableObjectProxy(object);
		((Auditable)object).setCreatedBy(null != username ? username : this.defaultSystemUserName);
		((Auditable)object).setUpdatedBy(null != username ? username : this.defaultSystemUserName);
		
		return super.insert(object);
	}

	@Override
	public int[] insertBatch(List<DAOObject> objects) {

		if(objects == null || objects.size() == 0 || !objects.get(0).getClass().isAnnotationPresent(Audited.class) ) {

			return super.insertBatch(objects);
			//throw new FatalException(new ExceptionMessage("error.daomapper.auditable.invalidobject"),this, ExceptionCode.PERSISTENCE_ERROR);
		}

		if(!(objects.get(0) instanceof Auditable)) {
			throw new FatalException(new ExceptionMessage("error.daomapper.auditable.invalidobject"),this, ExceptionCode.PERSISTENCE_ERROR);
		}

		String username = securityService.getUsername();

		//Auditable auditable = getAuditableObjectProxy(object);

		for (DAOObject object: objects) {
			((Auditable)object).setCreatedBy(null != username ? username : this.defaultSystemUserName);
			((Auditable)object).setUpdatedBy(null != username ? username : this.defaultSystemUserName);
		}

		return super.insertBatch(objects);
	}

	/* (non-Javadoc)
	 * @see com.apposit.service.data.ConcurrentSQLDomainObjectDAO#update(com.apposit.service.data.DomainObject)
	 */
	@Override
	public int update(DAOObject object) {

		if(object == null || !object.getClass().isAnnotationPresent(Audited.class) ) {

			return super.update(object);
			//throw new FatalException(new ExceptionMessage("error.daomapper.auditable.invalidobject"),this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		if(!(object instanceof Auditable)) {
			throw new FatalException(new ExceptionMessage("error.daomapper.auditable.invalidobject"),this, ExceptionCode.PERSISTENCE_ERROR);			
		}
		
		String username = securityService.getUsername();

		//Auditable auditable = getAuditableObjectProxy(object);
		((Auditable)object).setUpdatedBy(null != username ? username : this.defaultSystemUserName);
		
		return super.update(object);
	}
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * Sets a default system user name which will be used when securityManager.getUserName is null
	 * @param defaultSystemUserName
	 */
	public void setDefaultSystemUserName(String defaultSystemUserName) {
		this.defaultSystemUserName = defaultSystemUserName;
	}
	
	protected Auditable getAuditableObjectProxy(DAOObject object){

		ProxyFactory factory = new ProxyFactory(Auditable.class, new AuditableObjectMixin(object));
		return (Auditable)factory.getProxy();	
		 
	}
}
