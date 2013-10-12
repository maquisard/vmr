/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import java.util.EventListener;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public interface ItemAttributeEventListener extends EventListener 
{
    public void onSelectedItemAttribute(ItemAttributeEvent event);
    public void onUnselectedItemAttribute(ItemAttributeEvent event);
    public void onSelectedAttributeKey(ItemAttributeEvent event);
}
