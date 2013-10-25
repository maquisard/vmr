package vrecservice;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import vrec.data.movie.MovieUser;

@Path("/user")
public class UserService extends AbstractService
{
	@Resource
	private WebServiceContext wsContext;
	
	@GET
	@Path("/get/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String get(@PathParam("param") int ml_id)
	{
		MovieUser user = MovieUser.retrieveUserByMlId(ml_id);
		if(user == null) return new ErrorResponse("No User found with id: " + ml_id).JSONSerialize();
		
		return new EntityResponse<MovieUser>(user, true).JSONSerialize();
	}
	
	@GET
	@Path("/set/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String set(@PathParam("param") int ml_id)
	{
		
        MessageContext mc = wsContext.getMessageContext();
        HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession();
        // Get a session property "counter" from context
        if (session == null)
            throw new WebServiceException("No session in WebServiceContext");       
        
        MovieUser user = (MovieUser)session.getAttribute("currentuser");		
        if(user == null || user.getMl_id() != ml_id)
        {
            user = MovieUser.retrieveUserByMlId(ml_id);
        }
		if(user == null) return new ErrorResponse("No User found with id: " + ml_id).JSONSerialize();
		
		session.setAttribute("currentuser", user);
		return new EntityResponse<MovieUser>(user, true).JSONSerialize();
	}
	
	@GET
	@Path("/random/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String random(@PathParam("param") int k)
	{
		if(k <= 0)
		{
			return new ErrorResponse("The number of random users must be greater than zero.").JSONSerialize();
		}
		List<MovieUser> randomUsers = MovieUser.retrieveRandomUsers(k);
		if(randomUsers.isEmpty()) return new ErrorResponse("No random users found -- There might be a problem with the database").JSONSerialize();
		return new EntityCollectionResponse<MovieUser>(randomUsers, true).JSONSerialize();
	}

}
