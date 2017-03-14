package com.apposit.training.video.rental.data.sql;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * @author Eric Chijioke
 *
 */
public class SQLUtils {

	public static Date toDate(java.util.Date date) {
		
		if(date == null) return null;
		
		return new Date(date.getTime());
	}

	public static Time toTime(java.util.Date date) {
		
		if(date == null) return null;
		
		return new Time(date.getTime());
	}

	public static Timestamp toTimeStamp(java.util.Date date) {
		
		if(date == null) return null;
		
		return new Timestamp(date.getTime());
	}

	public static java.util.Date toJavaDate(Object object){
		
		if(object instanceof Timestamp) {
			
			return new java.util.Date(((Timestamp)object).getTime());
			
		} else if( object instanceof Date) {
			
			return new java.util.Date(((Date)object).getTime());
			
		} else if (object instanceof Time) {
			
			return new java.util.Date(((Time)object).getTime());
			
		} else {
			
			return null;
		}
	}
	
	public static Class<?> getClassFromSQLType(int sqlType) {
		
		switch (sqlType) {
			case java.sql.Types.TINYINT:
			case java.sql.Types.SMALLINT:
				return java.lang.Short.class;

			case java.sql.Types.INTEGER:
				return java.lang.Integer.class;

			case java.sql.Types.BINARY:
			case java.sql.Types.VARBINARY:
			case java.sql.Types.LONGVARBINARY:
				return java.lang.Byte.class;

			case java.sql.Types.REAL:
			case java.sql.Types.DOUBLE:
			case java.sql.Types.NUMERIC:
				return java.lang.Double.class;

			case java.sql.Types.DECIMAL:
				return java.math.BigDecimal.class;

			case java.sql.Types.VARCHAR:
			case java.sql.Types.LONGVARCHAR:
			case java.sql.Types.ROWID:
			case java.sql.Types.NCHAR:
			case java.sql.Types.NVARCHAR:
			case java.sql.Types.LONGNVARCHAR:
				return java.lang.String.class;

			case java.sql.Types.BOOLEAN:
			case java.sql.Types.BIT:
				return java.lang.Boolean.class;

			case java.sql.Types.BIGINT:
				return java.lang.Long.class;

			case java.sql.Types.FLOAT:
				return java.lang.Float.class;

			case java.sql.Types.CHAR:
				return java.lang.Character.class;

			case java.sql.Types.DATE:
				return java.sql.Date.class;

			case java.sql.Types.TIME:
				return java.sql.Time.class;

			case java.sql.Types.TIMESTAMP:
				return java.sql.Timestamp.class;

			case java.sql.Types.NULL:
				return null;

			default:
				break;
		}

		return null;
	}

	public static int getSQLTypeFromClass(Class<?> javaClass) {

			if(javaClass.isEnum()) {
				return Types.VARCHAR;
			}
			else if (javaClass.equals(java.lang.Short.class)) {
				return Types.SMALLINT;
			}
			else if (javaClass.equals(java.lang.Integer.class) || javaClass.equals(int.class)) {
				return Types.INTEGER;
			}
			else if (javaClass.equals(java.lang.Byte.class)) {
				return Types.BINARY;
			}
			else if (javaClass.equals(java.lang.Double.class) || javaClass.equals(double.class)) {
				return Types.FLOAT;
			}
			else if (javaClass.equals(java.math.BigDecimal.class)) {
				return Types.DECIMAL;
			}
			else if (javaClass.equals(java.lang.String.class)) {
				return Types.NVARCHAR;
			}
			else if (javaClass.equals(java.lang.Boolean.class)) {
				return Types.BIT;
			}
			else if (javaClass.equals(java.lang.Long.class)) {
				return Types.BIGINT;
			}
			else if (javaClass.equals(java.lang.Float.class)) {
				return Types.FLOAT;
			}
			else if (javaClass.equals(java.lang.Character.class)) {
				return Types.CHAR;
			}
			else if (javaClass.equals(java.util.Date.class)) {
				return Types.DATE;
			}
			else if (javaClass.equals(java.sql.Date.class)) {
				return Types.DATE;
			}
			else if (javaClass.equals(java.sql.Time.class)) {
				return Types.TIME;
			}
			else if (javaClass.equals(java.sql.Timestamp.class)) {
				return Types.TIMESTAMP;
			}
			else if (javaClass.equals(null)) {
				return Types.NULL;
			}
		return 0;
	}
	
	
}
