package vrecservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class testService 
{
	@GET
	@Path("/name/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String helloWorld(@PathParam("param") String name)
	{
		return "hello " + name;
	}

	@GET
	@Path("/simple")
	@Produces(MediaType.APPLICATION_JSON)
	public String helloWorld()
	{
		return "hello world.";
	}
}
