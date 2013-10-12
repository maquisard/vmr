/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import com.alee.extended.window.WebProgressDialog;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import suggestorui.views.ItemVizView;
import webclient.messaging.ServiceEvent;
import webclient.messaging.ServiceEventListener;
import webclient.messaging.SuggestorClient;
import webclient.messaging.SuggestorClient.SuggestorResponse;
import webclient.messaging.SuggestorMessage;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ServiceWorker extends SwingWorker<SuggestorResponse, SuggestorResponse> 
{
    private static ServiceWorker instance = null;
    private List<ServiceEventListener> listeners = new ArrayList<>();
    private WebProgressDialog asyncProgress;
    private SuggestorMessage message;
    
    
    private ServiceWorker()
    {
        
    }
    
    public static ServiceWorker getInstance()
    {
        if(instance == null)
        {
            instance = new ServiceWorker();
        }
        return instance;
    }
    
    

    public void createNewAsyncProgress(ItemVizView view)
    {
        asyncProgress = new WebProgressDialog(view, "Fetching Movies");
        asyncProgress.setText("Please wait...");
    }
    
    @Override
    public void done()
    {
        asyncProgress.setVisible(false);
    }

    @Override
    public SuggestorResponse doInBackground() 
    {
        asyncProgress.setVisible(true);
        asyncProgress.setModal(true);
        return SuggestorClient.getCurent().sendMessage(message);
    }
    
    
    public void addServiceEventListener(ServiceEventListener listener)
    {
        this.listeners.add(listener);
    }
    
    public void removeServiceEventListener(ServiceEventListener listener)
    {
        this.listeners.remove(listener);
    }
    
    public synchronized void fireOnPreCommunicationEvents(ServiceEvent event)
    {
        for(ServiceEventListener listener : listeners)
        {
            listener.onPreCommunication(event);
        }
    }
    
    public synchronized void fireOnPostCommunicationEvents(ServiceEvent event)
    {
        for(ServiceEventListener listener : listeners)
        {
            listener.onPostCommunication(event);
        }
    }

    /**
     * @return the message
     */
    public SuggestorMessage getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(SuggestorMessage message) {
        this.message = message;
    }


    
}
