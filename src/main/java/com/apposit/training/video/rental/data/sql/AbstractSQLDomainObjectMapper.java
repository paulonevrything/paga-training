package com.apposit.training.video.rental.data.sql;

import com.apposit.training.video.rental.data.DAO;
import com.apposit.training.video.rental.data.DAOObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Eric Chijioke
 *
 */
public abstract class AbstractSQLDomainObjectMapper implements SQLDAOObjectMapper {

	protected DAO dao;
	
	public Class<? extends DAOObject> supports(){
		
		throw new UnsupportedOperationException(this.getClass().getName() + " - For dynamic mapper configuration, you must override the support() " +
				"method in this mapper to indicate which dao object is supported ");
	}

	@Override
	public PreparedStatement prepareBatchInsertStatement(List<DAOObject> objects, Connection connection ) throws SQLException {
		throw new UnsupportedOperationException("Batch insert operation is not supported for " + objects.getClass());
	}

	/* (non-Javadoc)
	 * @see com.apposit.hrms.service.data.SQLDomainObjectMapper#getDAO()
	 */
	@Override
	public DAO getDAO() {
		
		return dao;
	}

	/* (non-Javadoc)
	 * @see com.apposit.hrms.service.data.SQLDomainObjectMapper#setDAO(com.apposit.hrms.service.data.DAO)
	 */
	@Override
	public void setDAO(DAO dao) {
		
		this.dao = dao;
	}
}
