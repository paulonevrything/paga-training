package com.apposit.training.video.rental.model.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;
import com.apposit.training.video.rental.data.DAOObject;
import com.apposit.training.video.rental.data.sql.AbstractSQLDomainObjectMapper;
import com.apposit.training.video.rental.model.VideoTypeEnum;
import com.apposit.training.video.rental.model.RentPricing;

public class RentPricingMapper extends AbstractSQLDomainObjectMapper {

	@Override
	public DAOObject mapRow(ResultSet resultSet, Locale locale) throws SQLException {

		RentPricing rentPricing = new RentPricing();
		rentPricing.setId(resultSet.getInt("RentPricingId"));

		rentPricing.setCreatedBy(resultSet.getString("CreatedBy"));
		rentPricing.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
		rentPricing.setDailyRate(resultSet.getDouble("DailyRate"));
		rentPricing.setUpdatedBy(resultSet.getString("UpdatedBy"));
		rentPricing.setUpdatedDate(resultSet.getTimestamp("UpdatedDate"));
		rentPricing.setVideoTypeId(null != resultSet.getString("VideoTypeId") ? VideoTypeEnum.valueOf(resultSet.getString("VideoTypeId")) : null);
		return rentPricing;
	}

	@Override
	public PreparedStatement prepareDeleteStatement(DAOObject object, Connection connection) throws SQLException {

		PreparedStatement statement = connection.prepareCall("{call spDeleteRentPricing(?)}");
		statement.setInt(1, (Integer)object.getId());
		return statement;
	}

	@Override
	public PreparedStatement prepareGetStatement(Class<?> clazz, Object id, Connection connection, Locale locale) throws SQLException {

		PreparedStatement statement = connection.prepareCall("{call spGetRentPricing(?)}");
		statement.setInt(1, (Integer)id);
		return statement;
	}

	@Override
	public PreparedStatement prepareInsertStatement(DAOObject object, Connection connection) throws SQLException {

		RentPricing rentPricing = (RentPricing)object;
		PreparedStatement statement = connection.prepareCall("{call spInsertRentPricing(?,?,?,?)}");
		statement.setString(1,(String)rentPricing.getCreatedBy());
		statement.setDouble(2,(Double)rentPricing.getDailyRate());
		statement.setString(3,(String)rentPricing.getUpdatedBy());
		statement.setString(4,(rentPricing.getVideoTypeId() == null ? null : rentPricing.getVideoTypeId().name()));
		return statement;
	}

	@Override
	public PreparedStatement prepareFindStatement(DAOObject object, Connection connection) throws SQLException {

		RentPricing rentPricing = (RentPricing)object;
		PreparedStatement statement = connection.prepareCall("{call spFindRentPricing(?,?,?,?)}");
		statement.setString(1,(String)rentPricing.getCreatedBy());
		statement.setObject(2,(Double)rentPricing.getDailyRate(),Types.DECIMAL);
		statement.setString(3,(String)rentPricing.getUpdatedBy());
		statement.setObject(4,(rentPricing.getVideoTypeId() == null ? null : rentPricing.getVideoTypeId().name()),Types.VARCHAR);
		return statement;
	}

	@Override
	public PreparedStatement prepareUpdateStatement(DAOObject object, Connection connection) throws SQLException {

		RentPricing rentPricing = (RentPricing)object;
		PreparedStatement statement = connection.prepareCall("{call spUpdateRentPricing(?,?,?,?)}");
		statement.setDouble(1,(Double)rentPricing.getDailyRate());
		statement.setInt(2,(Integer)rentPricing.getId());
		statement.setString(3,(String)rentPricing.getUpdatedBy());
		statement.setString(4,(rentPricing.getVideoTypeId() == null ? null : rentPricing.getVideoTypeId().name()));
		return statement;
	}

	@Override
	public Class<? extends DAOObject> supports() {

		return RentPricing.class;
	}

}



