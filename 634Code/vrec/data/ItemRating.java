/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data;

import core.Entity;
import core.FilterNode;
import core.Join;
import core.Query;
import java.util.List;
import vrec.DefaultSettings;

/**
 *
 * @author El Zede
 */
public class ItemRating extends Entity
{
    private String itemId;
    private String userId;
    private float rating;
    private String itemtype;

    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(float rating) {
        this.rating = rating;
    }
    
    /**
     * @return the itemtype
     */
    public String getItemtype() {
        return itemtype;
    }

    /**
     * @param itemtype the itemtype to set
     */
    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }
    
    public <T extends Item> T retrieveItem(Class<T> classname)
    {
        String iType = DefaultSettings.getCurrent().getItemClass();
        String _classname = classname.getSimpleName();
        
        Query query = new Query(_classname);
        query.filter("id", itemId);
        Join join = new Join(_classname, "id", "itemrating", "itemid");
        join.filter(new FilterNode("itemtype", iType));
        query.join(join);
        
        List<T> results = query.run(classname);
        if(results.size() == 1) return results.get(0);
        return null;
    }

    public <T extends User> T retrieveUser(Class<T> classname)
    {
        Query query = new Query(classname.getSimpleName());
        query.filter("id", userId);
        List<T> results = query.run(classname);
        if(results.size() == 1) return results.get(0);
        return null;
    }

}
