/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import vrec.DefaultSettings;

/**
 *
 * @author El Zede
 */
public class ScraperFactory 
{
    @SuppressWarnings("rawtypes")
	public static Scraper getCurrentScraper()
    {
        try 
        {
            String classFullName = DefaultSettings.getCurrent().getItemPackage() 
                           + "." + DefaultSettings.getCurrent().getScraperClass();
            Class<?> scraperClass = Class.forName(classFullName);
            Constructor[] constructors = scraperClass.getConstructors();
            if(constructors.length == 0)
            {
                throw new Exception("The scraper wrapper must have at least one constructor");
            }
            return (Scraper)constructors[0].newInstance();
        } catch (Exception ex) {
            Logger.getLogger(ScraperFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
