/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.graphstream.graph.EdgeFactory;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ElementSink;
import org.graphstream.ui.swingViewer.Viewer;
import suggestorui.views.ItemAttributeEvent;
import suggestorui.views.ItemAttributeEventListener;
import suggestorui.views.ItemVizView;
import webclient.AttributeCollection;
import webclient.Item;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemGraph<T extends Item> extends SingleGraph implements ItemAttributeEventListener, ElementSink
{
    
    private ItemNode<T> selectedNode;
    private Map<String, T> currentRecommendations;
    private String currentAttKey;
    private String currentAttValue;
    private Viewer viewer;
    
    public ItemGraph(final Map<String, T> recommendations)
    {
        super("Suggestor - Recommendations");
        this.currentRecommendations = recommendations;
        this.currentAttKey = Configuration.getValue("defaultAttributeKey");
    }  
    
    public void SetView(Viewer view)
    {
        this.viewer = view;
        initGraph();
        displayCurrentGraph();
    }
    
    public final void setRecommendations(final Map<String, T> recommendations)
    {
        setNodeFactory(new NodeFactory<ItemNode<T>>() {
            @Override
            public ItemNode<T> newInstance(String id, Graph graph) 
            {
                T item = recommendations.get(id);
                if(item == null)
                {
                    System.out.println("Why? Id" + id);
                }
                return new ItemNode<>((ItemGraph<T>)graph, item);
            }
        });
        
        setEdgeFactory(new EdgeFactory<ItemEdge<T>>(){
            @Override
            public ItemEdge<T> newInstance(String _id, Node _source, Node _target, boolean directed) 
            {
                ItemNode<T> sourceNode = (ItemNode<T>)_source;
                ItemNode<T> targetNode = (ItemNode<T>)_target;
                return new ItemEdge<>(sourceNode, targetNode);
            }
        });
    }
    
    public boolean buildFromItems(String attKey)
    {
        if(AttributeCollection.isEmpty())
        {
            return false;
        }
        //for(String attKey : AttributeCollection.getAttributeKeys())
        {
            //System.out.print(attKey + " ---> ");
            for(String attValue : AttributeCollection.getAttributeValues(attKey))
            {
                //System.out.println(attValue + " ");
                Item[] items = AttributeCollection.getCluster(attKey, attValue).values().toArray(new Item[0]);
                if(items.length == 1)
                {
                    this.addNodeItem((T)items[0]);
                    //break;
                }
                else
                {
                    for(int i = 0; i < items.length - 1; i++)
                    {
                        T source = (T)items[i];
                        T target = (T)items[i + 1];
                        this.addEdge(source, target);
                    }
                }
            }
            //System.out.println();
        }
        this.updateNodeStyles();
        return true;
    }
    
    public boolean buildFromItems()
    {
        if(AttributeCollection.isEmpty())
        {
            return false;
        }
        for(String attKey : AttributeCollection.getAttributeKeys())
        {
            //System.out.print(attKey + " ---> ");
            for(String attValue : AttributeCollection.getAttributeValues(attKey))
            {
                //System.out.print(attValue + " ");
                Item[] items = AttributeCollection.getCluster(attKey, attValue).values().toArray(new Item[0]);
                if(items.length == 1)
                {
                    this.addNodeItem((T)items[0]);
                    //break;
                }
                else
                {
                    for(int i = 0; i < items.length - 1; i++)
                    {
                        T source = (T)items[i];
                        T target = (T)items[i + 1];
                        this.addEdge(source, target);
                    }
                }
            }
            //System.out.println();
        }
        this.updateNodeStyles();
        return true;
    }
    
    public void highlight(String attKey, String attValue, String highlightClass)
    {
        for(ItemNode node : this.findNodes(attKey, attValue))
        {
            node.highlight(highlightClass);
        }
        
        for(ItemEdge edge : this.findEdges(attKey, attValue))
        {
           edge.highlight(highlightClass);
        }
    }
    
    public void restore(String attKey, String attValue)
    {
        List<ItemEdge> edges = this.findEdges(attKey, attValue);
        if(edges.isEmpty())
        {
            for(ItemNode node : this.findNodes(attKey, attValue))
            {
                ((Highlightable)node).restore();
            }
        }
        for(Highlightable edge : edges)
        {
            edge.restore();
        }
    }
    
    private List<ItemNode> findNodes(String attKey, String attValue)
    {
        List<ItemNode> nodes = new ArrayList<>();
        Item[] items = AttributeCollection.getCluster(attKey, attValue).values().toArray(new Item[0]);
        for(int i = 0; i < items.length; i++)
        {
            nodes.add(this.getNodeItem((T)items[i]));
        }
        return nodes;
    }
    
    private List<ItemEdge> findEdges(String attKey, String attValue)
    {
        List<ItemEdge> edges = new ArrayList<>();
        Item[] items = AttributeCollection.getCluster(attKey, attValue).values().toArray(new Item[0]);
        if(items.length > 1)
        {
            for(int i = 0; i < items.length - 1; i++)
            {
                String aNodeId = items[i].getItemId();
                String bNodeId = items[i + 1].getItemId();
                String edgeId = aNodeId + "-" + bNodeId;
                ItemEdge edge = this.getEdge(edgeId);
                if(edge == null)
                {
                    edgeId = bNodeId + "-" + aNodeId;
                    edge = this.getEdge(edgeId);
                }
                if(edge != null)
                {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }
    
    
    public void restore(ItemGraph<T> subGraph)
    {
        for(ItemEdge<T> edge : subGraph.getEachEdgeItem())
        {
            ((Highlightable)this.getEdgeItem(edge)).restore();
        }
    }
    
    public Iterable<ItemNode<T>> getEachNodeItem()
    {
        return (Iterable<ItemNode<T>>) this.getEachNode();
    }
    
    public Iterable<ItemEdge<T>> getEachEdgeItem()
    {
        return (Iterable<ItemEdge<T>>) this.getEachEdge();
    }
    
    public ItemNode<T> getNodeItem(T item)
    {
        return (ItemNode<T>)this.getNode(item.getItemId());
    }
    
    public ItemNode<T> getNodeItem(ItemNode<T> nodeItem)
    {
        return (ItemNode<T>)this.getNodeItem(nodeItem.getItem());
    }
    
    public ItemEdge<T> getEdgeItem(ItemEdge<T> edgeItem)
    {
        return (ItemEdge<T>)this.getEdge(edgeItem.getId());
    }
    
    private void updateNodeStyles() 
    {
        for (ItemNode<T> node : this.getEachNodeItem()) 
        {
            node.updateStyle();
        }
    }    
    
    public ItemNode<T> addNodeItem(T item)
    {
        if(item == null)
        {
            return null;
        }
        
        ItemNode<T> node = new ItemNode<>(this, item);
        if(this.getNode(item.getItemId()) == null)
        {
            this.addNode(item.getItemId());
        }
        return node;
    }

    public ItemEdge<T> addEdge(T source, T target)
    {
        ItemNode<T> sourceNode = this.getNodeItem(source); 
        if(sourceNode == null)
        {
            sourceNode = this.addNodeItem(source);
        }        
        ItemNode<T> targetNode = this.getNodeItem(target);
        if(targetNode == null)
        {
            targetNode = this.addNodeItem(target);
        }
        ItemEdge<T> edge = new ItemEdge<>(sourceNode, targetNode);
        if(this.getEdge(edge.getId()) == null && this.getEdge(new ItemEdge<>(targetNode, sourceNode).getId()) == null)
        {
            this.addEdge(edge.getId(), sourceNode, targetNode);
        }
        
        return edge;
    }
    
    
    public ItemNode<T> getSelectedNode()
    {
        return this.selectedNode;
    }
    
    public void setSelectedNode(String id)
    {
        if(this.selectedNode != null)
        {
            //((Highlightable)this.selectedNode).restore();
            this.selectedNode.unselect();
        }
        this.selectedNode = (ItemNode<T>)this.getNode(id);
        //((Highlightable)this.selectedNode).highlight(Highlightable.NODE_SELECTED_HIGHLIGHT);
        this.selectedNode.select();
    }

    
    @Override
    public void onSelectedItemAttribute(ItemAttributeEvent event) 
    {
        this.highlight(event.getAttKey(), event.getAttValue(), Highlightable.ORANGE_HIGHLIGHT);
    }

    @Override
    public void onUnselectedItemAttribute(ItemAttributeEvent event) 
    {
        this.restore(event.getAttKey(), event.getAttValue());
    }

    @Override
    public void onSelectedAttributeKey(ItemAttributeEvent event) 
    {
    }
    
    @Override
    public void graphCleared(String sourceId, long timeId)
    { 
        initGraph();
        displayCurrentGraph();                  
    }
    
    public void initGraph()
    {
        String graphInitStyle = String.format("url('%s')", Configuration.getValue("item.style"));
        addAttribute("ui.stylesheet", graphInitStyle);
        addAttribute("ui.antialias");
        addAttribute("ui.quality");
        setAutoCreate(true);
        setStrict(false);  
    }
    
    public void displayCurrentGraph()
    {
        setRecommendations(this.currentRecommendations);
        buildFromItems(this.currentAttKey);
        if (currentAttValue != null)
        {
            highlight(currentAttKey, currentAttValue, Highlightable.ORANGE_HIGHLIGHT);
        }
        viewer.enableAutoLayout();
    }
    
    public void updateGraph(Map<String, T> recommendations, String attKey)
    {
        viewer.disableAutoLayout();
        
        // Unhighlight stuff
        //unhighlight(currentAttKey, currentAttValue);
        
        this.currentRecommendations = recommendations;
        this.currentAttKey = attKey;
        this.currentAttValue = null;
        clear();
    }
    
    public void updateGraph(Map<String, T> recommendations, String attKey, String attValue)
    {
        viewer.disableAutoLayout();
        
        // Unhighlight stuff
        //unhighlight(currentAttKey, currentAttValue);
        
        this.currentRecommendations = recommendations;
        this.currentAttKey = attKey;
        this.currentAttValue = attValue;
        clear();
    }

}
