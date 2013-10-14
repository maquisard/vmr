/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data.movie;

import core.Entity;

/**
 *
 * @author El Zede
 */
public class MovieGenreData extends Entity
{
    private String movieid;
    private String genreid;
    
    public MovieGenreData() { }
    public MovieGenreData(String movieid, String genreid)
    {
        this.movieid = movieid;
        this.genreid = genreid;
    }

    /**
     * @return the movieid
     */
    public String getMovieid() {
        return movieid;
    }

    /**
     * @param movieid the movieid to set
     */
    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    /**
     * @return the genreid
     */
    public String getGenreid() {
        return genreid;
    }

    /**
     * @param genreid the genreid to set
     */
    public void setGenreid(String genreid) {
        this.genreid = genreid;
    }
}
