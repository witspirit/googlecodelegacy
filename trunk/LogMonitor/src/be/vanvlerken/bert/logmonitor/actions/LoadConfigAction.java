/**
 * @author vlerkenb
 * 4-feb-2003
 * LoadConfigAction.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import be.vanvlerken.bert.logmonitor.configuration.Configuration;
import be.vanvlerken.bert.logmonitor.gui.ConfigFileChooser;

/**
 * description
 */
public class LoadConfigAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(LoadConfigAction.class);
    
    private JFrame mainWindow;
    
    public LoadConfigAction(JFrame mainWindow)
    {
        super("Load config...");
        this.mainWindow = mainWindow;
    }
    
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ee)
	{
        logger.info("Displaying ConfigFileChooser for LoadConfigAction...");
        
        ConfigFileChooser fileSelector = ConfigFileChooser.getInstance();
        int returnVal = fileSelector.showOpenDialog(mainWindow);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileSelector.getSelectedFile();
            
            logger.info("File "+ selectedFile.getName() + " was selected");
            
            try
            {
                Configuration.getInstance().readConfiguration(selectedFile);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainWindow, e.getMessage());
            }
            catch (SAXException e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainWindow, e.getMessage());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainWindow, e.getMessage());
            }
        }
	}

}
