package com.apposit.training.video.rental.data.meta;

import java.math.BigDecimal;
import java.sql.SQLXML;
import java.sql.Timestamp;


public enum FieldType {

	BOOLEAN("java.lang.Boolean", Boolean.class),
	INTEGER("java.lang.Integer", Integer.class),
	DOUBLE("java.lang.Double", Double.class),
	STRING("java.lang.String", String.class),
	DATE("java.sql.Timestamp", Timestamp.class),
	BINARY("java.lang.Byte", Byte.class),
	XML("java.sql.SQLXML", SQLXML.class),
	DECIMAL("java.math.BigDecimal", BigDecimal.class),
	NULL("java.sql.Types.NULL", null),
	UNSUPPORTED("java.sql.Types.NULL", null);

	private String className;
	private Class<?> fieldClass;
	
	FieldType(String className, Class<?> fieldClass) {
		this.className = className;
		this.fieldClass = fieldClass;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the className
	 */
	public Class<?> getFieldClass() {
		return fieldClass;
	}
	
}
