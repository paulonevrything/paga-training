package com.apposit.training.video.rental.data.meta;

import java.util.Collection;
import java.util.Map;


public interface MetaGraph {

	/**
	 * Get an entity in the graph by name
	 * @param name
	 * @return the entity with the given name, or null if none exists
	 */
	public Entity getEntity(String name);
	
	/**
	 * Get a map of all entities in the graph, keyed by entity name 
	 * @return a Map<entityName, entity> of all entities in the graph
	 */
	public Map<String, Entity> getEntities();
	

	/**
	 * Set all given entities into the graph from a collection of entities
	 * MetaGraphs don't support duplicate entities. i.e. If entities aready exist, this will 
	 * replace any entities with the same name
	 * @param entities
	 */
	public void setEntities(Collection<Entity> entities);
	
	/**
	 * Sets an entity into the graph. The graph should only contain one entity of each name.
	 * This method will replace any existing entity in the graph with the same name.
	 * This methos will throw an exception of an entity with the same name exists and is involved in a relationship.
	 * @param entity
	 */
	public void setEntity(Entity entity);

	/**
	 * Removes an entity from the graph if it is not involved in any relationships. 
	 * This will throw an IllegalArgumentException exception if the entity is currently involved in any relationships;
	 * @param entity
	 */
	public Entity removeEntity(Entity entity);
	
	/**
	 * Get a collection of all relationships in the graph
	 * @return all entity relationships in the graph
	 */
	public Collection<Relationship> getRelationships();

	/**
	 * Get a collection of all the relationships in the graph for the given entity
	 * or null if the given entity doesn not exist in the graph.
	 * @param entity
	 * @return a collection of all the relationships in the graph for the given entity
	 */
	public Collection<Relationship> getRelationships(Entity entity);

	/**
	 * Get a collection of all the relationships in the graph for the entity with the given name
	 * or null if no entity by the given name exists in the graph.
	 * @param entityName
	 * @return a collection of all the relationships in the graph for the entity with the given name
	 */
	public Collection<Relationship> getRelationships(String entityName);
	
	/**
	 * Get the relationship between entityA and entityB. Two entities should only be related once in a single graph. 
	 * If more than one relationship exists, an arbitrary one of the relationships will be returned.
	 * If no relationship exists, this should return null 
	 * @param entityA
	 * @param entityB
	 * @return the relationhip between entityA and entityA and entityB, if it exists, or null if not
	 */
	public Relationship getRelationship(Entity entityA, Entity entityB);
	
	/**
	 * Get the relationship between two entities in the graph with the given names. 
	 * Returns null if no relationship exists or the is no entity in the graph for either of the given names. 
	 * @param entityA
	 * @param entityBName
	 * @return
	 */
	public Relationship getRelationship(String entityAName, String entityBName);

	/**
	 * Add a relationship to the graph. 
	 * Replaces any relationship already existing between the two entites (may have different fields related)
	 * @param primaryEntity
	 * @param secondaryEntity
	 * @param primaryField
	 * @param secondaryField
	 * @return
	 */
	public void setRelationship(Relationship relationship);
	
	/**
	 * Add a relationship between the given two fields of the two entities in the graph. Note that
	 * the fields must belong to the respective entities
	 * Replaces any relationship already existing between the two entites (may have different fields related)
	 * @param primaryEntity
	 * @param secondaryEntity
	 * @param primaryField
	 * @param secondaryField
	 * @return
	 */
	public void setRelationship(Entity primaryEntity, Entity secondaryEntity, String primaryField, String secondaryField);
	
	
	/**
	 * Add a relationship between the two fields of the two entities in the graph with the given names. Note that
	 * the fields must belong to the respective entities
	 * Replaces any relationship already existing between the two entites (may have different fields related)
	 * @param primaryEntityName
	 * @param secondaryEntityName
	 * @param primaryFieldName
	 * @param secondaryFieldName
	 * @return
	 */
	public void setRelationship(String primaryEntityName, String secondaryEntityName, String primaryFieldName, String secondaryFieldName);
	
	/**
	 * Remove a relationship from the graph.
	 * Will do nothing if the relationship doesn;t exist in the graph
	 * @param relationship
	 */
	public void removeRelationship(Relationship relationship);
	
	/**
	 * Clears the entire graph (removes all entities and relationships)
	 */
	public void clear();
}
