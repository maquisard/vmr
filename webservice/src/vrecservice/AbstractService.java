package vrecservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.xml.ws.WebServiceException;

import vrec.Configuration;
import vrec.data.User;

public abstract class AbstractService 
{
	@Context protected HttpServletRequest request;
	private String user_key = "currentuser";

	
	public AbstractService()
	{
		Configuration.initStorage();
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends User> T getCurrentUser()
	{
		HttpSession session = request.getSession();
        if (session == null)
            throw new WebServiceException("No session in the context");       
        
        T user = (T)session.getAttribute(user_key);
        return user;
	}
	
	protected <T extends User> void setCurrentUser(T user)
	{
		HttpSession session = request.getSession();
        if (session == null)
            throw new WebServiceException("No session in the context");
        
        session.setAttribute(user_key, user);
	}
}
