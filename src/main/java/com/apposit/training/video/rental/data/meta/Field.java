package com.apposit.training.video.rental.data.meta;

import java.io.Serializable;

/**
 * @author Eric Chijioke
 * Fields represent the fields of an entity in the 'graph'. 
 * Fields must be uniquely named within an entity ('name' property different for each field in an entity) as 
 * any two fields with the same name and same entity are equal (return true for the .equals() operation)
 */
public class Field implements Serializable {

	private static final long serialVersionUID = -4155689464950342474L;
	
	protected String name;
	
	protected String alias;
	
	protected FieldType type;
	
	protected String value;
	
	protected FieldOperator operator;
	
	protected String operand;
	
	protected FieldAggregator aggregator;
	
	protected FieldSort sort;
	

	/**
	 * Default Filed constructor
	 */
	public Field() {
		
		super();
	}
	
	/**
	 * Utility constructor to construct new Field with field assignments
	 * @param owner
	 * @param name
	 * @param alias
	 * @param type
	 * @param value
	 */
	public Field(String name, String alias, FieldType type, String value) {
		
		super();
		
		this.name = name;
		this.alias = alias;
		this.type = type;
		this.value = value;
	}

	/**
	 * Overrides this method to equate fields that have the same name (notice this doesn;t ensure that they belong to the same entity).
	 */
	@Override
	public boolean equals(Object other) {

		if(other == null) return false;
		
		if(other instanceof Field) {

			Field otherField = (Field) other;
			
			if(this.name == null || otherField.name == null ) { return false; }
			
			return otherField.name.equals(this.name);
			
		}
		
		return false;
	}

	@Override
	public int hashCode() {

	    int hash = 1;
	    
	    hash = hash * 13 + (name == null ? 0 : name.hashCode()); 
	    
	    return hash;
	}

	@Override
	public String toString() {

		return (name == null ? "" : name);
		
	}
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public FieldOperator getOperator() {
		return operator;
	}

	public void setOperator(FieldOperator operator) {
		this.operator = operator;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	public FieldAggregator getAggregator() {
		return aggregator;
	}

	public void setAggregator(FieldAggregator aggregator) {
		this.aggregator = aggregator;
	}

	public FieldSort getSort() {
		return sort;
	}

	public void setSort(FieldSort sort) {
		this.sort = sort;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Field clone = new Field();
		clone.name = this.name;
		clone.alias = this.alias;
		clone.type = this.type;
		clone.value = this.value;
		clone.operator = this.operator;
		clone.operand = this.operand;
		clone.aggregator = this.aggregator;
		clone.sort = this.sort;
		return clone;
		
	}
}
