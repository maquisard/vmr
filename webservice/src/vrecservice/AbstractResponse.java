package vrecservice;

import com.google.gson.annotations.Expose;

import flexjson.JSONSerializer;

public abstract class AbstractResponse
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
	
}
