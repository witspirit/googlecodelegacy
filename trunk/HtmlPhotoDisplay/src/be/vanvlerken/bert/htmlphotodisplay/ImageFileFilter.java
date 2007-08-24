/**
 * @author wItspirit
 * May 1, 2004
 * ImageFileFilter.java
 */

package be.vanvlerken.bert.htmlphotodisplay;

import java.io.File;
import java.io.FileFilter;


/**
 * Filters out JPEGs and GIFs out of a folder
 */
public class ImageFileFilter implements FileFilter
{

    /**
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept(File file)
    {
        if ( file.isDirectory() )
        {
            return false;
        }
        
        String name = file.getName();
        name = name.toLowerCase();
        if ( name.endsWith(".gif") || name.endsWith(".jpg"))
        {
            return true;
        }
        
        return false;
    }

}
