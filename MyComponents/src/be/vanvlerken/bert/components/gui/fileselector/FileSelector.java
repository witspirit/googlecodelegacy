/**
 * @author wItspirit
 * 23-apr-2003
 * FileSelector.java
 */
package be.vanvlerken.bert.components.gui.fileselector;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;

/**
 * Generic FileSelector
 * Offers a Singleton implementation, based on a String identifier.
 * Thus, it is possible to create multiple FileChoosers with this class by using
 * different String identifiers for them
 * Default an instance will open in the user dir.
 */
public class FileSelector extends JFileChooser
{
    private static Map  instanceMap = new HashMap();
    
    public static FileSelector getFileSelector(String identifier)
    {
        FileSelector instance;
        if ( instanceMap.containsKey(identifier) )
        {
            instance = (FileSelector) instanceMap.get(identifier);
        }
        else
        {
            instance = new FileSelector();
            instanceMap.put(identifier, instance);
        }
        return instance;
    }
    
    protected FileSelector()
    {
        super(System.getProperty("user.dir"));
    }
}
