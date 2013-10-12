/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import com.alee.laf.WebLookAndFeel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.graphstream.ui.swingViewer.ViewerListener;
import suggestorui.views.ItemVizView;
import webclient.Item;
import webclient.MovieItem;
import webclient.User;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class SuggestorUi// implements ViewerListener
{
    /**
     * @param args the command line arguments
     */
    
    protected boolean loop = true;
    protected boolean highlighted = false;
    protected ItemGraph<MovieItem> graph = null;
    
    private String[] gender = { "M", "F" };
    private String[] colors = 
    {
        Highlightable.BLUE_HIGHLIGHT,
        Highlightable.RED_HIGHLIGHT,
        Highlightable.YELLOW_HIGHLIGHT,
        Highlightable.GREEN_HIGHLIGHT,
        Highlightable.ORANGE_HIGHLIGHT,
        Highlightable.VIOLET_HIGHLIGHT,
        Highlightable.PINK_HIGHLIGHT
    };
    private int aIter = 0;
    private int cIter = 0;
    
    public static void main(String[] args) 
    {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        
        SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    new SuggestorUi().createAndShowGUI();
                }
            });
    }
    
    protected void createAndShowGUI()
    {
        try {
            UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName());
            WebLookAndFeel.initializeManagers();                        
            ItemVizView view = new ItemVizView();
            view.initializeModel();
            view.initUi();
            view.pack();
            view.setVisible(true);            
            startPumping(view);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SuggestorUi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void startPumping(final ItemVizView view)
    {        
        Thread queryThread = new Thread() 
        {
            public void run() 
            {
              view.startPumping();
            }
        };
        queryThread.start();
        
    }
}
