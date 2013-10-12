/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class GetExtendedMovieRecommendationsMessage extends GetMovieRecommendationsMessage
{
    public GetExtendedMovieRecommendationsMessage(String movieId, int nFirstUsers, String attKey, String attValue, int k)
    {
        super(nFirstUsers, k);
        this.addParameter("movieId", movieId);
        this.addParameter("filterKey", attKey);
        this.addParameter("filterValue", attValue);
    }

    @Override
    public String getOperation() 
    {
        return "GetExtendedMovieRecommendations";
    }
    
}
