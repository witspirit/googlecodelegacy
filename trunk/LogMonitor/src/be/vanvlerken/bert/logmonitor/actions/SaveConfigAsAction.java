/**
 * @author vlerkenb
 * 4-feb-2003
 * SaveConfigAsAction.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.configuration.Configuration;
import be.vanvlerken.bert.logmonitor.gui.ConfigFileChooser;

/**
 * description
 */
public class SaveConfigAsAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(SaveConfigAsAction.class);
    
    private JFrame mainWindow;
        
    public SaveConfigAsAction(JFrame mainWindow)
    {
        super("Save config as...");
        this.mainWindow = mainWindow;
    }
    
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
        logger.info("Display ConfigFileChooser for SaveConfigAsAction...");
        
        ConfigFileChooser fileSelector = ConfigFileChooser.getInstance();
        int returnVal = fileSelector.showSaveDialog(mainWindow);
        if(returnVal == ConfigFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileSelector.getSelectedFile();
          
            logger.info("File "+ selectedFile.getName() + " was selected");
                
            try
            {
                Configuration.getInstance().saveConfigurationAs(selectedFile);
            }
            catch (FileNotFoundException fnfe)
            {
                logger.error("Strange...Got a FileNotFoundException from my saveConfigurationAs...");
                JOptionPane.showMessageDialog(mainWindow, fnfe.getMessage());
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
	}

}
