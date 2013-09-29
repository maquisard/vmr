/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

/**
 *
 * @author El Zede
 * @ CSDL
 */
public class GetRandomItemMessage extends SuggestorMessage 
{

    public GetRandomItemMessage(int k)
    {
        this.addParameter("n", new Integer(k).toString());
    }
    
    @Override
    public String getName() 
    {
        return "Get Random Items";
    }

    @Override
    public String getOperation() 
    {
        return "GetRandomItems";
    }

    @Override
    public int getResponseType() 
    {
        return SuggestorMessage.ENTITY_LIST_RESPONSE;
    }
    
}
