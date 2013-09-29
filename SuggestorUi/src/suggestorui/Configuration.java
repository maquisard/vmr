/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.ResourceBundle;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class Configuration
{
    private static Configuration configuration = new Configuration();
    private ResourceBundle resourcebundle = null;
    
    private Configuration( ) 
    { 
        resourcebundle = ResourceBundle.getBundle("suggestorui.suggestorui");
    }
    
    public static <T> T getValue(String key, Class<T> classname)
    {
        return (T)configuration.resourcebundle.getObject(key);
        
    }
    
    public static String getValue(String key)
    {
        if(!configuration.resourcebundle.containsKey(key))
        {
            return "";
        }
        return configuration.resourcebundle.getString(key);
    }

}
