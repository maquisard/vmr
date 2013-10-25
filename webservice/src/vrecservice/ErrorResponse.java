package vrecservice;


public class ErrorResponse extends MessageResponse
{
	public ErrorResponse(String message) 
	{
		super(message);
		this.setStatus(false);
	}
}
