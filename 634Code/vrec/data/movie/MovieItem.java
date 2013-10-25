/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data.movie;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import vrec.DefaultSettings;
import vrec.data.Item;
import core.FilterNode;
import core.Join;
import core.Query;

/**
 *
 * @author El Zede
 */
public class MovieItem extends Item
{
    private String ersbRatingid = DefaultSettings.getCurrent().getDefaultContentRating().getId();
    private String runtime = "0 mins";
    private float imdbRating = 0.0f;
    private String imdbId = "000000";
    private int ml_id = -1;
    private String trailerurl = "http://www.google.com";
    
    private List<MovieCastMember> castMembers;
    private List<MovieGenre> genres;
    private MovieErsbRating ersb = null;
    
    public MovieItem() { }
    
    public MovieErsbRating retrieveErsbRating()
    {
        if(ersb == null)
        {
            ersb = MovieErsbRating.retrieveById(MovieErsbRating.class, this.ersbRatingid);
        }
        return ersb;
    }
    
    public static MovieItem retrieveByImdbId(String imdbid)
    {
        Query query = new Query("movieitem");
        query.filter("imdbid", imdbid);
        List<MovieItem> items = query.run(MovieItem.class);
        if(items.isEmpty()) return null;
        
        return items.get(0);
    }
    
    public List<MovieCastMember> retrieveDirectors()
    {
        return this.retrieveMembersByType(MovieCastMember.DIRECTOR);
    }
    
    public List<MovieCastMember> retrieveActors()
    {
        return this.retrieveMembersByType(MovieCastMember.ACTOR);
    }

    public List<MovieCastMember> retrieveWriters()
    {
        return this.retrieveMembersByType(MovieCastMember.WRITER);
    }
    
    private List<MovieCastMember> retrieveMembersByType(String type)
    {
        List<MovieCastMember> returnMembers = new ArrayList<MovieCastMember>(); 
        for(MovieCastMember member : this.retrieveCastMembers())
        {
            if(member.getMemberType().equals(type))
            {
                returnMembers.add(member);
            }
        }
        return returnMembers;
    }
    
    public List<MovieCastMember> retrieveCastMembers()
    {
        if(castMembers == null || castMembers.isEmpty())
        {
            Query query = new Query("moviecastmember");
            Join join = new Join("moviecastmember", "id", "moviecastmemberdata", "castmemberid");
            join.filter(new FilterNode("movieid", id));
            query.join(join);
            castMembers = query.run(MovieCastMember.class);
        }
        return castMembers;
    }
    
    public List<MovieGenre> retrieveGenres()
    {
        if(genres == null || genres.isEmpty())
        {
            Query query = new Query("moviegenre");
            Join join = new Join("moviegenre", "id", "moviegenredata", "genreid");
            join.filter(new FilterNode("movieid", id));
            query.join(join);
            genres = query.run(MovieGenre.class);
        }
        return genres;
    }

    /**
     * @return the ersbRatingid
     */
    public String getErsbRatingid() {
        return ersbRatingid;
    }

    /**
     * @param ersbRatingid the ersbRatingid to set
     */
    public void setErsbRatingid(String ersbRatingid) {
        this.ersbRatingid = ersbRatingid;
    }

    /**
     * @return the runtime
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * @param runtime the runtime to set
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    /**
     * @return the imdbRating
     */
    public float getImdbRating() {
        return imdbRating;
    }

    /**
     * @param imdbRating the imdbRating to set
     */
    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
    }

    /**
     * @return the imdbId
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * @param imdbId the imdbId to set
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    /**
     * @return the ml_id
     */
    public int getMl_id() {
        return ml_id;
    }

    /**
     * @param ml_id the ml_id to set
     */
    public void setMl_id(int ml_id) {
        this.ml_id = ml_id;
    }

    @Override
    public String buildBagOfWords() 
    {
        String bag = super.buildBagOfWords();
        for(MovieCastMember member : this.retrieveCastMembers())
        {
            try {
                bag += " " + URLDecoder.decode(member.getName(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MovieItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bag;
    }

	/**
	 * @return the trailerurl
	 */
	public String getTrailerurl() {
		return trailerurl;
	}

	/**
	 * @param trailerurl the trailerurl to set
	 */
	public void setTrailerurl(String trailerurl) {
		this.trailerurl = trailerurl;
	}
    
}
