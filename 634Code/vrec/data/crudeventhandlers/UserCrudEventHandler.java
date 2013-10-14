/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data.crudeventhandlers;

import core.CrudEventHandler;
import core.Entity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vrec.DefaultSettings;
import vrec.data.User;

/**
 *
 * @author El Zede
 */
public class UserCrudEventHandler extends CrudEventHandler
{
    @Override
    public boolean onCreating(Entity entity) 
    {
        User user = (User)entity;
        String username = (String) user.getPropertyValue(DefaultSettings.getCurrent().getUsernameProperty());
        String password = (String) user.getPropertyValue(DefaultSettings.getCurrent().getPasswordProperty());
        String encodedPassword = this.encodeCredentials(username + password);
        user.setPassword(encodedPassword);
        return true;
    }

    @Override
    public boolean onUpdating(Entity localImage, Entity serverImage) 
    {
        String usernameProperty = DefaultSettings.getCurrent().getUsernameProperty();
        String passwordProperty = DefaultSettings.getCurrent().getPasswordProperty();
        
        String localUsername = (String) localImage.getPropertyValue(usernameProperty);
        String localPassword = (String) localImage.getPropertyValue(passwordProperty);

        String serverUsername = (String) serverImage.getPropertyValue(usernameProperty);
        String serverPassword = (String) serverImage.getPropertyValue(passwordProperty);
        
        if(!localUsername.equals(serverUsername) || !localPassword.equals(serverPassword))
        {
            String encodedPassword = this.encodeCredentials(localUsername + localPassword);
            ((User)localImage).setPassword(encodedPassword);
        }
        return true;
    }
    
    private String encodeCredentials(String credentials)
    {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(credentials.getBytes());
     
            byte byteData[] = md.digest();
     
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) 
            {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(UserCrudEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
