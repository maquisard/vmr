/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.io.File;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import suggestorui.Displayable;
import suggestorui.Styleable;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */

@Root(name="Item")
public class MovieItem extends Item implements Displayable, Styleable
{
    @Element(name="Id")
    private String itemid;
    @Element(name="Title")
    private String title;
    @Element(name="Release_Date")
    private String releaseDate;
    @Element(name="Description")
    private String description;
    
    protected MovieItem()
    {
        
    }
    
    public static MovieItem TestMovie()
    {
        MovieItem item = new MovieItem();
        item.itemid = "";
        item.title = "";
        item.releaseDate = "";
        item.description = "";
        return item;
    }
    
    @ElementList(name="Attributes")
    private List<MovieAttribute> attributes;

    @Override
    public String getItemId() 
    {
        return this.itemid;
    }

    public String getYear()
    {
        String[] pieces = this.releaseDate.split("-");
        if(pieces.length == 0)
        {
            return "";
        }
        return pieces[pieces.length - 1];
    }
    
    
    @Override
    public int getQuantity() 
    {
        return 0;
    }

    @Override
    public String getTitle() 
    {
        return this.title;
    }

    @Override
    public String getDescription() 
    {
        return this.description;
    }

    @Override
    public String getIconPath() 
    {
        String template = "../Databases/ml-100k/PosterImages/%s";
        String path = String.format(template, this.getItemId() + ".jpg");
        File file = new File(path);
        if(file.exists())
        {
            return path;
        }
        else
        {
            return String.format(template, "default.png");
        }
    }
        
    @Override
    public String getStyle() 
    {
        return String.format("fill-image: url('%s');", this.getIconPath());
    }

    @Override
    public String getUiLabel() 
    {
        return "";
    }

    @Override
    public boolean onCreated() 
    {
        for(MovieAttribute attribute : attributes)
        {
            AttributeCollection.add(attribute.getKey(), attribute.getValue(), this);
        }
        return true;
    }
    
    public void printGenres()
    {
        for(MovieAttribute attribute : attributes)
        {
            if(attribute.getKey().toLowerCase().equals("genre"))
            {
                System.out.print(attribute.getValue() + " ");
            }
        }
        System.out.println();
    }
}
