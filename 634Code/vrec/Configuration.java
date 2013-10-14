/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec;

import java.util.ResourceBundle;

/**
 *
 * @author El Zede
 */
public class Configuration 
{
    private static ResourceBundle resource = ResourceBundle.getBundle("vrec.configuration");
            
    public static void initStorage()
    {
        configuration.Configuration.getCurrent().setResource(resource);
    }

}
