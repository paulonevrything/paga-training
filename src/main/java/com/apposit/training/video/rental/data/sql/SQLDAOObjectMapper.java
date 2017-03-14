package com.apposit.training.video.rental.data.sql;

import com.apposit.training.video.rental.data.DAO;
import com.apposit.training.video.rental.data.DAOObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * @author Eric Chijioke
 *
 */
public interface SQLDAOObjectMapper {

	public PreparedStatement prepareGetStatement(Class<?> clazz, Object id, Connection connection, Locale locale) throws SQLException;

	public PreparedStatement prepareFindStatement(DAOObject filter, Connection connection) throws SQLException;

	public PreparedStatement prepareUpdateStatement(DAOObject object, Connection connection) throws SQLException;

	public PreparedStatement prepareInsertStatement(DAOObject object, Connection connection) throws SQLException;

	public PreparedStatement prepareDeleteStatement(DAOObject object, Connection connection) throws SQLException;

	public PreparedStatement prepareBatchInsertStatement(List<DAOObject> objects, Connection connection) throws SQLException;

	public DAOObject mapRow(ResultSet resultSet, Locale locale) throws SQLException;
	
	public void setDAO(DAO dao);

	public DAO getDAO();
	
	public Class<? extends DAOObject> supports();
	
}
