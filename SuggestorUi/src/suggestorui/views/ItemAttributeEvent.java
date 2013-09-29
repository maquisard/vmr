/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import java.util.EventObject;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemAttributeEvent extends EventObject 
{
    private String attKey;
    private String attValue;
    private boolean rebuildRequired = false;
    private int count = 0;
    
    public ItemAttributeEvent(String attKey, String attValue)
    {
        super(attKey);
        this.attKey = attKey;
        this.attValue = attValue;
    }

    /**
     * @return the attKey
     */
    public String getAttKey() {
        return attKey;
    }

    /**
     * @param attKey the attKey to set
     */
    public void setAttKey(String attKey) {
        this.attKey = attKey;
    }

    /**
     * @return the attValue
     */
    public String getAttValue() {
        return attValue;
    }

    /**
     * @param attValue the attValue to set
     */
    public void setAttValue(String attValue) {
        this.attValue = attValue;
    }

    /**
     * @return the rebuildRequired
     */
    public boolean isRebuildRequired() {
        return rebuildRequired;
    }

    /**
     * @param rebuildRequired the rebuildRequired to set
     */
    public void setRebuildRequired(boolean rebuildRequired) {
        this.rebuildRequired = rebuildRequired;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    
}
