/**
 * @author vlerkenb
 * 6-feb-2003
 * ConfigFileFilter.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filters a file listing for ConfigFiles
 */
public class ConfigFileFilter extends FileFilter
{

    /**
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f)
    {
        if (f.isDirectory()) { return true; }

        String extension = getExtension(f);
        if (extension != null && extension.equals("xml")) { return true; }

        return false;
    }

    /**
     * Method getExtension.
     * 
     * @param f
     * @return String
     */
    private String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();

        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription()
    {
        return "LogMonitor configuration files";
    }

}