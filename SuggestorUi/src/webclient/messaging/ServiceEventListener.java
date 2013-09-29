/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

import java.util.EventListener;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public interface ServiceEventListener extends EventListener
{
    public void onPreCommunication(ServiceEvent event);
    public void onPostCommunication(ServiceEvent event);
}
