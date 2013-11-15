package vrecservice;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vrec.data.movie.MovieUser;

@WebService
@Path("/user")
public class UserService extends AbstractService
{
	@GET
	@Path("/get/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String get(@PathParam("param") int ml_id)
	{
		MovieUser user = MovieUser.retrieveUserByMlId(ml_id);
		if(user == null) return new ErrorResponse("No User found with id: " + ml_id).output();
		
		return new EntityResponse<MovieUser>(user, true).output();
	}
	
	@GET
	@Path("/set/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String set(@PathParam("param") int ml_id)
	{
		MovieUser user = this.getCurrentUser();		
        if(user == null || user.getMl_id() != ml_id)
        {
            user = MovieUser.retrieveUserByMlId(ml_id);
        }
		if(user == null) return new ErrorResponse("No User found with id: " + ml_id).output();
		
		this.setCurrentUser(user);
		user.clearBrowsedMovies();
		return new EntityResponse<MovieUser>(user, true).output();
	}
	
	@GET
	@Path("/random/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String random(@PathParam("param") int k)
	{
		if(k <= 0)
		{
			return new ErrorResponse("The number of random users must be greater than zero.").output();
		}
		List<MovieUser> randomUsers = MovieUser.retrieveRandomUsers(k);
		if(randomUsers.isEmpty()) return new ErrorResponse("No random users found -- There might be a problem with the database").output();
		return new EntityCollectionResponse<MovieUser>(randomUsers, true).output();
	}

}
