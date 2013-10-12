/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
@Root(name="Attribute")
public class MovieAttribute
{
    @Attribute(name="Key")
    private String key;
    @Attribute(name="Value")
    private String value;        

    protected MovieAttribute()
    {

    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
