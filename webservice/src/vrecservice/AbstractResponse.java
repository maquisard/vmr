package vrecservice;

import vrec.data.JSONSerializable;

import com.google.gson.annotations.Expose;

import flexjson.JSONSerializer;

public abstract class AbstractResponse implements JSONSerializable
{
	public JSONSerializer serializer;
	
	@Expose 
	private boolean status;
	
	public AbstractResponse()
	{
		serializer = new JSONSerializer();
		serializer = serializer.exclude("*.class", "serializer");
	}
	
	public boolean getStatus()
	{
		return this.status;
	}
	
	public void setStatus(boolean status)
	{
		this.status = status;
	}
	
	public String output()
	{
		return "jsonCallBack(" + this.JSONSerialize() + ");";
	}
	
	public String output(String callback)
	{
		return callback + "(" + this.JSONSerialize() + ");";
	}
}
