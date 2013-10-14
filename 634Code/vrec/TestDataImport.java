/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec;

import vrec.data.ScraperFactory;

import com.omertron.themoviedbapi.MovieDbException;

/**
 *
 * @author El Zede
 */
public class TestDataImport 
{
    public static void main(String[] args) throws MovieDbException
    {
        Configuration.initStorage();
        ScraperFactory.getCurrentScraper().run();
//    	TheMovieDbApi service = new TheMovieDbApi(DefaultSettings.getCurrent().getTmdbApiKey());
//    	TmdbResultsList<MovieDb> results = service.searchMovie("Star Wars", 2002, "en", false, 0);
//    	System.out.println("Results Count: " + results.getTotalResults());
//    	for(MovieDb mv : results.getResults())
//    	{
//    		MovieDb movie = service.getMovieInfo(mv.getId(), "en");
//    		System.out.println(movie.getOriginalTitle());
//    		System.out.println("\t" + movie.getReleaseDate());
//    		System.out.println("\t" + movie.getImdbID());
//    		System.out.println("\t" + movie.getRuntime() + " mins");
//    		System.out.println("\t" + movie.getVoteAverage());
//    	}
    	System.out.println("This is the beginning");
    }
}
