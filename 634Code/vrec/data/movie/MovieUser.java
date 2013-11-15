/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.persist.annotations.NoColumn;
import vrec.DefaultSettings;
import vrec.data.JSONFilterable;
import vrec.data.User;
import vrec.data.UserItem;
import core.FilterNode;
import core.Join;
import core.Query;
import flexjson.JSONSerializer;

/**
 *
 * @author El Zede
 */
public class MovieUser extends User implements JSONFilterable
{
    private int ml_id;
    private int age;
    private String gender;
    private String occupationid;
    private String zipcode;
    
    
    private MovieUserOccupation occupation = null;
    
    public MovieUser()
    {
    }
    
    public MovieUser(int ml_id, int age, String gender, String occupationid, String zipcode)
    {
        this.ml_id = ml_id;
        this.age = age;
        this.gender = gender;
        this.occupationid = occupationid;
        this.zipcode = zipcode;
    }
    
    public static MovieUser retrieveUserByMlId(int ml_id)
    {
    	if(ml_id < 0) return null;
    	
    	Query query = new Query("movieuser");
    	query.filter("ml_id", ml_id);
    	List<MovieUser> results = query.run(MovieUser.class);
    	if(results.size() != 1) return null;
    	return results.get(0);
    }
    
    @NoColumn
    public MovieUserOccupation getOccupation()
    {
        if(occupation == null)
        {
            occupation = MovieUser.retrieveById(MovieUserOccupation.class, this.occupationid);
        }
        return occupation;
    }
    
    public static List<MovieUser> retrieveRandomUsers(int k)
    {
    	List<MovieUser> users = new ArrayList<MovieUser>();
    	Random random = new Random();
    	int min = 1;
    	int max = DefaultSettings.getCurrent().getUserMlMaxId();
    	for(int i = 0; i < k; i++)
    	{
    		int ml_id = random.nextInt(max - min + 1) + min;
    		MovieUser user = MovieUser.retrieveUserByMlId(ml_id);
    		users.add(user);
    	}
    	return users;
    }
    
    public void clearBrowsedMovies()
    {
    	Query query = new Query("useritem");
    	query.filter("userid", this.ml_id);
    	query.filter("status", UserItem.BROWSED);
    	for(UserItem useritem : query.run(UserItem.class))
    	{
    		useritem.delete();
    	}
    }
    
    public List<MovieItem> retrieveMovieQueue(int index, int size)
    {
    	Query query = this.retrieveMoviesByStatus(UserItem.QUEUE);
    	query.setLimitIndex(index);
    	query.setLimitSize(size);
    	return query.run(MovieItem.class);
    }
    
    public List<MovieItem> retrieveBrowsedMovies()
    {
    	return this.retrieveMoviesByStatus(UserItem.BROWSED).run(MovieItem.class);
    }
    
    public List<MovieItem> retrieveWatchedMovies(int index, int size)
    {
    	Query query = new Query("movieitem");
    	query.setLimitIndex(index);
    	query.setLimitSize(size);
    	//query.orderBy("imdbrating", OrderBy.DESC);
    	Join join = new Join("movieitem", "ml_id", "itemrating", "itemid");
    	query.join(join);
    	FilterNode filter = new FilterNode("userid", "" + this.ml_id);
    	filter.setTablename("itemrating");
    	query.filter(filter);
    	return query.run(MovieItem.class);
    }
    
    private Query retrieveMoviesByStatus(int status)
    {
    	Query query = new Query("movieitem");
    	Join join = new Join("movieitem", "ml_id", "useritem", "itemid");
    	query.join(join);
    	//query.orderBy("imdbrating", OrderBy.DESC);
    	FilterNode userFilter = new FilterNode("userid", "" + this.ml_id);
    	userFilter.setTablename("useritem");
    	FilterNode statusFilter = new FilterNode("status", status);
    	statusFilter.setTablename("useritem");
    	query.filter(userFilter.and(statusFilter));
    	return query;
    }

	@Override
	public JSONSerializer filter(JSONSerializer serializer) 
	{
		serializer = serializer.exclude("*.password", "*.occupationid", "*.id").include("*.occupation");
		return serializer;
	}
    
    
    /**
     * @return the ml_id
     */
    public int getMl_id() {
        return ml_id;
    }

    /**
     * @param ml_id the ml_id to set
     */
    public void setMl_id(int ml_id) {
        this.ml_id = ml_id;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the occupationid
     */
    public String getOccupationid() {
        return occupationid;
    }

    /**
     * @param occupationid the occupationid to set
     */
    public void setOccupationid(String occupationid) {
        this.occupationid = occupationid;
        this.occupation = null;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


}
