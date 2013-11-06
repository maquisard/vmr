/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrec.data.movie;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import core.Entity;
import core.Query;

/**
 *
 * @author El Zede
 */
public class MovieCastMember extends Entity 
{
    public static final String ACTOR = "actor";
    public static final String DIRECTOR = "director";
    public static final String WRITER = "writer";
    
    private String memberType;
    private String name;
    
    public MovieCastMember() { }
    public MovieCastMember(String name, String memberType)
    {
        this.name = name;
        this.memberType = memberType;
    }
    
    public static MovieCastMember retrieveByNameAndType(String name, String type)
    {
        Query query = new Query(MovieCastMember.class.getSimpleName());
        query.filter("name", name);
        query.filter("membertype", type);
        List<MovieCastMember> members = query.run(MovieCastMember.class);
        if(members.size() == 1) return members.get(0);
        
        return null;
    }
    
    public void decodeName()
    {
    	try 
    	{
			name = URLDecoder.decode(this.name, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    

    /**
     * @return the memberType
     */
    public String getMemberType() {
        return memberType;
    }

    /**
     * @param memberType the memberType to set
     */
    public void setMemberType(String memberType) {
        this.memberType = memberType;
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
}
