package com.apposit.training.video.rental.data.meta;

import com.apposit.training.video.rental.exception.ExceptionCode;
import com.apposit.training.video.rental.exception.ExceptionMessage;
import com.apposit.training.video.rental.exception.FatalException;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eric Chijioke
 *
 */
public class DefaultMetaGraph implements MetaGraph {

	protected Map<String,Entity> entities;
	
	//Index Map<Etity1Name, Map<Entity2Name, Relationship>> of relationships by entities for fast relationship access
	protected Map<String, Map<String, Relationship>> relationshipIndex;

	protected Collection<Relationship> relationships;
	
	public DefaultMetaGraph() {

		super();
		
		entities = new HashMap<String,Entity>();
		
		relationships = new ArrayList<Relationship>();
		
		relationshipIndex = new HashMap<String, Map<String, Relationship>>();

	}

	@Override
	public Entity getEntity(String name) {
		
		return entities.get(name);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Entity> getEntities() {
	
		return MapUtils.unmodifiableMap(entities);
	}

	@Override
	public void setEntities(Collection<Entity> entities){

		if(entities == null) { return; }
		
		for (Entity entity : entities) {
			
			this.setEntity(entity);
		}
	}
	
	@Override
	public void setEntity(Entity entity) {

		if(entity != null) {

			String name = entity.getName();
			
			if(name == null ) { throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.nullEntityName"),this,ExceptionCode.REPORTING_ERROR); }
			
			Map<String, Relationship> existing = relationshipIndex.get(name);
				
			if(existing != null && !existing.isEmpty()) {
				
				throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.deleteRelatedEntity", entity.getName()),this,ExceptionCode.REPORTING_ERROR);
			}
				
			this.entities.put(name, entity);
			
			relationshipIndex.put(name, new HashMap<String,Relationship>());
		}
	}

	@Override
	public Entity removeEntity(Entity entity) {

		if(entity == null)  { return null; }
		
		String name = entity.getName();
		
		if(name == null ) { throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.nullEntityName"),this,ExceptionCode.REPORTING_ERROR); }
		
		if(!relationshipIndex.get(name).isEmpty()) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.deleteRelatedEntity", entity.getName()),this,ExceptionCode.REPORTING_ERROR);
		}
		
		relationshipIndex.remove(name);
		
		return entities.remove(name);
	}
	
	@Override
	public Collection<Relationship> getRelationships() {
					
		return relationships;
	}

	public Collection<Relationship> getRelationships(Entity entity) {
		
		if(entity == null) return null;

		return getRelationships(entity.getName());

	}

	@Override
	public Collection<Relationship> getRelationships(String entityName) {

		if(entityName == null ) { throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.nullEntityName"),this,ExceptionCode.REPORTING_ERROR); }

		Map<String, Relationship> existing = relationshipIndex.get(entityName);
		
		if(existing == null) return null;
		
		return existing.values();
		
	}

	@Override
	public Relationship getRelationship(Entity entityA, Entity entityB) {
		
		if(entityA == null || entityB == null) { return null; } 

		return getRelationship(entityA.getName(), entityB.getName());
		
	}

	@Override
	public Relationship getRelationship(String entityAName, String entityBName) {

		if(entityAName == null || entityBName == null) { throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.nullEntityName"),this,ExceptionCode.REPORTING_ERROR); }

		return relationshipIndex.get(entityAName).get(entityBName);
	}

	@Override
	public void setRelationship(Relationship relationship){
		
		if(relationship != null) {
		
			setRelationship(relationship.getPrimaryEntity(), relationship.getForeignEntity(), relationship.getPrimaryField(), relationship.getForeignField());

		}
	}
	
	@Override
	public void setRelationship(Entity primaryEntity, Entity secondaryEntity, String primaryField, String secondaryField) {
		
		if(primaryEntity == null) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.nullPrimaryEntity"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		if(secondaryEntity == null){
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.nullSecondaryEntity"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		if(primaryField == null) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.nullPrimaryField"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		if(secondaryField == null) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.nullSecondaryField"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		if(primaryEntity.getField(primaryField) == null) {
			
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.notEntityField", primaryEntity.getName(), primaryField),this,ExceptionCode.REPORTING_ERROR);
		} 
	
		if(secondaryEntity.getField(secondaryField) == null) {
			
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.notEntityField", secondaryEntity.getName(), secondaryField),this,ExceptionCode.REPORTING_ERROR);
		}

		primaryEntity = entities.get(primaryEntity.getName());
		secondaryEntity = entities.get(secondaryEntity.getName());
		
		if(primaryEntity == null ) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.invalidPrimaryEntity"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		if(secondaryEntity == null ) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.invalidSecondaryEntity"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		Relationship existing = getRelationship(primaryEntity, secondaryEntity);
		
		if(existing != null) {
			
			removeRelationship(existing);
		}
		
		Relationship relationship = new Relationship(primaryEntity, secondaryEntity, primaryField, secondaryField);
		
		relationshipIndex.get(primaryEntity.getName()).put(secondaryEntity.getName(), relationship);

		relationshipIndex.get(secondaryEntity.getName()).put(primaryEntity.getName(), relationship);
		
		relationships.add(relationship);		
	}

	@Override
	public void setRelationship(String primaryEntityName, String secondaryEntityName, String primaryFieldName, String secondaryFieldName) {

		Entity primaryEntity = entities.get(primaryEntityName);
		Entity secondaryEntity = entities.get(secondaryEntityName);
		
		if(primaryEntity == null ) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.invalidPrimaryEntity"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		if(secondaryEntity == null ) {
			throw new FatalException(new ExceptionMessage("error.reporting.adhoc.defaultMetaGraph.addRelationship.invalidSecondaryEntity"),this,ExceptionCode.REPORTING_ERROR);
		}
		
		setRelationship(entities.get(primaryEntityName), entities.get(secondaryEntityName) , primaryFieldName, secondaryFieldName );
		
	}

	@Override
	public void removeRelationship(Relationship relationship){

		if(relationship == null) return;
		
		Entity primaryEntity = relationship.getPrimaryEntity();
		
		Entity foreignEntity = relationship.getForeignEntity();
		
		relationshipIndex.get(primaryEntity.getName()).remove(foreignEntity.getName());

		if(primaryEntity != foreignEntity) {
			
			relationshipIndex.get(foreignEntity.getName()).remove(primaryEntity.getName());
		}
		
		relationships.remove(relationship);
		
	}
	
	@Override
	public void clear() {

		entities = new HashMap<String,Entity>();

		relationships = new ArrayList<Relationship>();
		
		relationshipIndex = new HashMap<String, Map<String, Relationship>>();
		
	}
	
}
