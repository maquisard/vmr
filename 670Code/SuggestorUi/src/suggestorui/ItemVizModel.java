/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.Map;
import org.graphstream.ui.swingViewer.Viewer;
import webclient.Item;
import webclient.User;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemVizModel<T extends Item>
{
    private ItemGraph<T> graph;
        
    public ItemVizModel(Map<String, T> recommendations)
    {                
        graph = new ItemGraph<>(recommendations);                
    }
    
    public void SetView(Viewer view)
    {
        this.graph.SetView(view);
    }
        
    public ItemGraph<T> getGraph()
    {
        return this.graph;
    }
    
    public void updateGraph(Map<String, T> recommendations, String attKey, String attValue)
    {
        graph.updateGraph(recommendations, attKey, attValue);
    }
    
    public void updateGraph(Map<String, T> recommendations, String attKey)
    {
        graph.updateGraph(recommendations, attKey);
    }
    
    public void selectItem(String itemId)
    {        
        this.graph.setSelectedNode(itemId);
    }
}
