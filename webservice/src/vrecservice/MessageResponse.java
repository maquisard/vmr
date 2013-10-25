package vrecservice;

import vrec.data.JSONSerializable;

public class MessageResponse extends AbstractResponse implements JSONSerializable
{
	private String message;
	
	public MessageResponse(String message)
	{
		this.setMessage(message);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String JSONSerialize() 
	{
		return serializer.serialize(this);
	}
}
