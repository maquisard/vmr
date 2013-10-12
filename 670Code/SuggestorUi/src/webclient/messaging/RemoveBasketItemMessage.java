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
public class RemoveBasketItemMessage extends AddBasketItemMessage
{
    public RemoveBasketItemMessage(String itemId, int quantity)
    {
        super(itemId, quantity);
    }

    @Override
    public String getOperation() 
    {
        return "RemoveBasketItem";
    }

    @Override
    public String getName() 
    {
        return "Remove Basket Item";
    }    
}
