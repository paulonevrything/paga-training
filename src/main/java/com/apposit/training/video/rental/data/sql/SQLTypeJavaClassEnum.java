package com.apposit.training.video.rental.data.sql;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum SQLTypeJavaClassEnum {

	TINYINT(-6, java.lang.Integer.class),
	INTEGER(4, java.lang.Integer.class),

	BINARY(-2, java.lang.Byte.class),
	VARBINARY(-3, java.lang.Byte.class),
	LONGVARBINARY(-4, java.lang.Byte.class),

	REAL(7, java.lang.Double.class),
	DOUBLE(8, java.lang.Double.class),
	NUMERIC(2, java.lang.Double.class),
	DECIMAL(3, java.math.BigDecimal.class),

	VARCHAR(12, java.lang.String.class),
	LONGVARCHAR(-1, java.lang.String.class),
	ROWID(-8, java.lang.String.class),
	NCHAR(-15, java.lang.String.class),
	NVARCHAR(-9, java.lang.String.class),
	LONGNVARCHAR(-16, java.lang.String.class),

	BOOLEAN(16, java.lang.Boolean.class),
	BIT(-7, java.lang.Boolean.class),

	BIGINT(-5, java.lang.Long.class),
	FLOAT(6, java.lang.Float.class),
	CHAR(1, java.lang.Character.class),
	SMALLINT(5, java.lang.Short.class),
	
	DATE(91, java.sql.Date.class),
	TIME(92, java.sql.Time.class),
	TIMESTAMP(93, java.sql.Timestamp.class),
	
	NULL(0, null);
	
	/*OTHER(1111, FieldType.UNSUPPORTED),
	JAVA_OBJECT(2000, FieldType.UNSUPPORTED),
	DISTINCT(2001, FieldType.UNSUPPORTED),
	STRUCT(2002, FieldType.UNSUPPORTED),
	ARRAY(2003, FieldType.UNSUPPORTED),
	BLOB(2004, FieldType.UNSUPPORTED),
	CLOB(2005, FieldType.UNSUPPORTED),
	REF(2006, FieldType.UNSUPPORTED),
	DATALINK(70, FieldType.UNSUPPORTED),
	/*NCLOB(2011, FieldType.UNSUPPORTED),
	SQLXML(2009, FieldType.XML);*/

	private Integer sqlType;
	
	private Class<?> javaClass;
	
	private static final Map<Class<?>,SQLTypeJavaClassEnum> javaLookup  = new HashMap<Class<?>,SQLTypeJavaClassEnum>();

	private static final Map<Integer,SQLTypeJavaClassEnum> sqlLookup  = new HashMap<Integer,SQLTypeJavaClassEnum>();
	
	static {
	
	    for(SQLTypeJavaClassEnum s : EnumSet.allOf(SQLTypeJavaClassEnum.class)) {
	    
	    	javaLookup.put(s.getJavaClass(), s);
	    	sqlLookup.put(s.getSqlType(), s);
	    }
	}

	SQLTypeJavaClassEnum(Integer sqlType, Class<?> javaClass) {
		this.sqlType = sqlType;
		this.javaClass = javaClass;
	}
	
	/**
	 * @return the contentType
	 */
	public Integer getSqlType() {
		return sqlType;
	}

	/**
	 * @return the contentType
	 */
	public Class<?> getJavaClass() {
		return javaClass;
	}

    public static SQLTypeJavaClassEnum getFromSQLType(int sqlType) { 
        return sqlLookup.get(sqlType); 
    }

    public static SQLTypeJavaClassEnum getFromJavaClass(Class<?> javaClass) {
        return javaLookup.get(javaClass); 
    }
}
