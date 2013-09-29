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
public class ResetBasketMessage extends SuggestorMessage
{

    public ResetBasketMessage()
    {
        
    }
    
    @Override
    public String getName() 
    {
        return "Reset Basket";
    }

    @Override
    public String getOperation() 
    {
        return "ResetBasket";
    }

    @Override
    public int getResponseType() 
    {
        return SuggestorMessage.VALUE_RESPONSE;
    }
    
}
