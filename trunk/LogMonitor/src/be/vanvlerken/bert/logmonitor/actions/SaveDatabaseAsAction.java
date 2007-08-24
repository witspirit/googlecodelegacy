/**
 * @author vlerkenb
 * 4-feb-2003
 * SaveDatabaseAsAction.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.gui.DatabaseFileChooser;
import be.vanvlerken.bert.logmonitor.logging.IDatabasePersister;

/**
 * Triggers the saveAs operation no the database persister
 */
public class SaveDatabaseAsAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(SaveDatabaseAsAction.class);
    
    private JFrame mainWindow;
    private IDatabasePersister  dbPersister;
    
    public SaveDatabaseAsAction(JFrame mainWindow, IDatabasePersister dbPersister)
    {
        super("Save Database As...");
        this.mainWindow = mainWindow;
        this.dbPersister = dbPersister;
    }    

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
        logger.info("Display DatabaseFileChooser for SaveDatabaseAsAction...");
        
        DatabaseFileChooser fileSelector = DatabaseFileChooser.getInstance();
        int returnVal = fileSelector.showSaveDialog(mainWindow);
        if(returnVal == DatabaseFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileSelector.getSelectedFile();
          
            logger.info("File "+ selectedFile.getName() + " was selected");
                
            try
            {
                dbPersister.saveAs(selectedFile);
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(mainWindow, ioe.getMessage());
            }
        }
	}

}
