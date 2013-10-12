/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import org.graphstream.graph.implementations.SingleNode;
import webclient.Item;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemNode<T extends Item> extends SingleNode implements Highlightable
{
    private T item;
    private boolean selected = false;
    private boolean highlighted = false;
    
    public ItemNode(ItemGraph<T> graph, T item)
    {
        super(graph, item.getItemId());        
        this.item = item;
    }

    public void updateStyle()
    {
        Styleable stylable = (Styleable)this.getItem();
        this.addAttribute("ui.label", stylable.getUiLabel());
        this.addAttribute("ui.style", stylable.getStyle());
    }
    
    /**
     * @return the item
     */
    public T getItem() 
    {
        return item;
    }

    @Override
    public void highlight(String highlightClass) 
    {
        this.highlighted = true;
        this.addAttribute("ui.class", highlightClass);
    }

    @Override
    public void restore() 
    {
        this.removeAttribute("ui.class");
        this.addAttribute("ui.class", Configuration.getValue("nodeBaseClass"));
        
        this.updateStyle();
        this.highlighted = false;
    }
    
    public void select()
    {
        this.selected = true;
        this.addAttribute("ui.class", Highlightable.NODE_SELECTED_HIGHLIGHT);
    }
    
    public void unselect()
    {
        this.selected = false;
        if(highlighted) 
        {
            this.addAttribute("ui.class", Highlightable.ORANGE_HIGHLIGHT);
        }
        else
        {
            this.addAttribute("ui.class", Configuration.getValue("nodeBaseClass"));
        }
    }
    

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the highlighted
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * @param highlighted the highlighted to set
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
