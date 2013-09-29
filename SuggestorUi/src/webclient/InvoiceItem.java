/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import suggestorui.Displayable;
import suggestorui.Styleable;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */

@Root(name="Item")
public class InvoiceItem extends Item implements Displayable, Styleable
{
    protected InvoiceItem() { }
    
    @Element(name="Id")
    protected String itemid;
    
    @Element(name="Description")
    protected String description;
    
    @Element(name="Quantity")
    protected int quantity;
    

    /**
     * @return the itemid
     */
    @Override
    public String getItemId() {
        return itemid;
    }

    /**
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @return the quantity
     */
    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String getTitle() 
    {
        return this.itemid;
    }

    @Override
    public String getIconPath() 
    {
        return "";
    }

    @Override
    public String getStyle() 
    {
        return "";
    }


    @Override
    public String getUiLabel() 
    {
        return this.itemid;
    }

}
