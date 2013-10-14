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
public class MovieCastMemberData extends Entity
{
    private String castMemberId;
    private String movieId;
    
    public MovieCastMemberData() { }
    public MovieCastMemberData(String castMemberId, String movieId)
    {
        this.castMemberId = castMemberId;
        this.movieId = movieId;
    }

    /**
     * @return the castMemberId
     */
    public String getCastMemberId() {
        return castMemberId;
    }

    /**
     * @param castMemberId the castMemberId to set
     */
    public void setCastMemberId(String castMemberId) {
        this.castMemberId = castMemberId;
    }

    /**
     * @return the movieId
     */
    public String getMovieId() {
        return movieId;
    }

    /**
     * @param movieId the movieId to set
     */
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
    
}
