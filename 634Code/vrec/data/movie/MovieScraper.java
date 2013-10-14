/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data.movie;

import core.Entity;
import core.Query;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.MovieDb;
import com.omertron.themoviedbapi.model.Trailer;
import com.omertron.themoviedbapi.results.TmdbResultsList;

import vrec.DefaultSettings;
import vrec.data.ItemRating;
import vrec.data.Scraper;

/**
 *
 * @author El Zede
 * 
 * This class use the Movie lens database as a seed and then scrapes the 
 * OMDB to download the rest of the item data(images, descriptions, actors, etc...)
 */
public class MovieScraper extends Scraper 
{
	
	private JSONParser parser = new JSONParser();

    private void updateRatings()
    {
        Query query = new Query("itemrating");
        for(ItemRating itemRating : query.run(ItemRating.class))
        {
            itemRating.setItemtype(DefaultSettings.getCurrent().getItemClass());
            itemRating.save();
        }
    }
    
    private void importRatings()
    {
        try 
        {
            String itemSeedPath = this.getItemSeedPath("u.data");
            BufferedReader reader = new BufferedReader(new FileReader(itemSeedPath));
            String recordLine = reader.readLine();
            while(recordLine != null && !recordLine.isEmpty())
            {
                String[] properties = recordLine.split("\\	");
                String userid = properties[0].trim();
                String itemid = properties[1].trim();
                float rating = Float.parseFloat(properties[2].trim());
                ItemRating mrating = new ItemRating();
                mrating.setItemId(itemid);
                mrating.setUserId(userid);
                mrating.setRating(rating);
                mrating.setItemtype(DefaultSettings.getCurrent().getItemClass());
                mrating.save();
                recordLine = reader.readLine();
            }
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(MovieScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cleanMovieData()
    {
        String[] tables = { "movieitem", "moviecastmember", "moviecastmemberdata", "moviegenredata" };
        Class[] classes = { MovieItem.class, 
                            MovieCastMember.class, 
                            MovieCastMemberData.class, 
                            MovieGenreData.class };
        
        for(int i = 0; i < tables.length; i++)
        {
        	System.out.println("Cleaning " + tables[i] + "...");
            Query query = new Query(tables[i]);
            for (Iterator it = query.run(classes[i]).iterator(); it.hasNext();) {
                Entity entity = (Entity) it.next();
                entity.delete();
                System.out.println("deleting....");
            }
        }
    }
    
    private void importMovies()
    {
        try 
        {
            String itemSeedPath = this.getItemSeedPath("u.item");
            BufferedReader reader = new BufferedReader(new FileReader(itemSeedPath));
            String recordLine = reader.readLine();
            while(recordLine != null && !recordLine.isEmpty())
            {
                String[] properties = recordLine.split("\\|");
                int ml_id = Integer.parseInt(properties[0]);
                String dirty_title = properties[1].split("\\(")[0].trim();
                String[] pieces = dirty_title.split(", ");
                if(pieces.length == 2)
                {
                    dirty_title = pieces[0].trim();
                }
                
                int i = properties[1].lastIndexOf("(") + 1;
                int j = properties[1].lastIndexOf(")");
                
                if(j < 0)
                {
                    recordLine = reader.readLine();
                    continue;
                }
                    
                int year = Integer.parseInt(properties[1].substring(i, j));
                
                MovieItem movie = new MovieItem();
                movie.setTitle(dirty_title);
                movie.setYear(year);
                movie.setMl_id(ml_id);
                movie.save();
                
                for(int k = 5; k <= 23; k++)
                {
                    if("1".equals(properties[k]))
                    {
                        MovieGenre genre = MovieGenre.retrieveByValue(k - 5);
                        if(genre != null)
                        {
                            MovieGenreData genredata = new MovieGenreData();
                            genredata.setMovieid(movie.getId());
                            genredata.setGenreid(genre.getId());
                            genredata.save();
                        }
                    }
                }
                
                String url_template = "http://www.omdbapi.com/?i=&s=%s";
                String url_text = String.format(url_template, URLEncoder.encode(dirty_title, "UTF-8"), 1993);
                URL url = new URL(url_text);
                JSONObject moviedata = this.getMovieData(url, dirty_title, year);
                
                if(moviedata != null)
                {
                    //correcting title
                    movie.setTitle(moviedata.get("Title").toString());
                    MovieErsbRating rating = MovieErsbRating.retrieveByName(moviedata.get("Rated").toString());
                    if(rating != null)
                    {
                        movie.setErsbRatingid(rating.getId());
                    }
                    movie.setRuntime(moviedata.get("Runtime").toString());
                    this.grabCastMember(moviedata, MovieCastMember.DIRECTOR, "Director", movie.getId());
                    this.grabCastMember(moviedata, MovieCastMember.ACTOR, "Actors", movie.getId());
                    this.grabCastMember(moviedata, MovieCastMember.WRITER, "Writer", movie.getId());
                    movie.setDescription(moviedata.get("Plot").toString());
                    if(!moviedata.get("Poster").toString().contains("N/A"))
                    {
                        movie.setPosterUrl(moviedata.get("Poster").toString());
                        //download the poster locally
                        BufferedImage poster = ImageIO.read(new URL(movie.getPosterUrl()));
                        String imagePath = this.getItemPosterPath() + movie.getId() + ".jpg";
                        ImageIO.write(poster, "jpg",new File(imagePath));
                    }
                    System.out.println(String.format("%s - %s", movie.getTitle(), moviedata.get("imdbRating").toString()));
                    if(moviedata.get("imdbRating").toString().contains("N/A"))
                    {
                        movie.setImdbRating(0.0f);
                    }
                    else
                    {
                        movie.setImdbRating(Float.parseFloat(moviedata.get("imdbRating").toString()));
                    }
                    movie.setImdbId(moviedata.get("imdbID").toString());
                }
                movie.save();
                System.out.println(String.format("%d - %s", movie.getMl_id(), movie.getTitle()));
                recordLine = reader.readLine();
            }
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(MovieScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private JSONObject getMovieData(URL url, String dirty_title, int dirty_year) throws Exception
    {
        JSONObject searchdata = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));
        JSONObject moviedata = null;
        if(searchdata != null && !searchdata.isEmpty()) //I am assuming
        {
            if(!searchdata.containsKey("Error"))
            {
                JSONArray data = (JSONArray) parser.parse(searchdata.get("Search").toString());
                String title = "";
                String year = "";
                Map<Integer, String[]> candidates = new TreeMap<Integer, String[]>();
                for(Object json : data)
                {
                    moviedata = (JSONObject)json;
                    title = moviedata.get("Title").toString();
                    year = moviedata.get("Year").toString();
                    int distance = this.distance(title, dirty_title);
                    distance += Math.abs(Integer.parseInt(year) - dirty_year);
                    candidates.put(distance, new String[]{ title, year });
                }
                for(int key : candidates.keySet())
                {
                    title = ((String[])candidates.get(key))[0];
                    year = ((String[])candidates.get(key))[1];

                    String url_template = "http://www.omdbapi.com/?i=&t=%s&y=%s";
                    String url_text = String.format(url_template, URLEncoder.encode(new String(title.getBytes(), "UTF-8"), "UTF-8"), year);
                    URL new_url = new URL(url_text);
                    moviedata = (JSONObject) parser.parse(new InputStreamReader(new_url.openStream()));
                    
                    Object good = null;
                    if(title.contains("The Professional"))
                    {
                    	good = moviedata.get("imdbRating");
                    }
                    if(moviedata.get("imdbRating").toString().contains("N/A") || 
                       moviedata.get("Poster").toString().contains("N/A"))
                        continue;
                    else
                        break;
                }
            }
        }
        return moviedata;
    }
    
    private String retrieveImdBRating(String imdbId) throws IOException, ParseException
    {
        String url_template = "http://www.omdbapi.com/?i=%s";
        String url_text = String.format(url_template, imdbId);
        URL new_url = new URL(url_text);
        JSONObject moviedata = (JSONObject) parser.parse(new InputStreamReader(new_url.openStream()));
        
        return moviedata.get("imdbRating").toString();
    }
    
    private int distance(String s1, String s2)
    {
        int sum = 0;
        int max = s1.length() > s2.length() ? s1.length() : s2.length();
        if(s1.equals(s2)) return 0;
        for(int i = 0; i < max; i++)
        {
            if(i >= s1.length() || i >= s2.length())
            {
                sum += 1;
            }
            else if(s1.charAt(i) != s2.charAt(i))
            {
                sum += 1;
            }
        }
        return sum;
    }
    
    private void grabCastMember(JSONObject data, String type, String key, String movieid) throws Exception
    {
        for(String memberName : this.getArray(data, key))
        {
            memberName = URLEncoder.encode(memberName, "UTF-8");
            MovieCastMember member = MovieCastMember.retrieveByNameAndType(memberName, type);
            if(member == null)
            {
                member = new MovieCastMember();
                member.setName(memberName);
                member.setMemberType(type);
                member.save();
            }
            MovieCastMemberData movieMemberData = new MovieCastMemberData();
            movieMemberData.setCastMemberId(member.getId());
            movieMemberData.setMovieId(movieid);
            movieMemberData.save();
        }
    }
    
    private String[] getArray(JSONObject data, String key)
    {
        Object dataValue = data.get(key);
        String[] returnPieces = new String[0];
        if(dataValue != null)
        {
            returnPieces = dataValue.toString().split(", ");
        }
        return returnPieces;
    }
    
    private void importErsb()
    {
        String[] ratings = { "G", "PG", "PG-13", "R", "NC-17" };
        int value = 1;
        for(String rating : ratings)
        {
            MovieErsbRating m_rating = new MovieErsbRating(rating, value);
            m_rating.save();
            value++;
        }
    }
    private void importGenres()
    {
        try 
        {
            String itemSeedPath = this.getItemSeedPath("u.genre");
            BufferedReader reader = new BufferedReader(new FileReader(itemSeedPath));
            String recordLine = reader.readLine();
            while(recordLine != null && !recordLine.isEmpty())
            {
                String[] properties = recordLine.split("\\|");
                String name = properties[0];
                int value = Integer.parseInt(properties[1]);
                new MovieGenre(name, value).save();
                recordLine = reader.readLine();
            }
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(MovieScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void importUsers()
    {
        try 
        {
            String itemSeedPath = this.getItemSeedPath("u.user");
            BufferedReader reader = new BufferedReader(new FileReader(itemSeedPath));
            String recordLine = reader.readLine();
            while(recordLine != null && !recordLine.isEmpty())
            {
                String[] properties = recordLine.split("\\|");
                int ml_id = Integer.parseInt(properties[0]);
                System.out.println("User: " + ml_id);
                int age = Integer.parseInt(properties[1]);
                String gender = properties[2];
                MovieUserOccupation occObj = MovieUserOccupation.retrieveByName(properties[3]);
                String occupation = occObj.getId();
                String zipcode = properties[4];
                MovieUser user = new MovieUser(ml_id, age, gender, occupation, zipcode);
                user.setEmail("user" + ml_id + "@default.def");
                user.setName("user" + ml_id);
                user.save();
                recordLine = reader.readLine();
            }
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(MovieScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void importOccupations()
    {
        try 
        {
            int count = 0;
            String itemSeedPath = this.getItemSeedPath("u.occupation");
            BufferedReader reader = new BufferedReader(new FileReader(itemSeedPath));
            String occupation = reader.readLine();
            while(occupation != null && !occupation.isEmpty())
            {
                new MovieUserOccupation(occupation, count).save();
                count++;
                
                occupation = reader.readLine();
            }
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(MovieScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() 
    {
        //this.importOccupations();
        //this.importUsers();
        //this.importGenres();
        //this.importErsb();
    	
//        this.cleanMovieData();
//        this.importMovies();

        //this.updateUserCredentials();
        //this.importRatings();
        //this.updateRatings();
//    	this.updateTrailers();
    	this.updateImageDirectory();
    }
    
    private void updateImageDirectory()
    {
        File imageDir = new File(this.getItemPosterPath());
        for(File image : imageDir.listFiles())
        {
        	String filename = image.getName().split("\\.")[0];
        	MovieItem movie = MovieItem.retrieveById(MovieItem.class, filename);
        	if(movie == null)
        	{
        		image.delete();
        		System.out.println("deleting - " + filename);
        	}
        }
    }
    
    private void updateTrailers()
    {
    	Query query = new Query("movieitem");
    	for(MovieItem mItem : query.run(MovieItem.class))
    	{
    		this.updateWithTrailer(mItem);
    	}
    }
    
	private void updateWithTrailer(MovieItem mItem)
	{
    	TheMovieDbApi service;
		try 
		{
			service = new TheMovieDbApi(DefaultSettings.getCurrent().getTmdbApiKey());
        	TmdbResultsList<MovieDb> results = service.searchMovie(mItem.getTitle(), mItem.getYear(), "en", false, 0);
        	for(MovieDb mv : results.getResults())
        	{        		
        		MovieDb movie = service.getMovieInfo(mv.getId(), "en");
    			System.out.println("Updating movie: " + movie.getTitle());
        		if(movie != null)
        		{
	        		if(movie.getImdbID().equalsIgnoreCase(mItem.getImdbId()))
	        		{
	                    String url_template = "http://api.themoviedb.org/3/movie/%s/trailers?api_key=" + DefaultSettings.getCurrent().getTmdbApiKey();
	                    String url_text = String.format(url_template, movie.getId());
	                    URL new_url = new URL(url_text);
	                    JSONObject trailerdata = (JSONObject) parser.parse(new InputStreamReader(new_url.openStream()));
	                    if(trailerdata.get("youtube") != null)
	                    {
	                        JSONArray data = (JSONArray) parser.parse(trailerdata.get("youtube").toString());
	                        if(!data.isEmpty())
	                        {
		                        JSONObject trailer = (JSONObject)data.get(0); //just take the first one and do not care about the other ones.
		                        String source = trailer.get("source").toString();
		        				System.out.println("\tSource: " + source);
		        				mItem.setTrailerurl(source);
	                        }
	                    }
	                    else if(trailerdata.get("quicktime") != null)
	                    {
	                        JSONArray data = (JSONArray) parser.parse(trailerdata.get("quicktime").toString());
	                        if(!data.isEmpty())
	                        {
		                        JSONObject trailer = (JSONObject)data.get(0); //just take the first one and do not care about the other ones.
		                        String source = trailer.get("source").toString();
		        				System.out.println("\tSource: " + source);
		        				mItem.setTrailerurl(source);
	                        }
	                    }
	                    
	        			mItem.save();
	        			break;
	        		}
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
