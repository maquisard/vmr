/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import webclient.messaging.ResetBasketMessage;
import webclient.messaging.GetBasketMessage;
import webclient.messaging.GetRecommendationMessage;
import webclient.messaging.SuggestorClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import suggestorui.Configuration;
import webclient.messaging.SuggestorClient.SuggestorItemListResponse;
import webclient.messaging.SuggestorClient.SuggestorValueResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class Basket
{
    private static Basket instance = null;

    private Basket() { }
    
    public static Basket getIntance()
    {
        if(instance == null)
        {
            instance = new Basket();
        }
        return instance;
    }
    
    public void addItem(Item item)
    {
        item.addToBasket();
    }
    
    public void removeItem(Item item)
    {
        item.removeFromBasket();
    }
    
    public <T extends Item> Map<String, T> getItems()
    {
        GetBasketMessage message = new GetBasketMessage();
        SuggestorItemListResponse<T> response = (SuggestorItemListResponse<T>) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
            return new HashMap<>();
        }
        else
        {
            return response.getItems();
        }
    }
    
    public <T extends Item> Map<String, T> getRecommendations()
    {
        int k = Integer.parseInt(Configuration.getValue("sizeofrecommendeditems"));
        GetRecommendationMessage message = new GetRecommendationMessage(k);
        SuggestorItemListResponse<T> response = (SuggestorItemListResponse<T>) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
            return new HashMap<>();
        }
        else
        {
            return response.getItems();
        }
    }
    
    public void resetBasket()
    {
        ResetBasketMessage message = new ResetBasketMessage();
        SuggestorValueResponse response = (SuggestorValueResponse) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
        }
    }
}
