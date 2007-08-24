/**
 * @author wItspirit
 * 11-nov-2005
 * MasterSlaveOverwriteStrategy.java
 */

package be.vanvlerken.bert.flickrstore.store;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Combines the strategy from the photo file with that of the XML file
 * Thus if the photo is overwritten, so will be the XML file.
 */
public class CombinedOverwriteStrategy implements OverwriteStrategy
{
    private OverwriteStrategy photoOverwriteStrategy;
    private Map<String, Boolean> cachedDecisions; 
    
    public CombinedOverwriteStrategy(OverwriteStrategy photoOverwriteStrategy)
    {
        this.photoOverwriteStrategy = photoOverwriteStrategy;
        
        cachedDecisions = new HashMap<String, Boolean>();
    }
    
    public boolean overwrite(File file)
    {
        boolean decision = true;
        if ( file.getName().endsWith("xml") )
        {
            decision = cachedDecisions.remove(getFileKey(file));
        }
        else
        {
            decision = photoOverwriteStrategy.overwrite(file);
            cachedDecisions.put(getFileKey(file), decision);
        }
        return decision;
    }

    private String getFileKey(File file)
    {
        String fileName = file.getName();        
        String fileKey = fileName.substring(0, fileName.length()-4);        
        return fileKey;
    }

}
