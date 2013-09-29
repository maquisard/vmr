/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */

public class AttributeCollection
{
    private static Map<String, Map<String, Map<String, Item>>> collection = new HashMap<>();
    
    public static <T extends Item> void add(String attrKey, String attrValue, T item)
    {
        ensureInitialization(attrKey, attrValue);
        collection.get(attrKey).get(attrValue).put(item.getItemId(), item);
    }
    
    public static <T extends Item> Map<String, Item> getCluster(String attrKey, String attrValue)
    {
        ensureInitialization(attrKey, attrValue);
        return collection.get(attrKey).get(attrValue);
    }
    
    public static <T extends Item> Map<String, MovieItem> getItems(String attrKey)
    {
        Map<String, MovieItem> toReturn = new HashMap<>();
        for(String attValue : collection.get(attrKey).keySet())
        {
            for (Iterator<Item> it = getCluster(attrKey, attValue).values().iterator(); it.hasNext();) {
                MovieItem item = (MovieItem) it.next();
                toReturn.put(item.getItemId(), item);
            }
        }
        
        return toReturn;
    }
    
    private static void ensureInitialization(String attrKey, String attrValue)
    {
        if(!collection.containsKey(attrKey))
        {
            collection.put(attrKey, new HashMap<String, Map<String, Item>>());
        }
        if(!collection.get(attrKey).containsKey(attrValue))
        {
            collection.get(attrKey).put(attrValue, new HashMap<String, Item>());
        }
    }
    
    public static List<String> getAttributeKeys()
    {
        List<String> attKeys = new ArrayList<>();
        for(String key : collection.keySet())
        {
            attKeys.add(key);
        }       
        return attKeys;
    }
    
    public static List<String> getAttributeValues(String attKey)
    {
        List<String> attValues = new ArrayList<>();
        for(String value : collection.get(attKey).keySet())
        {
            attValues.add(value);
        }
        return attValues;
    }
    
    
    public static int size()
    {
        return collection.size();
    }
    
    public static boolean isEmpty()
    {
        return collection.isEmpty();
    }
    
    public static void clear()
    {
//        for(String attKey : AttributeCollection.getAttributeKeys())
//        {
//            for(String attValue : AttributeCollection.getAttributeValues(attKey))
//            {
//                collection.get(attKey).get(attValue).clear();
//            }
//            collection.get(attKey).clear();
//        }
        collection.clear();
    }
    
    public static void printToScreen()
    {
        for(String attKey : AttributeCollection.getAttributeKeys())
        {
            System.out.print(attKey + " ---> ");
            for(String attValue : AttributeCollection.getAttributeValues(attKey))
            {
                System.out.print(attValue + " ");
            }
           System.out.println();
        }
    }

}
