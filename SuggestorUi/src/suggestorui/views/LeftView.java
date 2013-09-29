/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.WebAccordion;
import com.alee.extended.panel.WebAccordionStyle;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.extended.panel.WebComponentPanel;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import suggestorui.Configuration;
import suggestorui.ServiceWorker;
import webclient.AttributeCollection;
import webclient.User;
import webclient.messaging.ServiceEvent;
import webclient.messaging.ServiceEventListener;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class LeftView extends WebComponentPanel implements ComponentListener
{
    ItemAttributePanel itemAttributePane;
    public LeftView()
    {
        itemAttributePane = new ItemAttributePanel();
        this.addElement(new UserView());
        this.addElement(itemAttributePane);
        this.addComponentListener(this);
        this.setUndecorated(false);
        this.setDrawSides(false, false, false, false);
    }
    
    public ItemAttributePanel.WebAttributePanel getSelectedAttributePane()
    {
        return this.itemAttributePane.getSelectedPanel();
    }
    
    public void addItemAttributeEventListener(ItemAttributeEventListener listener)
    {
        itemAttributePane.addItemAttributeEventListener(listener);
    }
    
    public void removeItemAttributeEventListener(ItemAttributeEventListener listener)
    {
        itemAttributePane.removeItemAttributeEventListener(listener);
    }

    @Override
    public void componentResized(ComponentEvent e) 
    {
        setSize(new Dimension(190, getHeight())); 
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
    
    public class UserView extends WebPanel
    {
        public UserView()
        {
            User currentUser = User.getCurrent();
            WebImage profileImage = new WebImage(currentUser.getIconPath());
            TooltipManager.setTooltip(profileImage, "The Current Selected User", TooltipWay.up);
            
            
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(Box.createVerticalGlue());
            this.add(profileImage);
            this.add(new WebLabelPanel("ID: ", currentUser.getItemId()));
            this.add(new WebLabelPanel("Age: ", "" +currentUser.getAge()));
            this.add(new WebLabelPanel("Gender: ", currentUser.getGender()));
            this.add(new WebLabelPanel("Occupation: ", currentUser.getOccupation()));
            this.add(new WebLabelPanel("Zip Code: ", currentUser.getZipcode()));
        }
    }
    
    public class ItemAttributePanel extends WebPanel implements ServiceEventListener, MouseListener
    {
        protected List<ItemAttributeEventListener> listeners = new ArrayList<>();
        private WebAttributePanel selected;
        private int selectedIndex;
        private boolean hover = false;
        private WebAccordion attKeyPane;
        private WebComboBox attKeyList;
        
        public ItemAttributePanel()
        {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(Box.createVerticalGlue());
            
            WebLabel label = new WebLabel();
            label.setText(String.format("<html><div style='font-size: 1.2em; font-weight: bold; font-color: rgb(250,250,250);'>%s</div></html>", Configuration.getValue("itemAttributeLabel")));
            WebPanel titlePanel = new WebPanel();
            
            titlePanel.add(label);
            //titlePanel.setUndecorated ( false );
            titlePanel.setMargin ( 6 );
            //titlePanel.setDrawSides(true, true, true, true);
            titlePanel.setDrawBackground(false);
            titlePanel.setBackground(new Color(172,167,147));
            titlePanel.setForeground(new Color(250,250,250));
            
            this.add(titlePanel);
            this.setDrawSides(false, false, false, false);
            ServiceWorker.getInstance().addServiceEventListener(this);
            updateUi();
        }
        
        public WebAttributePanel getSelectedPanel()
        {
            return this.selected;
        }
        
        
        private void updateUi()
        {
            this.removeCollapsible();
            if(!AttributeCollection.isEmpty())
            {
                attKeyPane = new WebAccordion(WebAccordionStyle.accordionStyle);
                attKeyPane.setMargin(3);
                attKeyPane.addMouseListener(this);
                attKeyPane.setMultiplySelectionAllowed(false);
                AttributeCollection.printToScreen();
                int firstIndex = AttributeCollection.getAttributeKeys().indexOf(Configuration.getValue("defaultAttributeKey"));
                for(String attKey : AttributeCollection.getAttributeKeys())
                {
                    String keyName = "".equals(Configuration.getValue(attKey)) ? attKey : Configuration.getValue(attKey);
                    int totalcount = 0;
                    WebAttributeKeyPanel attValuePanes = new WebAttributeKeyPanel(attKey, "", 4);
                    attValuePanes.addMouseListener(this);
                    attValuePanes.setLayout(new BoxLayout(attValuePanes, BoxLayout.Y_AXIS));
                    attValuePanes.add(Box.createVerticalGlue());
                    for(String attValue : AttributeCollection.getAttributeValues(attKey))
                    {
                        int count = AttributeCollection.getCluster(attKey, attValue).size();
                        totalcount++;
                        String valueLabel = String.format("%s (%d)", attValue, count);
                        WebAttributePanel panel = new WebAttributePanel(attKey, attValue);
                        panel.setCount(count);
                        panel.setDrawTop(true);
                        panel.setDrawBottom(true);
                        panel.add(new WebLabel(valueLabel));
                        panel.addMouseListener(this);
                        attValuePanes.add(panel);
                    }
                    String title = String.format("%s (%d)", keyName, totalcount);
                    attKeyPane.addPane(title, attValuePanes);
                }
                
                if(firstIndex > 0)
                {
                    Component a = attKeyPane.getContentAt(0);
                    String a_title = attKeyPane.getTitleAt(0);
                    Component b = attKeyPane.getContentAt(firstIndex);
                    String b_title = attKeyPane.getTitleAt(firstIndex);
                    
                    attKeyPane.setContentAt(0, b);
                    attKeyPane.setTitleAt(0, b_title);
                    attKeyPane.setContentAt(firstIndex, a);
                    attKeyPane.setTitleAt(firstIndex, a_title);
                }
                
                this.add(attKeyPane);
                this.selectedIndex = attKeyPane.getFirstSelectedIndex();
            }
            this.repaint();
            this.updateUI();
        }
        
        public String getSeletectedAttributeKey()
        {
            int index = this.attKeyPane.getFirstSelectedIndex();
            String attKey = ((WebAttributeKeyPanel)this.attKeyPane.getContentAt(index)).getAttKey();
            return attKey;
        }
        
        public void addItemAttributeEventListener(ItemAttributeEventListener listener)
        {
            listeners.add(listener);
        }
        
        public void removeItemAttributeEventListener(ItemAttributeEventListener listener)
        {
            listeners.remove(listener);
        }
        
        protected synchronized void fireOnSelectedItemAttributeEvents(ItemAttributeEvent event)
        {
            for(ItemAttributeEventListener listener : listeners)
            {
                listener.onSelectedItemAttribute(event);
            }
        }
        
        protected synchronized void fireOnUnselectedItemAttributeEvents(ItemAttributeEvent event)
        {
            for(ItemAttributeEventListener listener : listeners)
            {
                listener.onUnselectedItemAttribute(event);
            }
        }

        protected synchronized void fireOnUnselectedAttributeKeyEvents(ItemAttributeEvent event)
        {
            for(ItemAttributeEventListener listener : listeners)
            {
                listener.onSelectedAttributeKey(event);
            }
        }
        
        private void removeCollapsible()
        {
            for(Component c : this.getComponents())
            {
                if(c instanceof WebAccordion)
                {
                   this.remove(c); 
                }
            }
        }

        @Override
        public void onPreCommunication(ServiceEvent event) 
        {
        }

        @Override
        public void onPostCommunication(ServiceEvent event) 
        {
            this.updateUi();
        }

        @Override
        public void mouseClicked(MouseEvent e) 
        {
            if(this.selected != null)
            {
                ItemAttributeEvent event = new ItemAttributeEvent(selected.getAttKey(), selected.getAttValue());
                this.fireOnUnselectedItemAttributeEvents(event);
                this.selected.setBackground(Color.white);
            }
            if(e.getSource() instanceof WebAttributePanel)
            {
                WebAttributePanel panel = (WebAttributePanel)e.getSource();
                panel.setBackground(new Color(153,153, 153));                
                ItemAttributeEvent event = new ItemAttributeEvent(panel.getAttKey(), panel.getAttValue());
                event.setCount(panel.getCount());
                if(selected != null)
                {
                    event.setRebuildRequired(!selected.getAttKey().equals(panel.getAttKey()));
                }
                else
                {
                    event.setRebuildRequired(true);
                }
                this.fireOnSelectedItemAttributeEvents(event);
                this.selected = panel;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) 
        {
//            if(e.getSource() instanceof WebAccordion)
//            {
//                WebAccordion accordion = (WebAccordion)e.getSource();
//                if(this.selectedIndex != accordion.getFirstSelectedIndex())
//                {
//                    this.selectedIndex = accordion.getFirstSelectedIndex();
//                    WebAttributePanel panel = (WebAttributePanel)accordion.getContentAt(selectedIndex);
//                    
//                    ItemAttributeEvent event = new ItemAttributeEvent(panel.getAttKey(), "");
//                    this.fireOnUnselectedAttributeKeyEvents(event);
//                }
//            }
        }

        @Override
        public void mouseReleased(MouseEvent e) 
        {
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
            hover = true;
//            if(e.getSource() instanceof WebAttributeKeyPanel)
//            {
//                WebAccordion accordion = (WebAccordion)e.getSource();
//                if(this.selectedIndex != accordion.getFirstSelectedIndex())
//                {
//                    this.selectedIndex = accordion.getFirstSelectedIndex();
//                    WebAttributePanel panel = (WebAttributePanel)accordion.getContentAt(selectedIndex);
//                    
//                    ItemAttributeEvent event = new ItemAttributeEvent(panel.getAttKey(), "");
//                    this.fireOnUnselectedAttributeKeyEvents(event);
//                }
//            }
        }

        @Override
        public void mouseExited(MouseEvent e) 
        {
            hover = false;
        }
        
        public class WebAttributeKeyPanel extends JPanel
        {            
            private String attKey;
            private String attValue;
            
            public WebAttributeKeyPanel(String attKey, String attValue, int margin)
            {
                this.attKey = attKey;
                this.attValue = attValue;
                //this.setMargin(margin);
            }
            
            public WebAttributeKeyPanel(String attKey, String attValue)
            {
                this(attKey, attValue, 3);
            }

            /**
             * @return the attKey
             */
            public String getAttKey() {
                return attKey;
            }

            /**
             */
            public String getAttValue() {
                return attValue;
            }
        }
        
        public class WebAttributePanel extends WebPanel
        {
            private String attKey;
            private String attValue;
            private int count = 0;
            
            public WebAttributePanel(String attKey, String attValue, int margin)
            {
                this.attKey = attKey;
                this.attValue = attValue;
                this.setMargin(margin);
            }
            
            public WebAttributePanel(String attKey, String attValue)
            {
                this(attKey, attValue, 3);
            }

            /**
             * @return the attKey
             */
            public String getAttKey() {
                return attKey;
            }

            /**
             */
            public String getAttValue() {
                return attValue;
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
    }
    
}
