/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data;

import vrec.DefaultSettings;

/**
 *
 * @author El Zede
 */
public abstract class Scraper 
{
    public abstract void run();
    
    protected String getItemSeedPath(String filename)
    {
        return "seeds/" + 
            DefaultSettings.getCurrent().getItemClass().toLowerCase() + 
            "/" + filename;
    }
    
    protected String getItemPosterPath()
    {
        return "images/" + 
            DefaultSettings.getCurrent().getItemClass().toLowerCase() + "/";
    }
}
