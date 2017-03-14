
package com.apposit.training.video.rental.data;

import java.util.*;

/**
 * @author Eric Chijioke
 *
 */
public interface DAO {
    
	/**
	 * Find one or more domain objects using the provided object as a filter
	 * @param filter A filter to find domain objects, the non-filtering properties should be null
	 * @return
	 */
	public List<DAOObject> find(DAOObject filter);

	/**
	 * Find only one domain objects using the provided object as a filter, or null if none match
	 * @param filter A filter to find domain objects, the non-filtering properties should be null
	 * @return
	 */
	public DAOObject findOne(DAOObject filter);
	
	/**
	 * Select a single domain object with the provided if
	 * @param filter The id of the object to find
	 * @return
	 */
	public DAOObject get(Class<?> clazz, Object id, Locale locale);

	/**
	 * Insert a new domain object
	 * @param object
	 * @return The number of records affected by the update
	 */
	public int update(DAOObject object);

	/**
	 * Delete an existing domain object
	 * @param object
	 * @return True if deleted successfully, false if non-exception failure
	 */
	public int delete(DAOObject object);
	
	/**
	 * Insert a new domain object
	 * @param object
	 * @return The newly inserted DomainObject with's id and GUID set
	 */
	public DAOObject insert(DAOObject object);

	/**
	 *
	 * @param objects
	 * @return
     */
	public int[] insertBatch(List<DAOObject> objects);

	/** utility methods **/
	
	/**
	 * This utility method returns a double based on the query and parameters provided 
	 */
	public Double getDouble(String query, Object[] parameters);

	/**
	 * This utility method returns an integer based on the query and parameters provided 
	 */
	public Integer getInteger(String query, Object[] parameters);

	/**
	 * This utility method returns a long based on the query and parameters provided 
	 */
	public Long getLong(String query, Object[] parameters);

	/**
	 * This utility method returns a string based on the query and parameters provided 
	 */
	public String getString(String query, Object[] parameters);

	/**
	 * This utility method returns a boolean based on the query and parameters provided 
	 */
	public Boolean getBoolean(String query, Object[] parameters);

	/**
	 * This utility method returns a date based on the query and parameters provided 
	 */
	public Date getDate(String query, Object[] parameters);

	/**
	 * This utility method returns a large binary array based on the query and parameters provided 
	 */
	public byte[] getBlob(String query, Object[] parameters);

	/**
	 * This utility method returns a large binary array based on the query and parameters provided 
	 */
	public String getXML(String query, Object[] parameters);

	/**
	 * This utility method returns a list based on the first column in the result set based on the query and parameters provided.
	 * The result set must have at least one column or an exception will be thrown.
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Object> getList(String query, Object[] parameters);

	/**
	 * This utility method returns a list based on the first row in the result set based on the query and parameters provided.
	 * The values in the columns of the first row are returned (in order) as the values in the list
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Object> getTransposedList(String query, Object[] parameters);

	/**
	 * This utility method returns a map based on the values of the first two columns returned from the result set based on the query and parameters provided.
	 * The first column provides the map key, the second column provides the map value 
	 * The result set must have at least two columns or an exception will be thrown.
	 * @param sql
	 * @param values
	 * @return
	 */
	public Map<Object, Object> getMap(String query, Object[] parameters);

	/**
	 * This utility method returns a collection representation of the result set based on the query and parameters provided. 
	 * Each entry in the list represents a row in the result set and contains a map of column-name, column-value pairs.
	 * @param sql
	 * @param values
	 * @return
	 */
	public Collection<Map<String, Object>> getMapCollection(String query, Object[] parameters);

	/**
	 * This utility method returns a list representation of the result set based on the query and parameters provided. in the result-set order 
	 * Each entry in the list represents a row in the result set and contains a map of column-name, column-value pairs.
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Map<String, Object>> getMapList(String query, Object[] parameters);
	
	/**
	 * This utility method returns a list of collection representations of the the result set based on the query and parameters provided. 
	 * Each entry in the top list represents a different result set. each entry in the inner list
	 * represents a row in the result set and contains a map of column-name, column-value pairs.
	 * @param query
	 * @param parameters
	 * @return
	 */
	public List<Collection<Map<String,Object>>> getMapCollectionList(String query, Object[] parameters);
	
	/**
	 * Executes a statement on the data source that does not expect any return values
	 * @param query
	 * @param parameters
	 * @return
	 */
	public int executeStatement(String query, Object[] parameters);
	
	/**
	 * Return the given name of the DAO implementation.
	 * DAAO decorators should not change the name returned without a VERY good reason.
	 * @return
	 */
	public String getImplementationName();
}
