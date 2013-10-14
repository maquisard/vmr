/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec;

import messages.EmailTemplate;
import vrec.data.movie.MovieErsbRating;
import vrec.data.movie.MovieGenre;
import vrec.data.movie.MovieUserOccupation;

/**
 *
 * @author El Zede
 */
public class DefaultSettings extends settings.DefaultSettings 
{
    private static DefaultSettings current = null;
    private EmailTemplate resetPasswordTemplate = null;
    private MovieUserOccupation defaultMUO = null;
    private MovieGenre defaultMG = null;
    private MovieErsbRating defaultContentRating = null;

    protected DefaultSettings()
    {
        super();
    }
    
    public static DefaultSettings getCurrent()
    {
        if(current == null)
        {
            current = new DefaultSettings();
        }
        return current;
    }
    
    public String getScraperClass()
    {
        return getValue("scraper.class");
    }
    
    public String getItemClass()
    {
        return getValue("item.class");
    }
    
    public String getUserClass()
    {
        return getValue("user.class");
    }
    
    public String getItemPackage()
    {
        return getValue("item.package");
    }
    
    public MovieErsbRating getDefaultContentRating()
    {
        if(defaultContentRating == null)
        {
            defaultContentRating = MovieErsbRating.retrieveById(MovieErsbRating.class, getValue("default.contentrating.id"));
        }
        return defaultContentRating;
    }
    
    public MovieGenre getDefaultMovieGenre()
    {
        if(defaultMG == null)
        {
            defaultMG = MovieGenre.retrieveById(MovieGenre.class, getValue("default.moviegenre.id"));
        }
        return defaultMG;
    }
    
    public MovieUserOccupation getDefaultMovieUserOccupation()
    {
        if(defaultMUO == null)
        {
            defaultMUO = MovieUserOccupation.retrieveById(MovieUserOccupation.class, getValue("default.movieuseroccupation.id"));
        }
        return defaultMUO;
    }
            
    public EmailTemplate getResetPasswordEmailTemplate()
    {
        if(resetPasswordTemplate == null)
        {
            resetPasswordTemplate = EmailTemplate.retrieveById(EmailTemplate.class, getValue("template.password.reset.id"));
        }
        return resetPasswordTemplate;
    }
        
    public int getProfileImageWidth()
    {
        return Integer.parseInt(getValue("profile.image.width"));
    }

    public int getProfileImageHeight()
    {
        return Integer.parseInt(getValue("profile.image.height"));
    }
    
    public String getTmdbApiKey()
    {
    	return getValue("tmdb.api.key");
    }
}
