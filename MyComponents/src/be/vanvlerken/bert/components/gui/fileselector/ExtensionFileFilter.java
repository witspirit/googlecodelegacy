/**
 * @author wItspirit
 * 23-apr-2003
 * ExtensionFileFilter.java
 */
package be.vanvlerken.bert.components.gui.fileselector;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * FileFilter that selects files with a certain extension
 */
public class ExtensionFileFilter extends FileFilter
{
    private String description;
    private String extension;
    
    public ExtensionFileFilter(String description, String extension)
    {
        super();
        this.description = description;
        this.extension = extension;
    }

    /**
     * @see javax.swing.filechooser.FileFilter#accept(File)
     */
    public boolean accept(File file)
    {
        if ( file.isDirectory() )
        {
            return true;
        }
        
        String fileExtension = getExtension(file);
        if (fileExtension != null) 
        {
            if (fileExtension.equals(extension) )
            {
                return true;
            } 
            else 
            {
                return false;
            }
        }

        return false;
    }
    
    /**
     * Method getExtension.
     * @param file
     * @return String
     */
    private String getExtension(File file)
    {
        String ext = null;
        String inputString = file.getName();

        int i = inputString.lastIndexOf('.');
        if (i > 0 &&  i < inputString.length() - 1) 
        {
           ext = inputString.substring(i+1).toLowerCase();
        }
        return ext;
        
        /* Implementation using regex caused a problem in Eclipse with escape sequences */
    }
        

    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

}
