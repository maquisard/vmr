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
public class AddBasketItemMessage extends SuggestorMessage 
{
    public AddBasketItemMessage(String itemId, int quantity)
    {
        this.addParameter("itemid", itemId);
        this.addParameter("quantity", new Integer(quantity).toString());
    }

    @Override
    public String getName() 
    {
        return "Add Basket Item";
    }

    @Override
    public String getOperation() 
    {
        return "AddBasketItem";
    }

    @Override
    public int getResponseType() 
    {
        return SuggestorMessage.VALUE_RESPONSE;
    }
}
