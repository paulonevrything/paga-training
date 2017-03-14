package com.apposit.training.video.rental.model.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Locale;
import com.apposit.training.video.rental.data.DAOObject;
import com.apposit.training.video.rental.data.sql.AbstractSQLDomainObjectMapper;
import com.apposit.training.video.rental.model.User;

public class UserMapper extends AbstractSQLDomainObjectMapper {

	@Override
	public DAOObject mapRow(ResultSet resultSet, Locale locale) throws SQLException {

		User user = new User();
		user.setId(resultSet.getInt("UserId"));

		user.setCreatedBy(resultSet.getString("CreatedBy"));
		user.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
		user.setFirstName(resultSet.getString("FirstName"));
		user.setLastName(resultSet.getString("LastName"));
		user.setUpdatedBy(resultSet.getString("UpdatedBy"));
		user.setUpdatedDate(resultSet.getTimestamp("UpdatedDate"));
		user.setUsername(resultSet.getString("Username"));
		return user;
	}

	@Override
	public PreparedStatement prepareDeleteStatement(DAOObject object, Connection connection) throws SQLException {

		PreparedStatement statement = connection.prepareCall("{call spDeleteUser(?)}");
		statement.setInt(1, (Integer)object.getId());
		return statement;
	}

	@Override
	public PreparedStatement prepareGetStatement(Class<?> clazz, Object id, Connection connection, Locale locale) throws SQLException {

		PreparedStatement statement = connection.prepareCall("{call spGetUser(?)}");
		statement.setInt(1, (Integer)id);
		return statement;
	}

	@Override
	public PreparedStatement prepareInsertStatement(DAOObject object, Connection connection) throws SQLException {

		User user = (User)object;
		PreparedStatement statement = connection.prepareCall("{call spInsertUser(?,?,?,?,?)}");
		statement.setString(1,(String)user.getCreatedBy());
		statement.setString(2,(String)user.getFirstName());
		statement.setString(3,(String)user.getLastName());
		statement.setString(4,(String)user.getUpdatedBy());
		statement.setString(5,(String)user.getUsername());
		return statement;
	}

	@Override
	public PreparedStatement prepareFindStatement(DAOObject object, Connection connection) throws SQLException {

		User user = (User)object;
		PreparedStatement statement = connection.prepareCall("{call spFindUser(?,?,?,?,?)}");
		statement.setString(1,(String)user.getCreatedBy());
		statement.setString(2,(String)user.getFirstName());
		statement.setString(3,(String)user.getLastName());
		statement.setString(4,(String)user.getUpdatedBy());
		statement.setString(5,(String)user.getUsername());
		return statement;
	}

	@Override
	public PreparedStatement prepareUpdateStatement(DAOObject object, Connection connection) throws SQLException {

		User user = (User)object;
		PreparedStatement statement = connection.prepareCall("{call spUpdateUser(?,?,?,?,?)}");
		statement.setString(1,(String)user.getFirstName());
		statement.setString(2,(String)user.getLastName());
		statement.setString(3,(String)user.getUpdatedBy());
		statement.setInt(4,(Integer)user.getId());
		statement.setString(5,(String)user.getUsername());
		return statement;
	}

	@Override
	public Class<? extends DAOObject> supports() {

		return User.class;
	}

}



