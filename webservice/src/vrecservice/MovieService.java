package vrecservice;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vrec.DefaultSettings;
import vrec.data.ItemRating;
import vrec.data.UserItem;
import vrec.data.movie.MovieItem;
import vrec.data.movie.MovieUser;

@WebService
@Path("/movie")
public class MovieService extends AbstractService
{

	@GET
	@Path("/recommend/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String recommend(@PathParam("param") int k)
	{
		if(k <= 0)
		{
			return new ErrorResponse("The number of recommended movies must be greater than zero.").output("recommend_movies_callback");
		}
		
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output("recommend_movies_callback");
		}
		
		List<MovieItem> randomMovies = MovieItem.retrieveRandomMovies(k);
		if(randomMovies.isEmpty()) return new ErrorResponse("No recommended movies found -- There might be a problem with the database").output("recommend_movies_callback");
		
		for(MovieItem movie : randomMovies)
		{
			this.updateMovie(movie, currentUser);
		}
		
		return new EntityCollectionResponse<MovieItem>(randomMovies, true).output("recommend_movies_callback");
	}
	
	
	@GET
	@Path("/recommend/{movieid}/{k}")
	@Produces(MediaType.APPLICATION_JSON)
	public String recommend(@PathParam("k") int k, @PathParam("movieid") String movieid)
	{
		if(k <= 0)
		{
			return new ErrorResponse("The number of recommended movies must be greater than zero.").output("recommend_movies_callback");
		}
		
		if(movieid == null || movieid.isEmpty())
		{
			return new ErrorResponse("Invalid movie id").output("recommend_movies_callback");
		}
		
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output("recommend_movies_callback");
		}
		
		List<MovieItem> randomMovies = MovieItem.retrieveRandomMovies(k);
		if(randomMovies.isEmpty()) return new ErrorResponse("No recommended movies found -- There might be a problem with the database").output("recommend_movies_callback");
		
		for(MovieItem movie : randomMovies)
		{
			this.updateMovie(movie, currentUser);
		}
		
		return new EntityCollectionResponse<MovieItem>(randomMovies, true).output("recommend_movies_callback");
	}
	
	
	
	@GET
	@Path("/random/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String random(@PathParam("param") int k)
	{
		if(k <= 0)
		{
			return new ErrorResponse("The number of random movies must be greater than zero.").output("random_movies_callback");
		}
		
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output("random_movies_callback");
		}
		
		List<MovieItem> randomMovies = MovieItem.retrieveRandomMovies(k);
		if(randomMovies.isEmpty()) return new ErrorResponse("No random movies found -- There might be a problem with the database").output("random_movies_callback");
		
		for(MovieItem movie : randomMovies)
		{
			this.updateMovie(movie, currentUser);
		}
		
		return new EntityCollectionResponse<MovieItem>(randomMovies, true).output("random_movies_callback");
	}
	
	@GET
	@Path("/rating/{value}/{movieid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateRating(@PathParam("value") float value, @PathParam("movieid") String movieid)
	{
		if(movieid == null || movieid.isEmpty())
		{
			return new ErrorResponse("Invalid movie id").output("update_rating_callback");
		}
		
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output("update_rating_callback");
		}
		MovieItem movie = MovieItem.retrieveById(MovieItem.class, movieid);
		if(movie == null)
		{
			return new ErrorResponse("No movie was retrieved with id: " + movieid).output("update_rating_callback");
		}
		
		ItemRating itemRating = ItemRating.retrieveItemRating("" + movie.getMl_id(), "" + currentUser.getMl_id());
		if(itemRating == null)
		{
			itemRating = new ItemRating();
			itemRating.setItemId("" + movie.getMl_id());
			itemRating.setUserId("" + currentUser.getMl_id());
			itemRating.setItemtype(DefaultSettings.getCurrent().getItemClass());
		}
		itemRating.setRating(value);
		itemRating.save();
		return new MessageResponse("Rating Update Successful").output("update_rating_callback");
	}
	
	@GET
	@Path("/queue/{index}/{total}")
	@Produces(MediaType.APPLICATION_JSON)
	public String queue(@PathParam("index") int index, @PathParam("total") int total)
	{
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output("queue_callback");
		}
		List<MovieItem> movies = currentUser.retrieveMovieQueue(index, total);
		if(index < 0) return new ErrorResponse("Negative index").output("queue_callback");
		if(total < 0 ) return new ErrorResponse("Negative total").output("queue_callback");

		for(MovieItem movie : movies)
		{
			this.updateMovie(movie, currentUser);
		}
		
		return new EntityCollectionResponse<MovieItem>(movies, true).output("queue_callback");
	}

	@GET
	@Path("/watched/{index}/{total}")
	@Produces(MediaType.APPLICATION_JSON)
	public String watched(@PathParam("index") int index, @PathParam("total") int total)
	{
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output("watched_callback");
		}
		List<MovieItem> movies = currentUser.retrieveWatchedMovies(index, total);
		if(index < 0) return new ErrorResponse("Negative index").output("watched_callback");
		if(total < 0 ) return new ErrorResponse("Negative total").output("watched_callback");
		
		for(MovieItem movie : movies)
		{
			this.updateMovie(movie, currentUser);
		}
		
		return new EntityCollectionResponse<MovieItem>(movies, true).output("watched_callback");
	}
	
	@GET
	@Path("add/queue/{movieid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String addToQueue(@PathParam("movieid") String movieid)
	{
		if(movieid == null || movieid.isEmpty())
		{
			return new ErrorResponse("Invalid movie id").output("add_queue_callback");
		}
		
		MovieUser currentUser = this.getCurrentUser();
		if(currentUser == null)
		{
			return new ErrorResponse("There is no user in the session, Please log in to execute this operation").output("add_queue_callback");
		}
		MovieItem movie = MovieItem.retrieveById(MovieItem.class, movieid);
		if(movie == null)
		{
			return new ErrorResponse("No movie was retrieved with id: " + movieid).output("add_queue_callback");
		}
		
		UserItem useritem = new UserItem();
		useritem.setItemid("" + movie.getMl_id());
		useritem.setUserid("" + currentUser.getMl_id());
		useritem.setStatus(UserItem.QUEUE);
		useritem.setItemtype(DefaultSettings.getCurrent().getItemClass());
		if(UserItem.exist(useritem))
		{
			return new ErrorResponse("Movie is already in the queue The client should handle this case - " + movieid).output("add_queue_callback");
		}
		useritem.save();
		
		return new MessageResponse("Added to the queue.").output("add_queue_callback");
	}
	
	private void updateMovie(MovieItem movie, MovieUser user)
	{
		movie.decodeMembersName();
		movie.updateUserRating(user.getMl_id());
		movie.updateAttributes();
	}
}
