/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import suggestorui.Configuration;
import webclient.AttributeCollection;
import webclient.Item;
import webclient.User;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class SuggestorClient extends DefaultHttpClient 
{
    private String wsUrl = Configuration.getValue("webserviceurl");
    private static SuggestorClient current = null;
    
    private SuggestorClient()
    {
        super();
    }
    
    public static SuggestorClient getCurent()
    {
        if(current == null)
        {
            current = new SuggestorClient();
        }
        return current;
    }
    
    public SuggestorResponse sendMessage(SuggestorMessage message)
    {
        if(message == null)
        {
            return null;
        }
        String fullUrl = wsUrl + message.getOperation();
        HttpResponse response = null;
        try 
        {
            HttpPost post = new HttpPost(fullUrl);
            post.setEntity(new UrlEncodedFormEntity(message.getParameters()));
            response = this.execute(post);
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(SuggestorClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(message.getResponseType() == SuggestorMessage.VALUE_RESPONSE)
        {
            return new SuggestorValueResponse(response);
        }
        else if(message.getResponseType() == SuggestorMessage.ENTITY_LIST_RESPONSE)
        {
            return new SuggestorItemListResponse(response);
        }
        else if(message.getResponseType() == SuggestorMessage.ENTITY_RESPONSE)
        {
            return new SuggestorUserResponse(response);
        }
        else
        {
            return new SuggestorResponse(response);
        }
    }
    
    public class SuggestorUserResponse extends SuggestorResponse
    {
        private User user;
        
        public SuggestorUserResponse(HttpResponse response)
        {
            super(response);
        }

        @Override
        protected void extractContent(String content) 
        {
            super.extractContent(content);
            user = null;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            try 
            {            
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                InputSource source = new InputSource();
                source.setCharacterStream(new StringReader(content));
                Document doc = builder.parse(source);
                
                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();
                String expression = "//Item";
                XPathExpression xPathExpression = xPath.compile(expression);
                NodeList result = (NodeList)xPathExpression.evaluate(doc, XPathConstants.NODESET);
                
                if(result.getLength() > 0)
                {
                    user = Item.createFromXml(User.class, this.toInnerXml(result.item(0)));
                }

            } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
                Logger.getLogger(SuggestorClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        
        public User getUser()
        {
            return user;
        }
        
    }
    public class SuggestorItemListResponse<T extends Item> extends SuggestorResponse
    {
        private Map<String, T> items;
        
        public SuggestorItemListResponse(HttpResponse response)
        {
            super(response);
        }

        @Override
        protected void extractContent(String content) 
        {
            super.extractContent(content);
            if(items == null)
            {
                items = new HashMap<>();
            }
            items.clear();
            
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            try 
            {            
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                InputSource source = new InputSource();
                source.setCharacterStream(new StringReader(content));
                Document doc = builder.parse(source);
                
                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();
                String expression = "//Item";
                XPathExpression xPathExpression = xPath.compile(expression);
                NodeList result = (NodeList)xPathExpression.evaluate(doc, XPathConstants.NODESET);
                
                Class<T> classname = (Class<T>) Class.forName("webclient." + this.getItemType());

                for(int i = 0; i < result.getLength(); i++)
                {
                    T item = Item.createFromXml(classname, this.toInnerXml(result.item(i)));
                    this.items.put(item.getItemId(), item);
                }
                
                System.out.println("Real Size of things: " + result.getLength());

            } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException | ClassNotFoundException ex) {
                Logger.getLogger(SuggestorClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                
        public Map<String, T> getItems()
        {
            return items;
        }
        
        public String getBasketType()
        {
            return Configuration.getValue("baskettype");
        }
        
        public String getItemType()
        {
            return Configuration.getValue("itemtype");
        }

        public String getItemTypeListName()
        {
            return Configuration.getValue("itemtypelist");
        }
    }
    
    public class SuggestorValueResponse extends SuggestorResponse
    {
        private String value;
        protected SuggestorValueResponse(HttpResponse response)
        {
            super(response);
        }

        @Override
        protected void extractContent(String content) 
        {
            super.extractContent(content);
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            try 
            {            
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                InputSource source = new InputSource();
                source.setCharacterStream(new StringReader(content));
                Document doc = builder.parse(source);
                
                if(doc.getChildNodes().getLength() > 0)
                {
                    value = doc.getChildNodes().item(0).getTextContent();
                }
                else
                {
                    value = content;
                }
                
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(SuggestorClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public String getValue()
        {
            return value;
        }
        
    }
    
    public class SuggestorResponse
    {
        private boolean failed = false;
        private String errorMessage = "";
        
        protected SuggestorResponse(HttpResponse response)
        {
            BufferedReader rd;
            try 
            {
                rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String content = "";
                String line = "";
                while ((line = rd.readLine()) != null) 
                {
                    content += line;
                }
                this.processContent(content);
                if(!failed)
                {
                    this.extractContent(content);
                }
            } 
            catch (IOException | IllegalStateException ex) 
            {
                Logger.getLogger(SuggestorResponse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        protected void extractContent(String content)
        {
            
        }
        
        private void processContent(String content) 
        {
            if(content.toLowerCase().contains("exception"))
            {
                this.failed = true;
                if(content.toLowerCase().contains("html"))
                {
                    this.errorMessage = this.retrieveErrorMessage(content, "title");
                }
                else if (content.toLowerCase().contains("soap"))
                {
                    this.errorMessage = this.retrieveErrorMessage(content, "soap:Reason").replace("</soap:Text", "").replace("<soap:Text xml:lang=\"en\">", "");
                }
                else 
                {
                    this.errorMessage = "Unknown Error Type.\n\n" + content;
                }
            }
        }
        
        private String retrieveErrorMessage(String content, String element)
        {
            String beginElement = "<" + element + ">";
            String endElement = "</" + element + ">";
            int beginIndex = content.indexOf(beginElement) + beginElement.length();
            int endIndex = content.indexOf(endElement) - 1;
            
            return content.substring(beginIndex, endIndex);
        }
        
        public String toInnerXml(Node node)
        {
            String rootName = node.getNodeName();
            DOMImplementationLS lsImpl = (DOMImplementationLS)node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
            LSSerializer lsSerializer = lsImpl.createLSSerializer();
            lsSerializer.getDomConfig().setParameter("xml-declaration", false);
            NodeList childNodes = node.getChildNodes();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("<%s>", rootName));
            for (int i = 0; i < childNodes.getLength(); i++) {
               sb.append(lsSerializer.writeToString(childNodes.item(i)));
            }
            sb.append(String.format("</%s>", rootName));
            return sb.toString();         
        }
        
        
        public boolean hasError()
        {
            return failed;
        }
        
        public String getErrorMessage()
        {
            return errorMessage;
        }
    }
}
