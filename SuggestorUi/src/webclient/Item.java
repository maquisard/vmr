/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import webclient.messaging.GetRandomItemMessage;
import webclient.messaging.RemoveBasketItemMessage;
import webclient.messaging.AddBasketItemMessage;
import webclient.messaging.SuggestorClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import suggestorui.Configuration;
import webclient.messaging.SuggestorClient.SuggestorItemListResponse;
import webclient.messaging.SuggestorClient.SuggestorValueResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */


public abstract class Item
{
    
    public Item( )
    {
    }
    
    public void removeFromBasket()
    {
        RemoveBasketItemMessage message = new RemoveBasketItemMessage(this.getItemId(), this.getQuantity());
        SuggestorValueResponse response = (SuggestorValueResponse) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
        }
    }
    
    public void addToBasket()
    {
        AddBasketItemMessage message = new AddBasketItemMessage(this.getItemId(), this.getQuantity());
        SuggestorValueResponse response = (SuggestorValueResponse) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
        }
    }
    
    public abstract String getItemId();
    public abstract int getQuantity();
    
    public static <T extends Item> Map<String, T> getRandomItems()
    {
        int k = Integer.parseInt(Configuration.getValue("sizeofrandomitems"));
        GetRandomItemMessage message = new GetRandomItemMessage(k);
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
    
    public boolean onCreated() { return true; }
        
    public static <T extends Item> T createFromXml(Class<T> classname, String xml)
    {
        Serializer serializer = new Persister();
        try 
        {
            T returnValue = serializer.read(classname, xml);
            if(returnValue.onCreated())
            {
                return returnValue;
            }
        } catch (Exception ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
