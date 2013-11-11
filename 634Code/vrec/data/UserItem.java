package vrec.data;

import core.Entity;
import core.FilterNode;
import core.Query;

public class UserItem extends Entity
{
	
	public static int NONE = 0;
	public static int QUEUE = 1;
	public static int BROWSED = 2;
	public static int WATCHED = 3;
	
    private String itemid;
    private String userid;
    private int status;
    private String itemtype;
    
    
    public static boolean exist(UserItem useritem)
    {
    	Query query = new Query("useritem");
    	query.filter(new FilterNode("itemid", useritem.getItemid())
    			.and(new FilterNode("userid", useritem.getUserid())
    			.and(new FilterNode("status", useritem.getStatus())
    			.and(new FilterNode("itemtype", useritem.getItemtype())))));
    	
    	return query.run(UserItem.class).size() == 1;
    }
    
	/**
	 * @return the itemid
	 */
	public String getItemid() {
		return itemid;
	}
	/**
	 * @param itemid the itemid to set
	 */
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
}
