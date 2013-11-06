package vrecservice;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vrec.data.movie.MovieItem;
import vrec.data.movie.MovieUser;

@WebService
@Path("/movie")
public class MovieService extends AbstractService
{
	@GET
	@Path("/queue/{index}/{total}")
	@Produces(MediaType.APPLICATION_JSON)
	public String queue(@PathParam("index") int index, @PathParam("total") int total)
	{
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output();
		}
		List<MovieItem> movies = currentUser.retrieveMovieQueue();
		if(index < 0 || index >= movies.size()) return new ErrorResponse("Negative index").output();
		if(total < 0 || total > movies.size()) return new ErrorResponse("Negative total").output();
		if(index >= total) return new ErrorResponse("index must be less than total").output();

		List<MovieItem> returnList = new ArrayList<>();
		
		for(int i = index; i < index + total && i < movies.size(); i++)
		{
			MovieItem movie = movies.get(i);
			movie.decodeMembersName();
			returnList.add(movie);
		}
		
		return new EntityCollectionResponse<MovieItem>(returnList, true).output();
	}

	@GET
	@Path("/watched/{index}/{total}")
	@Produces(MediaType.APPLICATION_JSON)
	public String watched(@PathParam("index") int index, @PathParam("total") int total)
	{
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output();
		}
		List<MovieItem> movies = currentUser.retrieveWatchedMovies(index, total);
		if(index < 0) return new ErrorResponse("Negative index").output();
		if(total < 0 ) return new ErrorResponse("Negative total").output();
		
		for(MovieItem movie : movies)
		{
			movie.decodeMembersName();
		}
		
		return new EntityCollectionResponse<MovieItem>(movies, true).output("watched_callback");
	}
}
