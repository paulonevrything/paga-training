package com.apposit.training.video.rental.data.meta;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Eric Chijioke
 * Entities represent the basic data container in the 'graph'. 
 * Entities must be uniquely named ('name' field different for each entity) as 
 * any two entities with the same name are equal (retun true for the .equals() operation)
 */
public class Entity implements Cloneable, Serializable {

	private static final long serialVersionUID = 8882042961914726736L;

	protected String name;
	
	protected String alias;
	
	protected EntityType type;
	
	protected Map<String, Field> fields;

	/**
	 * Default Entity constructor
	 */
	public Entity(){
	
		super();
		
		fields = new TreeMap<String, Field>();
	}
	
	/**
	 * Utility constructor to construct new Entity with field assignments
	 * @param name
	 * @param type
	 * @param alias
	 */
	public Entity(String name, EntityType type, String alias){
		
		this();
		
		this.name = name;
		this.type = type;
		this.alias = alias;
	}
		
	/**
	 * Overrides this method to equate entities that have the same name.
	 */
	@Override
	public boolean equals(Object other) {

		if(other == null) return false;
		
		if(other instanceof Entity) {
			
			Entity otherEntity = (Entity) other;
			
			if( this.name == null || otherEntity.name == null) { return false; }
				
			return otherEntity.name.equals(this.name);
			
		}
		
		return false;
	}

	@Override
	public int hashCode() {
	    
	    return 19 + (name == null ? 0 : name.hashCode());
	}

	@Override
	public String toString() {
		
		return (name == null ? "" : name) + (alias == null ? "" : " (" + alias + ")");
	}

	public Map<String, Field> getFields() {
		
		return fields;
	}
	
	public Field getField(String name) {
		
		return this.fields.get(name);
	}

	public void setFields(Map<String, Field> fields) {
		
		for (Field field : fields.values()) {
			
			this.setField(field);
		}
	}

	public void setField(Field field) {
				
		fields.put(field.getName(), field);
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

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Entity clone = new Entity();

		clone.name = this.name;
		
		clone.alias = this.alias;
		
		clone.type = this.type;
		
		for (Field field : this.fields.values()) {
		
			clone.fields.put(field.getName(), (Field) field.clone());
		}
		
		return clone;
	}
}
