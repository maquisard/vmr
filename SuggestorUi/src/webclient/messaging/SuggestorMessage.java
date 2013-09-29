/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public abstract class SuggestorMessage 
{
    private List<NameValuePair> nameValuePairs = new ArrayList<>(1);
    
    public static int VALUE_RESPONSE = 0;
    public static int ENTITY_RESPONSE = 2;
    public static int ENTITY_LIST_RESPONSE = 1;
    
    public SuggestorMessage( ) { }
    protected void addParameter(String key, String value)
    {
        nameValuePairs.add(new BasicNameValuePair(key, value));
    }
    
    public List<NameValuePair> getParameters()
    {
        return nameValuePairs;
    }
    
    public abstract String getName();
    public abstract String getOperation();
    public abstract int getResponseType();
}
