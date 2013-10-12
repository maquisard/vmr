/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

import suggestorui.Configuration;

/**
 *
 * @author El Zede
 * @ CSDL
 */
public class GetBasketMessage extends SuggestorMessage
{

    @Override
    public String getName() 
    {
        return "Get Basket";
    }
    
    @Override
    public String getOperation() 
    {
        return "GetBasket";
    }

    @Override
    public int getResponseType() 
    {
        return SuggestorMessage.ENTITY_LIST_RESPONSE;
    }
}
