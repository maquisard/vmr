/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.graphstream.ui.graphicGraph.GraphicElementChangeListener;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;
import suggestorui.Configuration;
import suggestorui.Highlightable;
import suggestorui.ItemGraph;
import suggestorui.ItemVizModel;
import webclient.AttributeCollection;
import webclient.Item;
import webclient.MovieItem;
import webclient.User;


/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemVizView extends JFrame implements ViewerListener, ComponentListener, ItemAttributeEventListener
{
    private ItemVizModel model;
    private boolean loop = true;
    private RightView rightView;
    private WebBreadcrumb breadcrumbs;
    private ViewerPipe pipe;
    private View graphView;
    private Date lastClickTime = new Date();
    private int nRecommendations = Integer.parseInt(Configuration.getValue("nRecommendedItems"));
    private Viewer viewer;
    private WebLabel info;
    private Map<String, String> movieStack = new HashMap<>();
    private WebButton clearBreadCrumbs;
    private LeftView leftView;
    private int nHighlighted = 0;
    JProgressBar progressBar;
    JPanel middlePanel;
    
    public ItemVizView()
    {
        super(Configuration.getValue("application.title"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void initializeModel()
    {
        Map<String, MovieItem> initialRecommendations = GetRecommendedMovies();
        ItemVizModel newModel = new ItemVizModel(initialRecommendations);
        this.model = newModel;
        //updateGraph(initialRecommendations);
    }
    
    public void initUi() 
    {
        this.rightView = new RightView();
        this.leftView = new LeftView();
        this.leftView.addItemAttributeEventListener(model.getGraph());
        this.leftView.addItemAttributeEventListener(this);
                
        this.getContentPane().setLayout(new BorderLayout());
        
        JProgressBar newProgressBar = new JProgressBar();
        progressBar = newProgressBar;
        progressBar.setIndeterminate(true);
        int centerX = (this.getWidth() / 2) - (progressBar.getWidth() / 2);
        int centerY = (this.getHeight() / 2) - (progressBar.getHeight()/ 2);
        progressBar.setBounds(centerX, centerY, 200, 100);
        this.add(progressBar);
        
        progressBar.setVisible(false);
        
        this.initMiddleView();                        
        
        this.getContentPane().add(leftView, BorderLayout.WEST);
        this.getContentPane().add(rightView, BorderLayout.EAST);
        
        //this.setLocationRelativeTo(null);
    }
    
    private void initMiddleView()
    {
        middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());                
        
        this.initGraphViz(middlePanel);
        
        breadcrumbs = new WebBreadcrumb ( true );
        this.addBreadCrumb("Initial Recommendations", "-1");
        middlePanel.add(breadcrumbs, BorderLayout.NORTH);
        
        WebPanel bottomPanel = new WebPanel();
        bottomPanel.setUndecorated(false);
        info = new WebLabel(String.format("<html><b>Total Recommended Movies: </b>%d &#08;          "
                                                 + "<b>Top Similar Users: </b>%s &#08; <b>Hightlighted: </b>%d &#08;</html>", 
                                                this.nRecommendations, Configuration.getValue("nFirstUsers"), this.nHighlighted));
        
        clearBreadCrumbs = new WebButton("Clear Bread Crumbs");
        clearBreadCrumbs.setEnabled(false);
        clearBreadCrumbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Component[] crumbs = breadcrumbs.getComponents();
                for(int i = 1; i < crumbs.length; i++)
                {
                    Component c = crumbs[i];
                    breadcrumbs.remove(c);
                }
                ((WebBreadcrumbToggleButton)crumbs[0]).setSelected(true);
                clearBreadCrumbs.setEnabled(false);
                Map<String, MovieItem> recommendations = GetRecommendedMovies();
                updateGraph(recommendations);
                
            }
        });
        
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(info, BorderLayout.WEST);
        bottomPanel.add(clearBreadCrumbs, BorderLayout.EAST);
        bottomPanel.setMargin(3);
        middlePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        this.getContentPane().add(middlePanel, BorderLayout.CENTER);
    }
    
    public void addBreadCrumb(String label, String movieId)
    {
        WebBreadcrumbToggleButton button = new WebBreadcrumbToggleButton(label);
        movieStack.put(label, movieId);
        breadcrumbs.add(button);
        if(clearBreadCrumbs != null)
        {
            clearBreadCrumbs.setEnabled(breadcrumbs.getComponentCount() > 1);
        }
        button.setSelected(true);
        for(Component c : breadcrumbs.getComponents())
        {
            if(c instanceof WebBreadcrumbToggleButton && !c.equals(button))
            {
                ((WebBreadcrumbToggleButton)c).setSelected(false);
            }
        }
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                for(Component c : breadcrumbs.getComponents())
                {
                    if(c instanceof WebBreadcrumbToggleButton && !c.equals(e.getSource()))
                    {
                        ((WebBreadcrumbToggleButton)c).setSelected(false);
                    }
                }
                Map<String, MovieItem> recommendations;
                if(e.getActionCommand().toLowerCase().equals("initial recommendations"))
                {
                    recommendations = GetRecommendedMovies();
                }
                else
                {
                    recommendations = GetXRecommendedMovies(movieStack.get(e.getActionCommand()));
                }
                updateGraph(recommendations);
            }
        });
    }
    
    private void updateBottom()
    {
        info.setText(String.format("<html><b>Total Recommended Movies: </b>%d &#08;          "
                                                 + "<b>Top Similar Users: </b>%s &#08; <b>Hightlighted: </b>%d</html> ", 
                                                this.nRecommendations, Configuration.getValue("nFirstUsers"), this.nHighlighted));
    }
    
    private void initGraphViz(JPanel middlePanel)
    {
        ItemGraph graph = model.getGraph();
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);        
        viewer.enableAutoLayout();
        viewer.enableXYZfeedback(true);
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
        this.model.SetView(viewer);

        pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        pipe.addSink(graph);
                
        graphView = viewer.addDefaultView(false);
        graphView.addComponentListener(this);
        
        middlePanel.add(graphView, BorderLayout.CENTER);
    }
    
    public void startPumping()
    {
        while(loop) 
        {            
            pipe.pump();
            sleep(100);
        }        
    }
    
    protected static void sleep(long ms) {
		try { Thread.sleep(ms); } catch(Exception e) {} 
	}

    @Override
    public void viewClosed(String string) 
    {
        loop = false;
    }

    @Override
    public void buttonPushed(String nodeId) 
    {                
        System.out.println("buttonPushed");
        this.model.selectItem(nodeId);
        MovieItem movie = (MovieItem) this.model.getGraph().getSelectedNode().getItem();
        this.rightView.getMovieView().updateView(movie);
        movie.printGenres();
        
        Date currentClickTime = new Date();
        long interval = currentClickTime.getTime() - this.lastClickTime.getTime();
        //System.out.println("interval: " + interval);
        if(interval <= 400)
        {
            //System.out.println("Awesome!!! - Detected Double Click");
            Map<String, MovieItem> recommendations;
            if(this.leftView.getSelectedAttributePane() == null || 
               this.leftView.getSelectedAttributePane().getAttKey() == null || this.leftView.getSelectedAttributePane().getAttKey().equals("") ||
               this.leftView.getSelectedAttributePane().getAttValue() == null || this.leftView.getSelectedAttributePane().getAttValue().equals(""))
            {
                recommendations = GetXRecommendedMovies(movie.getItemId());                
                this.updateGraph(recommendations);
            }
            else
            {
                String attKey = this.leftView.getSelectedAttributePane().getAttKey();
                String attValue = this.leftView.getSelectedAttributePane().getAttValue();
                recommendations = GetXRecommendedMovies(movie.getItemId(), attKey, attValue);
                this.updateGraph(recommendations, attKey);
                //System.out.println("Selected Key Pair: " + attKey + ", " + attValue);
            }
            this.addBreadCrumb(movie.getTitle(), movie.getItemId());
        }
        this.lastClickTime = currentClickTime;
    }        

    private void updateGraph(Map<String, MovieItem> recommendations)
    {
        this.updateGraph(recommendations, Configuration.getValue("defaultAttributeKey"));
    }
    
    private void updateGraph(Map<String, MovieItem> recommendations, String attKey)
    {
        this.model.updateGraph(recommendations, attKey);
        updateUiInfo(recommendations.size(), 0);
    }
    
    private void updateGraph(Map<String, MovieItem> recommendations, String attKey, String attValue)
    {
        this.model.updateGraph(recommendations, attKey, attValue);
        this.updateUiInfo(recommendations.size(), 0);
    }
    
    private void updateUiInfo(int nRecommendations, int nHighlighted)
    {
        this.nRecommendations = nRecommendations;
        this.nHighlighted = nHighlighted;
        this.updateBottom();
    }
    
    private void updateHighlightNumber(int nHighlighted)
    {
        this.nHighlighted = nHighlighted;
        this.updateBottom();
    }

    @Override
    public void buttonReleased(String nodeId) 
    {
        
    }

    @Override
    public void componentResized(ComponentEvent e) 
    {
        graphView.getCamera().resetView();
    }

    @Override
    public void componentMoved(ComponentEvent e) 
    {
    }

    @Override
    public void componentShown(ComponentEvent e) 
    {
    }

    @Override
    public void componentHidden(ComponentEvent e) 
    {
    }

    @Override
    public void onSelectedItemAttribute(ItemAttributeEvent event) 
    {
        if(event.isRebuildRequired())
        {
            String attKey = event.getAttKey();
            String attValue = event.getAttValue();
            this.updateGraph(AttributeCollection.getItems(attKey), attKey, attValue);
        }
        this.updateHighlightNumber(event.getCount());
    }

    @Override
    public void onUnselectedItemAttribute(ItemAttributeEvent event) 
    {
    }

    @Override
    public void onSelectedAttributeKey(ItemAttributeEvent event) 
    {
    }

    public Map<String, MovieItem> GetRecommendedMovies()
    {   
        //Map<String, MovieItem> recommendations;        
        showProgressBar();
        Map<String, MovieItem> recommendations = User.getCurrent().getRecommendations();
        hideProgressBar();


        return recommendations;
    }
    
    public Map<String, MovieItem> GetXRecommendedMovies(String movieId)
    {
        showProgressBar();
        Map<String, MovieItem> recommendations =  User.getCurrent().getXRecommendations(movieId);
        hideProgressBar();
        return recommendations;
    }
    
    public Map<String, MovieItem> GetXRecommendedMovies(String movieId, String attKey, String attValue)
    {
        showProgressBar();
        Map<String, MovieItem> recommendations =  User.getCurrent().getXRecommendations(movieId, attKey, attValue);
        hideProgressBar();
        return recommendations;
    }
    
    private void showProgressBar()
    {
        
        SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    int centerX = (getWidth() / 2) - (progressBar.getWidth() / 2);
                    int centerY = (getHeight() / 2) - (progressBar.getHeight()/ 2);
                    progressBar.setBounds(centerX, centerY, 200, 100);
                    progressBar.setVisible(true);    
                    setEnabled(false);
                    System.out.println("ProgressBar hidden");
                }
            });
    }
    
    private void hideProgressBar()
    {
        SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    progressBar.setVisible(false);
                    setEnabled(true);
                    System.out.println("ProgressBar visible");
                }
            });
    }
}
