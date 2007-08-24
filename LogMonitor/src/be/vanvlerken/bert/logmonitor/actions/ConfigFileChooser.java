/**
 * @author vlerkenb
 * 6-feb-2003
 * ConfigFileChooser.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import javax.swing.JFileChooser;

/**
 * Provides a window to select a file for loading or saving the LogMonitor configuration
 */
public class ConfigFileChooser extends JFileChooser
{
    private static ConfigFileChooser instance = null;
    
    protected ConfigFileChooser()
    {
        super(System.getProperty("user.dir"));
        
        ConfigFileFilter filter = new ConfigFileFilter();
        setFileFilter(filter);
    }
    
    public static ConfigFileChooser getInstance()
    {
        if ( instance == null )
        {
            instance = new ConfigFileChooser();
        }
        return instance;
    }
}
