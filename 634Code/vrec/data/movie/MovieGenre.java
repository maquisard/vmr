/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data.movie;

import core.Entity;
import core.Query;
import java.util.List;
import vrec.DefaultSettings;

/**
 *
 * @author El Zede
 */
public class MovieGenre extends Entity 
{
    private String name;
    private int value;
    
    public MovieGenre() { }
    
    public MovieGenre(String name, int value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    public static MovieGenre retrieveByValue(int value)
    {
        Query query = new Query(MovieGenre.class.getSimpleName());
        query.filter("value", value);
        List<MovieGenre> genres = query.run(MovieGenre.class);
        if(genres.size() == 1) return genres.get(0);
        
        return DefaultSettings.getCurrent().getDefaultMovieGenre();
    }

    public static MovieGenre retrieveByName(String name)
    {
        Query query = new Query(MovieGenre.class.getSimpleName());
        query.filter("name", name);
        List<MovieGenre> genres = query.run(MovieGenre.class);
        if(genres.size() == 1) return genres.get(0);
        
        return DefaultSettings.getCurrent().getDefaultMovieGenre();
    }
}
