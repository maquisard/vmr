/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class WebLabelPanel extends WebPanel
{
    WebLabel labelComponent;
    WebLabel valueComponent;
    public WebLabelPanel(String label, String value)
    {
        labelComponent = new WebLabel(String.format("<html><b>%s </b></html>", label));
        valueComponent = new WebLabel(value);
        this.setLayout(new BorderLayout());
        this.add(labelComponent, BorderLayout.WEST);
        this.add(valueComponent, BorderLayout.EAST);
        this.setDrawLeft(false);
        this.setDrawRight(false);
        this.setDrawTop(false);
        this.setMargin(3);
        this.setUndecorated(false);
        this.setDrawBackground(false);

    }
    
    public void setLabel(String label)
    {
        labelComponent.setText(String.format("<html><b>%s </b></html>", label));
    }
    
    public void setValue(String value)
    {
        valueComponent.setText(value);
    }

    public void setValue(String title, int maxWidth) 
    {
        valueComponent.setText(title);
        valueComponent.setMaximumSize(new Dimension(maxWidth, valueComponent.getHeight()));
    }
}

