package vrecservice;

import java.util.List;

import core.Entity;
import vrec.data.JSONFilterable;
import vrec.data.JSONSerializable;

public class EntityCollectionResponse<T extends Entity> extends AbstractResponse implements JSONSerializable
{
	private List<T> entities;
	private String entityType;
	
	public EntityCollectionResponse(List<T> entities, boolean status)
	{
		this.setEntities(entities);
		this.setStatus(status);;
	}

	@Override
	public String JSONSerialize() 
	{
		serializer = serializer.exclude("*.id", "serializer", "*.persisted", "*.allFields").include("entities");
		for(T entity : entities)
		{
			if(entity instanceof JSONFilterable)
			{
				serializer = ((JSONFilterable) entity).filter(serializer);
				break;
			}
		}
		return serializer.serialize(this);
	}

	/**
	 * @return the entities
	 */
	public List<T> getEntities() {
		return entities;
	}

	/**
	 * @param entities the entities to set
	 */
	public void setEntities(List<T> entities) {
		this.entities = entities;
		for(T entity : entities)
		{
			this.entityType = entity.getClass().getSimpleName();
			break;
		}
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
}
