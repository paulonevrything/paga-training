package com.apposit.training.video.rental.model.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;
import com.apposit.training.video.rental.data.DAOObject;
import com.apposit.training.video.rental.data.sql.AbstractSQLDomainObjectMapper;
import com.apposit.training.video.rental.model.VideoGenreEnum;
import com.apposit.training.video.rental.model.VideoTypeEnum;
import com.apposit.training.video.rental.model.Video;

public class VideoMapper extends AbstractSQLDomainObjectMapper {

	@Override
	public DAOObject mapRow(ResultSet resultSet, Locale locale) throws SQLException {

		Video video = new Video();
		video.setId(resultSet.getInt("VideoId"));

		video.setCreatedBy(resultSet.getString("CreatedBy"));
		video.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
		video.setImgUrl(resultSet.getString("ImgUrl"));
		video.setMaximumAge((Integer)resultSet.getObject("MaximumAge"));
		video.setTitle(resultSet.getString("Title"));
		video.setUpdatedBy(resultSet.getString("UpdatedBy"));
		video.setUpdatedDate(resultSet.getTimestamp("UpdatedDate"));
		video.setVideoGenreId(null != resultSet.getString("VideoGenreId") ? VideoGenreEnum.valueOf(resultSet.getString("VideoGenreId")) : null);
		video.setVideoTypeId(null != resultSet.getString("VideoTypeId") ? VideoTypeEnum.valueOf(resultSet.getString("VideoTypeId")) : null);
		video.setYearReleased((Integer)resultSet.getObject("YearReleased"));
		return video;
	}

	@Override
	public PreparedStatement prepareDeleteStatement(DAOObject object, Connection connection) throws SQLException {

		PreparedStatement statement = connection.prepareCall("{call spDeleteVideo(?)}");
		statement.setInt(1, (Integer)object.getId());
		return statement;
	}

	@Override
	public PreparedStatement prepareGetStatement(Class<?> clazz, Object id, Connection connection, Locale locale) throws SQLException {

		PreparedStatement statement = connection.prepareCall("{call spGetVideo(?)}");
		statement.setInt(1, (Integer)id);
		return statement;
	}

	@Override
	public PreparedStatement prepareInsertStatement(DAOObject object, Connection connection) throws SQLException {

		Video video = (Video)object;
		PreparedStatement statement = connection.prepareCall("{call spInsertVideo(?,?,?,?,?,?,?,?)}");
		statement.setString(1,(String)video.getCreatedBy());
		statement.setString(2,(String)video.getImgUrl());
		statement.setObject(3,(Integer)video.getMaximumAge(),Types.INTEGER);
		statement.setString(4,(String)video.getTitle());
		statement.setString(5,(String)video.getUpdatedBy());
		statement.setString(6,(video.getVideoGenreId() == null ? null : video.getVideoGenreId().name()));
		statement.setString(7,(video.getVideoTypeId() == null ? null : video.getVideoTypeId().name()));
		statement.setObject(8,(Integer)video.getYearReleased(),Types.INTEGER);
		return statement;
	}

	@Override
	public PreparedStatement prepareFindStatement(DAOObject object, Connection connection) throws SQLException {

		Video video = (Video)object;
		PreparedStatement statement = connection.prepareCall("{call spFindVideo(?,?,?,?,?,?,?,?)}");
		statement.setString(1,(String)video.getCreatedBy());
		statement.setString(2,(String)video.getImgUrl());
		statement.setObject(3,(Integer)video.getMaximumAge(),Types.INTEGER);
		statement.setString(4,(String)video.getTitle());
		statement.setString(5,(String)video.getUpdatedBy());
		statement.setObject(6,(video.getVideoGenreId() == null ? null : video.getVideoGenreId().name()),Types.VARCHAR);
		statement.setObject(7,(video.getVideoTypeId() == null ? null : video.getVideoTypeId().name()),Types.VARCHAR);
		statement.setObject(8,(Integer)video.getYearReleased(),Types.INTEGER);
		return statement;
	}

	@Override
	public PreparedStatement prepareUpdateStatement(DAOObject object, Connection connection) throws SQLException {

		Video video = (Video)object;
		PreparedStatement statement = connection.prepareCall("{call spUpdateVideo(?,?,?,?,?,?,?,?)}");
		statement.setString(1,(String)video.getImgUrl());
		statement.setObject(2,(Integer)video.getMaximumAge(),Types.INTEGER);
		statement.setString(3,(String)video.getTitle());
		statement.setString(4,(String)video.getUpdatedBy());
		statement.setString(5,(video.getVideoGenreId() == null ? null : video.getVideoGenreId().name()));
		statement.setInt(6,(Integer)video.getId());
		statement.setString(7,(video.getVideoTypeId() == null ? null : video.getVideoTypeId().name()));
		statement.setObject(8,(Integer)video.getYearReleased(),Types.INTEGER);
		return statement;
	}

	@Override
	public Class<? extends DAOObject> supports() {

		return Video.class;
	}

}



