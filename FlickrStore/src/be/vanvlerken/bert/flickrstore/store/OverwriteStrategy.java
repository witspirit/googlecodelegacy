/**
 * @author wItspirit
 * 11-nov-2005
 * OverwriteStrategy.java
 */

package be.vanvlerken.bert.flickrstore.store;

import java.io.File;


public interface OverwriteStrategy
{
    /**
     * Will be called when file already exists.
     * Return true if the file should be overwritten
     * Return false if the file should be skipped
     * @param file
     * @return
     *      true    Overwrite
     *      false   Skip
     */
    public boolean overwrite(File file);
}
