package vrecservice;

import vrec.data.JSONFilterable;
import vrec.data.JSONSerializable;
import core.Entity;
import flexjson.JSON;

public class EntityResponse<T extends Entity> extends AbstractResponse implements JSONSerializable
{
	private T entity;
	private String entityType;
	
	public EntityResponse(T entity, boolean status)
	{
		this.setEntity(entity);
		this.setStatus(status);
	}

	/**
	 * @return the entity
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(T entity) {
		this.entity = entity;
		this.entityType = entity.getClass().getSimpleName();
	}

	/**
	 * @return the entityType
	 */
	@JSON
	public String getEntityType() {
		return entityType;
	}

	@Override
	public String JSONSerialize() 
	{
		serializer = serializer.exclude("*.id", "serializer", "*.persisted", "*.allFields");
		if(entity instanceof JSONFilterable)
		{
			serializer = ((JSONFilterable) entity).filter(serializer);
		}
		return serializer.serialize(this);
	}
}
