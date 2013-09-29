/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public interface Highlightable 
{
    
    public String DEFAULT_HIGHLIGHT = "default_highlight";
    public String RED_HIGHLIGHT = "red";
    public String ORANGE_HIGHLIGHT = "orange";
    public String YELLOW_HIGHLIGHT = "yellow";
    public String GREEN_HIGHLIGHT = "green";
    public String BLUE_HIGHLIGHT = "blue";
    public String VIOLET_HIGHLIGHT = "violet";
    public String PINK_HIGHLIGHT = "pink";
    public String NODE_SELECTED_HIGHLIGHT = "nodeselected";
    public Map<String, String> previousClass = new HashMap<>();
    
    
    public void highlight(String highlightClass);
    public void restore();
}
