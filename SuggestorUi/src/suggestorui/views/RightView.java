/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import com.alee.extended.image.WebImage;
import com.alee.extended.panel.WebComponentPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextPane;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.text.html.HTMLEditorKit;
import suggestorui.views.WebLabelPanel;
import webclient.MovieItem;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class RightView extends WebComponentPanel implements ComponentListener
{
    private MovieView mv;
    public RightView()
    {
        mv = new MovieView();
        this.addElement(mv);
        this.addComponentListener(this);
        this.setUndecorated(false);
        this.setDrawSides(false, false, false, false);
        this.setMargin(1);
	this.setPreferredSize(new Dimension(240, 0));        
    }

    public MovieView getMovieView()
    {
        return mv;
    }
    
    @Override
    public void componentResized(ComponentEvent e) 
    {
        setSize(new Dimension(300, getHeight())); 
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    public class MovieView extends WebPanel
    {
        WebImage movieImage;
        WebLabelPanel id;
        WebLabelPanel title;
        WebLabelPanel year;
        WebTextPane description;

        public MovieView()
        {
            MovieItem movie = MovieItem.TestMovie();
            movieImage = new WebImage(movie.getIconPath());
            TooltipManager.setTooltip(movieImage, "The Current Selected Movie", TooltipWay.up);

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(Box.createVerticalGlue());
            this.add(movieImage);
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(Box.createVerticalGlue());
            this.add(movieImage);

            id = new WebLabelPanel("ID: ", movie.getItemId());
            title = new WebLabelPanel("Title: ", movie.getTitle());
            year = new WebLabelPanel("Year: ", movie.getYear());

            this.add(id);
            //this.add(title);
            this.add(year);
            description = new WebTextPane();
            HTMLEditorKit kit = new HTMLEditorKit();
            description.setEditorKit(kit);                
            description.setEditable(false);
            description.setContentType("text/html");            
            description.setEditable(false);
            description.setText(String.format("<html><div style=\"overflow: auto;\">%s</div></html>", movie.getDescription()));
            this.add(new WebPanel(description));
        }
        
        public void updateView(MovieItem movie)
        {
            if(movie == null)
            {
                movie = MovieItem.TestMovie();
            }
            
            if(movie != null)
            {
                try {
                    BufferedImage image = ImageIO.read(new File(movie.getIconPath()));
                    movieImage.setImage(image);
                    id.setValue(movie.getItemId());
                    title.setValue(movie.getTitle(), 300);
                    year.setValue(movie.getYear());
                    description.setText(String.format("<html><div style=\"overflow: auto;\">%s</div></html>", movie.getDescription()));
                } catch (IOException ex) {
                    Logger.getLogger(RightView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
}
