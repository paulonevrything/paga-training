package com.apposit.training.video.rental.data;

import com.apposit.training.video.rental.data.auditing.Auditable;
import com.apposit.training.video.rental.data.sql.AbstractSQLDomainObjectMapper;
import com.apposit.training.video.rental.data.sql.SQLUtils;
import com.apposit.training.video.rental.exception.ExceptionCode;
import com.apposit.training.video.rental.exception.ExceptionMessage;
import com.apposit.training.video.rental.exception.FatalException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyDescriptor;
import java.sql.*;
import java.util.*;


/**
 Conventions:
  - Entity Object must have constuctor with no argument
  - Entity Object and Entity(Actual table name) must have the same name.
  - All stored procedure parameters name must be ordered ascending
  - The first column of all stored procedure result set must be the identity field(which should be the entity name +"Id")
 */
public class EntityMapper extends AbstractSQLDomainObjectMapper {

	private String packageName;
	
	private List<String> excludes;

	@Override
	public DAOObject mapRow(ResultSet resultSet, Locale locale) throws SQLException {
				
		
		String idName = resultSet.getMetaData().getColumnName(1);
		
		String entityName = idName.substring(0, idName.length() - 2);
		
		Object object = null;

		try{			

				object = Class.forName(packageName + "." + entityName ).getConstructor(new Class<?>[]{}).newInstance(new Object[]{});
			
		} catch(Exception e) {
			
			throw new FatalException(new ExceptionMessage("error.entityMapper.instantiateObject", e), this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		if(!(object instanceof DAOObject)) {
			throw new FatalException(new ExceptionMessage("error.entityMapper.invalidDAOObject"),this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		if(!(object instanceof Auditable)) {
			throw new FatalException(new ExceptionMessage("error.entityMapper.invalidAuditableObject"),this, ExceptionCode.PERSISTENCE_ERROR);
		}
								
		ResultSetMetaData metaData = resultSet.getMetaData();
		
		int columnCount  = metaData.getColumnCount();
		
		((DAOObject)object).setId((Integer)resultSet.getObject(idName));
		
		String columnName;

		try {
			for(int i = 1; i <= columnCount; ++i) {
					
				columnName = metaData.getColumnName(i);					
				//BeanUtils.setProperty(object, StringUtils.uncapitalize(columnName), resultSet.getObject(columnName));
				Object value = resultSet.getObject(columnName);
				
				/*if (columnName.equals((entityName + "Id"))){
					columnName = "id";
				} else if  (columnName.equals((entityName + "GUID"))){
					columnName = "guid";
				}*/
					
				PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(object, StringUtils.uncapitalize(columnName));
				
				if(descriptor!= null) {
					System.out.println(descriptor.getName() + descriptor.getPropertyType());
					BeanUtils.setProperty(object, StringUtils.uncapitalize(columnName), ( descriptor.getPropertyType().equals(java.util.Date.class) ? SQLUtils.toJavaDate(value) : value));
				}
				
			}
			
		} catch (Exception e) {
			throw new FatalException(new ExceptionMessage("error.daomapper.sql.invalidobject",e), this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		return ((DAOObject) object);
	}
		
	@Override
	public CallableStatement prepareGetStatement(Class<?> clazz, Object id, Connection connection, Locale locale)  throws SQLException {
		
		String entityName = clazz.getName().substring((clazz.getName() .lastIndexOf(".") + 1), clazz.getName() .length());
		
		CallableStatement statement = connection.prepareCall("{call spGet" + entityName + "(?)}");

		statement.setInt(1, (Integer) id);
		
		return statement;
	}
	
	@Override
	public CallableStatement prepareDeleteStatement(DAOObject object,
													Connection connection) throws SQLException {

		String entityName =  getEntityName(object);
		
		CallableStatement statement = connection.prepareCall("{call spDelete" + entityName + "(?)}");

		statement.setInt(1, (Integer)object.getId());
		
		return statement;
	}
	
	@Override
	public CallableStatement prepareFindStatement(DAOObject filter, Connection connection) throws SQLException {
	
		String entityName = getEntityName(filter);

		CallableStatement statement = connection.prepareCall("{call spFind" + entityName + "(" +  parameterMarkers((PropertyUtils.getPropertyDescriptors(filter).length) - 8) + ")}");

		int i =  1 ;
		
		try {
			PropertyDescriptor[] x = PropertyUtils.getPropertyDescriptors(filter);
		
			List<PropertyDescriptor> list = Arrays.asList(x); //Array to Collection

			Collections.sort(list,  new Comparator<PropertyDescriptor>(){

				@Override
				public int compare(PropertyDescriptor arg0, PropertyDescriptor arg1) {
					
					return arg0.getName().compareTo(arg1.getName());
				}
			
			});

			for (PropertyDescriptor descriptor : list) {
				
				String property = BeanUtils.getSimpleProperty(filter, descriptor.getName());
				
				if(! excludes.contains(descriptor.getName()) && property != null ) {
				
					if (descriptor.getPropertyType().equals(java.lang.Boolean.class)){
						statement.setString(i, property);
					} else{
						statement.setObject(i, property, SQLUtils.getSQLTypeFromClass(descriptor.getPropertyType()));
					}	
					i++;
				}
			}
			
		} catch (Exception e) {
			throw new FatalException(new ExceptionMessage("error.daomapper.sql.invalidobject",e), this, ExceptionCode.PERSISTENCE_ERROR);
		}

		return statement;
	}

	
	@Override
	public CallableStatement prepareInsertStatement(DAOObject object,
													Connection connection) throws SQLException {
		
		String entityName = getEntityName(object);
		
		CallableStatement statement = connection.prepareCall("{call spInsert" + entityName + "(" + parameterMarkers((PropertyUtils.getPropertyDescriptors(object).length) - 8) + ")}");
		
		int i =  1 ;
		try {
			PropertyDescriptor[] x = PropertyUtils.getPropertyDescriptors(object);
			
			List<PropertyDescriptor> list = Arrays.asList(x); //Array to Collection
			
			Collections.sort(list,  new Comparator<PropertyDescriptor>(){

				@Override
				public int compare(PropertyDescriptor arg0, PropertyDescriptor arg1) {
					
					return arg0.getName().compareTo(arg1.getName());
				}
			
			});
			
		for (PropertyDescriptor descriptor : list) {
			if(! excludes.contains(descriptor.getName()) ) {
				
				statement.setObject(i, 
						BeanUtils.getSimpleProperty(object, descriptor.getName()), 
							SQLUtils.getSQLTypeFromClass(descriptor.getPropertyType()));
				
				i++;
			}
		}
			
		} catch (Exception e) {
			throw new FatalException(new ExceptionMessage("error.daomapper.sql.invalidobject",e), this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		return statement;
	}

	@Override
	public CallableStatement prepareUpdateStatement(DAOObject object, Connection connection) throws SQLException {

		String entityName=getEntityName(object);
		
		CallableStatement statement = connection.prepareCall("{call spUpdate" + entityName + "(" +  parameterMarkers((PropertyUtils.getPropertyDescriptors(object).length) - 7) + ")}");
		
		int i =  1 ;
		try {
			PropertyDescriptor[] x = PropertyUtils.getPropertyDescriptors(object);
			
			List<PropertyDescriptor> list = Arrays.asList(x); //Array to Collection
			
			//list.add(new PropertyDescriptor(entityName + "Id", object.getClass()));
			PropertyDescriptor y = new PropertyDescriptor(StringUtils.uncapitalize(entityName) +"Id", object.getClass(),"getId","setId");
			PropertyDescriptor z = new PropertyDescriptor("id", object.getClass(),"getId","setId");
			
			list.set(list.indexOf(z), y);
			
			Collections.sort(list,  new Comparator<PropertyDescriptor>(){

				@Override
				public int compare(PropertyDescriptor arg0, PropertyDescriptor arg1) {
					
					return arg0.getName().compareTo(arg1.getName());
				}
			
			});

		for (PropertyDescriptor descriptor : list) {
			
			if(! excludes.contains(descriptor.getName()) ) {
				if(descriptor.getName().equals(StringUtils.uncapitalize(entityName) +"Id")){
					statement.setObject(i, "setId", Types.INTEGER);
				}
				else{
					System.out.println(BeanUtils.getSimpleProperty(object, descriptor.getName()) + " _ " +descriptor.getPropertyType() + " _ " + SQLUtils.getSQLTypeFromClass(descriptor.getPropertyType()));
					statement.setObject(i, 
							BeanUtils.getSimpleProperty(object, descriptor.getName()), 
								SQLUtils.getSQLTypeFromClass(descriptor.getPropertyType()));
				}
				
				i++;
			}
		}
			
		} catch (Exception e) {
			throw new FatalException(new ExceptionMessage("error.daomapper.sql.invalidobject",e), this, ExceptionCode.PERSISTENCE_ERROR);
		}
		
		
		return statement;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	private String parameterMarkers(int i){
		String x = "";
		for (int j = 0; j<i; j++){
			x = x + "?,";
		}
		return x.substring(0, (x.length() - 1));
	}

	private String getEntityName(DAOObject object){
		
		return object.getClass().getName().substring((object.getClass().getName().lastIndexOf(".") + 1), object.getClass().getName().length());
	}
	
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}
}
