/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class UserMessage extends SuggestorMessage 
{
    public static String GETUSER = "GetUser";
    public static String SELECTUSER = "SelectUser";
    public static String SELECTRANDOMUSER = "SelectRandomUser";
    
    private String operation;
    
    public UserMessage(String userid, String operation)
    {
        this.operation = operation;
        if(!this.operation.equals(UserMessage.SELECTRANDOMUSER))
        {
            this.addParameter("id", userid);
        }
    }
    
    @Override
    public String getName() 
    {
        return "Get User";
    }

    @Override
    public String getOperation() 
    {
        return this.operation;
    }

    @Override
    public int getResponseType() 
    {
        return SuggestorMessage.ENTITY_RESPONSE;
    }
    
}
