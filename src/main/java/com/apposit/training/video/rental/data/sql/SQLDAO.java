	package com.apposit.training.video.rental.data.sql;

    import com.apposit.training.video.rental.data.DAO;
    import com.apposit.training.video.rental.data.DAOObject;
    import com.apposit.training.video.rental.exception.ExceptionCode;
    import com.apposit.training.video.rental.exception.ExceptionMessage;
    import com.apposit.training.video.rental.exception.FatalException;
    import org.apache.commons.lang.builder.ReflectionToStringBuilder;
    import org.apache.commons.logging.Log;
    import org.apache.commons.logging.LogFactory;
    import org.springframework.beans.factory.config.BeanDefinition;
    import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
    import org.springframework.core.type.filter.AssignableTypeFilter;
    import org.springframework.dao.DataAccessException;
    import org.springframework.dao.DeadlockLoserDataAccessException;
    import org.springframework.jdbc.datasource.DataSourceUtils;
    import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

    import javax.sql.DataSource;
    import java.sql.*;
    import java.util.*;
    import java.util.Date;

	/**
     * This class implements the {@link DAO} interface for to manage data access to a relational database. The class
     * expects {@link SQLDAOObjectMapper} classes to be registered with it and it delegates the details of database
     * manipulation for each {@link DAOObject} type to the appropriate mapper.
     *
     * @author Eric Chijioke
     *
     */
    public class SQLDAO implements DAO {

        private static final Log logger = LogFactory.getLog(SQLDAO.class);

        protected DataSource dataSource;

        protected Map<Class<?>, SQLDAOObjectMapper> mappers;

        private ClassPathScanningCandidateComponentProvider classScanningProvider;

        private SQLErrorCodeSQLExceptionTranslator sqlErrorTranslator;

        public SQLDAO() {
            super();
            mappers = new Hashtable<Class<?>, SQLDAOObjectMapper>();

            classScanningProvider = new ClassPathScanningCandidateComponentProvider(true);
            classScanningProvider.addIncludeFilter(new AssignableTypeFilter(SQLDAOObjectMapper.class));
        }

        @Override
        public String getImplementationName() {
            return "SQLDAO";
        }

        public void setMappersByPackageName(List<String> packages) {

            try{

                for (String packageName : packages) {

                    Set<BeanDefinition> components = classScanningProvider.findCandidateComponents(packageName);

                    for (BeanDefinition component : components) {

                        // Note: DomainObjectMappers set this way must have no-arg constructor
                        SQLDAOObjectMapper mapper = (SQLDAOObjectMapper) Class.forName(component.getBeanClassName()).getConstructor(new Class<?>[]{}).newInstance(new Object[]{});
                        mapper.setDAO(this);

                        this.mappers.put(mapper.supports(), mapper );
                    }
                }

            } catch(Exception e) {

                throw new FatalException(new ExceptionMessage("error.daomapper.register", e), this, ExceptionCode.PERSISTENCE_ERROR);
            }

        }

        /**
         * @param mappers the mappers to set
         */
        public void setMappersByName(Map<String, String> mappers) {

            if(this.mappers == null) { this.mappers = new Hashtable<Class<?>, SQLDAOObjectMapper>(mappers.size()); }

            try{

                for (String objectClass : mappers.keySet()) {

                    // Note: DomainObjectMappers set this way must have no-arg constructor
                    SQLDAOObjectMapper mapper = (SQLDAOObjectMapper) Class.forName(mappers.get(objectClass)).getConstructor(new Class<?>[]{}).newInstance(new Object[]{});

                    mapper.setDAO(this);

                    this.mappers.put(Class.forName(objectClass), mapper );

                }

            } catch(Exception e) {

                throw new FatalException(new ExceptionMessage("error.daomapper.register", e), this, ExceptionCode.PERSISTENCE_ERROR);
            }
        }

        /**
         * @param mappers the mappers to set
         */
        public void setMappers(Map<String, SQLDAOObjectMapper> mappers) {

            if(this.mappers == null) { this.mappers = new Hashtable<Class<?>, SQLDAOObjectMapper>(mappers.size()); }

            try{

                for (String objectClass : mappers.keySet()) {

                    SQLDAOObjectMapper mapper = mappers.get(objectClass);

                    mapper.setDAO(this);

                    this.mappers.put(Class.forName(objectClass), mapper);

                }

            } catch(Exception e) {

                throw new FatalException(new ExceptionMessage("error.daomapper.register", e), this, ExceptionCode.PERSISTENCE_ERROR);
            }
        }

        public void setMapper(String className, SQLDAOObjectMapper mapper) {

            if(this.mappers == null) { this.mappers = new Hashtable<Class<?>, SQLDAOObjectMapper>(mappers.size()); }

            try{

                mapper.setDAO(this);

                this.mappers.put( Class.forName(className), mapper);


            } catch(Exception e) {

                throw new FatalException(new ExceptionMessage("error.daomapper.register", e), this, ExceptionCode.PERSISTENCE_ERROR);
            }
        }

        @Override

        public int delete(DAOObject object) {

            if(object == null) {

                throw new FatalException(new ExceptionMessage("error.daomapper.delete.nullobject"),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            DAOObject idObject = (DAOObject)object;

            if(idObject.isReadOnly()){

                throw new FatalException(new ExceptionMessage("error.daomapper.delete.readonlyobject", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);

                SQLDAOObjectMapper mapper = getMapper(idObject.getClass());

                if(mapper == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.unregistered", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                statement = mapper.prepareDeleteStatement(idObject, connection);

                if(statement == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.delete.nullstatement", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                return statement.executeUpdate();

            } catch(SQLException se) {

                throw handleSQLException(connection, se, object);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);

            }
        }

        @Override

        public List<DAOObject> find(DAOObject filter) {

            if(filter == null) {

                throw new FatalException(new ExceptionMessage("error.daomapper.find.nullfilter"),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            DAOObject idObject = (DAOObject)filter;

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);

                SQLDAOObjectMapper mapper = getMapper(idObject.getClass());

                if(mapper == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.unregistered", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                statement = mapper.prepareFindStatement(idObject, connection);

                if(statement == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.find.nullstatement", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                ResultSet rs = statement.executeQuery();

                List<DAOObject> objects = new ArrayList<DAOObject>();

                if(rs != null) {

                    while(rs.next()) {

                        objects.add(mapper.mapRow(rs, idObject.getLocale()));
                    }
                }

                return objects;

            } catch(SQLException se) {

                 throw handleSQLException(connection, se, filter);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override
        public DAOObject findOne(DAOObject filter) {

            List<DAOObject> results = find(filter);

            return results.isEmpty() ? null : results.get(0);
        }

        @Override

        public DAOObject get(Class<?> clazz, Object id, Locale locale) {

            if(clazz == null) {

                throw new FatalException(new ExceptionMessage("error.daomapper.get.nullclass"),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            if(id == null) {

                throw new FatalException(new ExceptionMessage("error.daomapper.get.nullid"),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);

                SQLDAOObjectMapper mapper = getMapper(clazz);

                if(mapper == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.unregistered", clazz.toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                statement = mapper.prepareGetStatement(clazz, id, connection, locale);

                if(statement == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.get.nullstatement", clazz.toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                ResultSet rs = statement.executeQuery();

                DAOObject object;

                if(rs != null && rs.next()) {

                    object = mapper.mapRow(rs, locale);

                } else {

                    //TODO: should i throw exception here
                    return null;

                }

                return object;

            } catch(SQLException se) {

                throw handleSQLException(connection, se, id);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        /**
         * Note: This implementation depends on the insert statement returning the primary key id of the entity just inserted !!!
         */
        @SuppressWarnings("unchecked")
        @Override

        public DAOObject insert(DAOObject object) {

            if(object == null) {

                throw new FatalException(new ExceptionMessage("error.daomapper.insert.nullobject"),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            DAOObject idObject = (DAOObject)object;

            if(idObject.isReadOnly()){

                throw new FatalException(new ExceptionMessage("error.daomapper.insert.readonlyobject", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);

                SQLDAOObjectMapper mapper = getMapper(idObject.getClass());

                if(mapper == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.unregistered", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                statement = mapper.prepareInsertStatement(idObject, connection);

                if(statement == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.insert.nullstatement", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                //ResultSet rs = statement.executeQuery();

                boolean hasResultSet = statement.execute();

                if(hasResultSet) {

                    ResultSet rs = statement.getResultSet();

                    if(rs != null && rs.next()) {

                        Integer id = rs.getInt(1);

                        if(id != null) {

                            statement.close();

                            return get((Class<DAOObject>)idObject.getClass(), id , idObject.getLocale());
                        }
                    }
                }

                return null;

                //Can't use identifier.getIdQuery() event though tied (returns unique for) to current connection because trigerrs create a new identity in Audit table
                //different from last inseted entity id

                //Can't use getInteger(...) becuase this will use a new connection which will not guarantee returning the last identity
                //unless the entire operation is being performeed within a single business transaction

            } catch(SQLException se) {

                 throw handleSQLException(connection, se, object);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override
        public int[] insertBatch(List<DAOObject> objects) {

            if (objects == null || objects.size() == 0) {

                throw new FatalException(new ExceptionMessage("error.daomapper.insert.nullobject"), this, ExceptionCode.PERSISTENCE_ERROR);
            }

            Connection connection = null;
            Statement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);

                SQLDAOObjectMapper mapper = getMapper(objects.get(0).getClass());

                if (mapper == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.unregistered", objects.get(0).getClass().toString()), this, ExceptionCode.PERSISTENCE_ERROR);
                }

                statement = mapper.prepareBatchInsertStatement(objects, connection);

                if (statement == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.insert.nullstatement", objects.get(0).getClass().toString()), this, ExceptionCode.PERSISTENCE_ERROR);
                }

                int[] updateCounts = statement.executeBatch();

                statement.close();

                return updateCounts;

            } catch (SQLException se) {

                throw handleSQLException(connection, se, objects);

            } finally {

                try {
                    if (statement != null) statement.close();
                } catch (SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }

        }

        @Override

        public int update(DAOObject object) {

            if(object == null) {

                throw new FatalException(new ExceptionMessage("error.daomapper.update.nullobject"),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            DAOObject idObject = (DAOObject)object;

            if(idObject.isReadOnly()){

                throw new FatalException(new ExceptionMessage("error.daomapper.update.readonlyobject", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
            }

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);

                SQLDAOObjectMapper mapper = getMapper(idObject.getClass());

                if(mapper == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.unregistered", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                statement = mapper.prepareUpdateStatement(idObject, connection);

                if(statement == null) {

                    throw new FatalException(new ExceptionMessage("error.daomapper.update.nullstatement", idObject.getClass().toString()),this, ExceptionCode.PERSISTENCE_ERROR);
                }

                return statement.executeUpdate();


            } catch(SQLException se) {

                throw handleSQLException(connection, se, object);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public Double getDouble(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    return rs.getDouble(1);

                } else {

                    return null;
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public Integer getInteger(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    return rs.getInt(1);

                } else {

                    return null;
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public Long getLong(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    return rs.getLong(1);

                } else {

                    return null;
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }


        @Override

        public Date getDate(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    return rs.getDate(1);

                } else {

                    return null;
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public Boolean getBoolean(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    return rs.getBoolean(1);

                } else {

                    return null;
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }


        @Override

        public byte[] getBlob(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    try
                    {
                       // Get as a BLOB
                       Blob aBlob = rs.getBlob(1);
                       return aBlob.getBytes(1, (int) aBlob.length());

                    } catch(Exception ex) {

                       // The driver could not handle this as a BLOB...
                       // Fallback to default (and slower) byte[] handling
                       return rs.getBytes(1);
                    }

                } else {

                    return new byte[]{};
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public String getString(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    return rs.getString(1);

                } else {

                    return null;
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override
        public String getXML(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.next()){

                    SQLXML xml = rs.getSQLXML(1);

                    return xml == null ? null : xml.getString();

                } else {

                    return null;
                }

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public List<Object> getList(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.getMetaData().getColumnCount() < 1) {

                    return new ArrayList<Object>();
                }

                List<Object> list = new ArrayList<Object>();

                if(rs != null){

                    while(rs.next()) {

                        list.add(rs.getObject(1));
                    }

                }

                return list;

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public List<Object> getTransposedList(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                List<Object> list = new ArrayList<Object>();

                ResultSet rs = statement.executeQuery();

                if(rs != null){

                    if(rs.next()) {

                    }

                    int columns = rs.getMetaData().getColumnCount();

                    for (int i = 0; i < columns; i++) {

                        list.add(rs.getObject(i+1));
                    }
                }

                return list;

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public Map<Object,Object> getMap(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                if(rs.getMetaData().getColumnCount() < 2) {

                    return new HashMap<Object,Object>();
                }

                Map<Object,Object> map = new LinkedHashMap<Object,Object>();

                if(rs != null){

                    while(rs.next()) {

                        map.put(rs.getObject(1),rs.getObject(2));
                    }

                }

                return map;

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public Collection<Map<String,Object>> getMapCollection(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                return buildMapList(rs);

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public List<Map<String,Object>> getMapList(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                ResultSet rs = statement.executeQuery();

                return buildMapList(rs);

            } catch(SQLException se) {

                    throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public List<Collection<Map<String,Object>>> getMapCollectionList(String query, Object[] parameters) {

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet rs = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                List<Collection<Map<String,Object>>> resultSet = new ArrayList<Collection<Map<String, Object>>>();

                if(statement.execute()) {

                    do{

                        rs = statement.getResultSet();

                        resultSet.add(buildMapList(rs));

                    }while(statement.getMoreResults());

                }

                return resultSet;

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        @Override

        public int executeStatement(String query, Object[] parameters){

            Connection connection = null;
            PreparedStatement statement = null;

            try {

                connection = DataSourceUtils.getConnection(dataSource);
                statement = connection.prepareStatement(query);

                setStatementParameters(statement, parameters);

                return statement.executeUpdate();

            } catch(SQLException se) {

                throw handleSQLException(connection, se, query, parameters);

            } finally {

                try{ if (statement != null) statement.close(); }
                catch(SQLException e) { /* TODO log warning */}

                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }

        private List<Map<String, Object>> buildMapList(ResultSet rs) throws SQLException {

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Map<String, Object> row;

            if(rs != null) {

                ResultSetMetaData meta = rs.getMetaData();

                while (rs.next()) {

                    row = new LinkedHashMap<String, Object>();  //Must be HashMap or similar to allow null entries

                    for (int i = 1; i <= meta.getColumnCount(); ++i) {

                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }

                    list.add(row);
                }
            }

            return list;
        }

        protected void setStatementParameters(PreparedStatement statement, Object[] parameters) throws SQLException {

            if(parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    if(parameters[i] == null){
                        statement.setObject(i+1, null, Types.JAVA_OBJECT);
                    } else if(parameters[i] instanceof Date) {
                        statement.setTimestamp(i+1, new Timestamp(((Date)parameters[i]).getTime()));
                    } else {
                        statement.setObject(i+1, parameters[i]);
                    }
                }
            }
        }

        protected SQLDAOObjectMapper getMapper(Class<?> clazz){

            return mappers.get(clazz);
        }

        protected RuntimeException handleSQLException(Connection connection, SQLException se, Object object) {

            DataAccessException dae = sqlErrorTranslator.translate("SQLDAO operation", null, se);

            if(dae instanceof DeadlockLoserDataAccessException) {

                if(logger.isDebugEnabled()) {

                    logger.debug("Deadlock exception caught operating on object: " + ReflectionToStringBuilder.toString(object), se);
                }

                return new FatalException(new ExceptionMessage("error.sqldaomapper", se), this, ExceptionCode.PERSISTENCE_DEADLOCK_ERROR);
            }

            return new FatalException(new ExceptionMessage("error.sqldaomapper", se), this, ExceptionCode.PERSISTENCE_ERROR);
        }


        protected RuntimeException handleSQLException(Connection connection, SQLException se, String statement, Object[] parameters) {

            DataAccessException dae = sqlErrorTranslator.translate("SQLDAO operation", null, se);

            if(dae instanceof DeadlockLoserDataAccessException) {

                logger.debug("Deadlock exception caught executing statement: " + statement + "[" + Arrays.toString(parameters) + "]", se);

                return new FatalException(new ExceptionMessage("error.sqldaomapper", se), this, ExceptionCode.PERSISTENCE_DEADLOCK_ERROR);
            }

            return new FatalException(new ExceptionMessage("error.sqldaomapper", se), this, ExceptionCode.PERSISTENCE_ERROR);
        }

        /**
         * @param dataSource the dataSource to set
         */
        public void setDataSource(DataSource dataSource) {

            this.dataSource = dataSource;

            sqlErrorTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
        }

    }
