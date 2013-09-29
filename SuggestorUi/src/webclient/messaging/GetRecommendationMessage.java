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
public class GetRecommendationMessage extends SuggestorMessage
{
    public GetRecommendationMessage(int n)
    {
        this.addParameter("n", new Integer(n).toString());
    }

    @Override
    public String getName() 
    {
        return "Get Recommendations ";
    }

    @Override
    public String getOperation() 
    {
        return "GetRecommendedItems";
    }

    @Override
    public int getResponseType() 
    {
        return SuggestorMessage.ENTITY_LIST_RESPONSE;
    }
}
