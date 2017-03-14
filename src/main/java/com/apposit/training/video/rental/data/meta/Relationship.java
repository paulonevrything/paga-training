package com.apposit.training.video.rental.data.meta;

import java.io.Serializable;

/**
 * </p>
 * Relationships represent the relationship between two entities in the graph. For each relationship there is 
 * a primary and a foreign entity and a primary and foreign field;
 * </p>
 * There should only be one relationship between any two entities in the object graph and two relationships will be equal
 * (equals() operation will return true) if the primary and foreign entities are equal <i>irrespective of whether the field
 * values are equals</i> 
 * 
 * @author Eric Chijioke
 */
public class Relationship implements Serializable {

	private static final long serialVersionUID = -5049805751688077888L;

	protected Entity primaryEntity;
	
	protected Entity foreignEntity;

	protected String primaryField;
	
	protected String foreignField;
	
		
	/**
	 * Default Relationship constructor
	 */
	public Relationship() {
		
		super();
	}
	
	/**
	 * Utility constructor to construct new Relationship with field assignments
	 * @param primaryEntity
	 * @param foreignEntity
	 * @param primaryField
	 * @param foreignField
	 * @param cardinality
	 */
	public Relationship(Entity primaryEntity, Entity foreignEntity, String primaryField, String foreignField) {
		
		super();
		this.primaryEntity = primaryEntity;
		this.foreignEntity = foreignEntity;
		this.primaryField = primaryField;
		this.foreignField = foreignField;
	}

	/**
	 * Return true if the specified entity is related by this relationship
	 * @param entity
	 * @return true if the specified entityis related by this relationship
	 */
	public boolean relates(Entity entity) {
		
		return (primaryEntity != null ? primaryEntity.equals(entity) : false ) || (foreignEntity != null ? foreignEntity.equals(entity) : false );
	}
	
	public boolean isComplete(){
		
		return (this.primaryEntity != null && this.foreignEntity != null && this.primaryField != null && this.foreignField != null);
	}
	
	/**
	 * Overrides this method to equate relationships that have the same entities (irrespective of primary/secondary nature, cardinality, or field values).
	 */
	@Override
	public boolean equals(Object other) {

		if(other == null) return false;
		
		if(other instanceof Relationship) {
			
			return (((Relationship)other).primaryEntity.equals(this.primaryEntity) && ((Relationship)other).foreignEntity.equals(this.foreignEntity))
			|| (((Relationship)other).primaryEntity.equals(this.foreignEntity) && ((Relationship)other).foreignEntity.equals(this.primaryEntity));
			
		}
		
		return false;
	}

	@Override
	public int hashCode() {

	    int hash = 1;
	    
	    hash = hash * 11 + (primaryEntity == null ? 0 : primaryEntity.hashCode()); 

	    hash = hash * 31 + (foreignEntity == null ? 0 : foreignEntity.hashCode()); 
	    
	    return hash;
	}
	
	@Override
	public String toString() {
		
		if(primaryEntity != null && foreignEntity != null) { 
			
			if(primaryField != null && foreignField != null ) {
			
				return primaryField.toString() + " <-> " + foreignField.toString();
			}
			
			return primaryEntity.toString() + " <-> " + foreignEntity.toString();
		
		} else {
			
			if(primaryEntity != null) {
				
				return primaryEntity.toString() + " <-> ?";
				
			} else if(foreignEntity != null) {
				
				return  "? <-> " + foreignEntity.toString();
			}
			
			return super.toString();
		}
	}
	
	public Entity getPrimaryEntity() {
		return primaryEntity;
	}

	public void setPrimaryEntity(Entity primaryEntity) {
		this.primaryEntity = primaryEntity;
	}

	public Entity getForeignEntity() {
		return foreignEntity;
	}

	public void setForeignEntity(Entity foreignEntity) {
		this.foreignEntity = foreignEntity;
	}

	public String getPrimaryField() {
		return primaryField;
	}

	public void setPrimaryField(String primaryField) {
		this.primaryField = primaryField;
	}

	public String getForeignField() {
		return foreignField;
	}

	public void setForeignField(String foreignField) {
		this.foreignField = foreignField;
	}
}
