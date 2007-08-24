/**
 * @author vlerkenb
 * 4-feb-2003
 * SaveDatabaseAction.java
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

import be.vanvlerken.bert.logmonitor.gui.DatabaseFileChooser;
import be.vanvlerken.bert.logmonitor.logging.IDatabasePersister;

/**
 * Triggers a save action on the databasePersister
 */
public class SaveDatabaseAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(SaveDatabaseAction.class);
    
    private JFrame mainWindow;
    private IDatabasePersister  dbPersister;
    
    public SaveDatabaseAction(JFrame mainWindow, IDatabasePersister dbPersister)
    {
        super("Save Database");
        this.mainWindow = mainWindow;
        this.dbPersister = dbPersister;
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        logger.info("Saving the logs...");
        
        logger.info("Testing if a save file is already selected...");
        
        try
        {
            /* Trying SaveDatabase... On failure jump to catch */
            dbPersister.save();
        }
        catch (FileNotFoundException fe)
        {
            /* Probably there was no correct save file, should display save dialog */
            DatabaseFileChooser fileSelector = DatabaseFileChooser.getInstance();
            int returnVal = fileSelector.showSaveDialog(mainWindow);
            if(returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File selectedFile = fileSelector.getSelectedFile();
            
                logger.info("File "+ selectedFile.getName() + " was selected");
                
                /* Try saveAs on the database */
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
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(mainWindow, ioe.getMessage());
        }
    }

}
