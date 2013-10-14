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
public class MovieUserOccupation extends Entity
{
    private int value;
    private String occupation;
    
    public MovieUserOccupation()
    {
    }
    
    public MovieUserOccupation(String occupation, int value)
    {
        this.occupation = occupation;
        this.value = value;
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

    /**
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * @param occupation the occupation to set
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    
    public static MovieUserOccupation retrieveByName(String name)
    {
        Query query = new Query(MovieUserOccupation.class.getSimpleName());
        query.filter("occupation", name);
        List<MovieUserOccupation> occupations = query.run(MovieUserOccupation.class);
        if(occupations.size() == 1) return occupations.get(0);
        
        return DefaultSettings.getCurrent().getDefaultMovieUserOccupation();
    }
}
