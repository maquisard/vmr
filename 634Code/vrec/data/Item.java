/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data;

import java.util.ArrayList;
import java.util.List;

import net.sf.persist.annotations.NoColumn;
import core.Entity;

/**
 *
 * @author El Zede
 */
public abstract class Item extends Entity 
{
    protected String title = "No title available.";
    protected int year = -1;
    protected String description = "No description available.";
    protected String posterUrl = "images/default.png";
    protected List<ItemAttribute> attributes = new ArrayList<ItemAttribute>();

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the posterUrl
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * @param posterUrl the posterUrl to set
     */
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    
    public String buildBagOfWords()
    {
        return this.title + " " + this.description + " " + this.year;
    }

	/**
	 * @return the attributes
	 */
    @NoColumn
	public List<ItemAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
    @NoColumn
	public void setAttributes(List<ItemAttribute> attributes) {
		this.attributes = attributes;
	}
}
