package com.apposit.training.video.rental.data;

import com.apposit.training.video.rental.exception.ExceptionCode;
import com.apposit.training.video.rental.exception.ExceptionMessage;
import com.apposit.training.video.rental.exception.FatalException;

import java.util.*;

public abstract class DAODecorator implements DAO {

	protected DAO dao;
	
	public DAODecorator(DAO dao) {

		if(dao == null) {
			
			throw new FatalException(new ExceptionMessage("error.daodecorator.nulldao"),this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		this.dao = dao;
	}
	
	@Override
	public int delete(DAOObject object) {
		return dao.delete(object);
	}

	@Override
	public List<DAOObject> find(DAOObject filter) {
		return dao.find(filter);
	}
	
	@Override
	public DAOObject findOne(DAOObject filter) {
		return dao.findOne(filter);
	}

	@Override
	public DAOObject get(Class<?> clazz, Object id, Locale locale) {
		return dao.get(clazz, id, locale);
	}

	@Override
	public DAOObject insert(DAOObject object) {
		return dao.insert(object);
	}

	@Override
	public int[] insertBatch(List<DAOObject> objects) {return dao.insertBatch(objects);}

	@Override
	public int update(DAOObject object) {
		return dao.update(object);
	}

	@Override
	public Double getDouble(String query, Object[] parameters) {
		return dao.getDouble(query, parameters);
	}

	@Override
	public Integer getInteger(String query, Object[] parameters) {
		return dao.getInteger(query, parameters);
	}

	@Override
	public List<Object> getList(String query, Object[] parameters) {
		return dao.getList(query, parameters);
	}

	@Override
	public List<Object> getTransposedList(String query, Object[] parameters) {
		return dao.getTransposedList(query, parameters);
	}

	@Override
	public Long getLong(String query, Object[] parameters) {
		return dao.getLong(query, parameters);
	}

	@Override
	public Boolean getBoolean(String query, Object[] parameters) {
		return dao.getBoolean(query, parameters);
	}

	@Override
	public Date getDate(String query, Object[] parameters) {
		return dao.getDate(query, parameters);
	}

	@Override
	public byte[] getBlob(String query, Object[] parameters) {
		return dao.getBlob(query, parameters);
	}
	
	@Override
	public String getXML(String query, Object[] parameters) {

		return dao.getXML(query, parameters);
	}

	@Override
	public Map<Object, Object> getMap(String query, Object[] parameters) {
		return dao.getMap(query, parameters);
	}

	@Override
	public List<Map<String, Object>> getMapList(String query,
												Object[] parameters) {
		return dao.getMapList(query, parameters);
	}

	@Override
	public Collection<Map<String, Object>> getMapCollection(String query,
															Object[] parameters) {
		return dao.getMapCollection(query, parameters);
	}

	@Override
	public String getString(String query, Object[] parameters) {
		return dao.getString(query, parameters);
	}

	@Override
	public int executeStatement(String query, Object[] parameters) {
		return dao.executeStatement(query, parameters);
	}

	@Override
	public List<Collection<Map<String,Object>>> getMapCollectionList(String query, Object[] parameters) {
		return dao.getMapCollectionList(query, parameters);
	}

	@Override
	public String getImplementationName() {
		return dao.getImplementationName();
	}
}
