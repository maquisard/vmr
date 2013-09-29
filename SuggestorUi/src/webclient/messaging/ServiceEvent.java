/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

import java.util.EventObject;
import webclient.messaging.SuggestorClient.SuggestorResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ServiceEvent extends EventObject
{
    private SuggestorResponse response;
    private SuggestorMessage message;
    
    public ServiceEvent(SuggestorMessage message, SuggestorResponse response)
    {
        super(message);
    }
    
    public SuggestorMessage getMessage()
    {
        return message;
    }
    
    public SuggestorResponse getResponse()
    {
        return response;
    }
}
