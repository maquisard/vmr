/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import org.graphstream.graph.implementations.AbstractEdge;
import webclient.Item;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemEdge<T extends Item> extends AbstractEdge implements Highlightable
{
    public ItemEdge(ItemNode<T> source, ItemNode<T> target)
    {
        super(source.getId() + "-" + target.getId(), source, target, false);
    }
    
    public T getSourceItem()
    {
        return ((ItemNode<T>)super.getSourceNode()).getItem();
    }
    
    public T getTargetItem()
    {
        return ((ItemNode<T>)super.getTargetNode()).getItem();
    }

    @Override
    public void highlight(String highlightClass) 
    {
        //((Highlightable)this.getSourceNode()).highlight(highlightClass);
        //((Highlightable)this.getTargetNode()).highlight(highlightClass);
        this.addAttribute("ui.class", highlightClass);
    }

    @Override
    public void restore() 
    {
        ((Highlightable)this.getSourceNode()).restore();
        ((Highlightable)this.getTargetNode()).restore();
        this.removeAttribute("ui.class");
        this.addAttribute("ui.class", Configuration.getValue("edgeBaseClass"));
    }
}
