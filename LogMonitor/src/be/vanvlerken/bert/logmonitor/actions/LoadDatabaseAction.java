/**
 * @author vlerkenb
 * 4-feb-2003
 * LoadDatabaseAction.java
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
 * Performs the loading of a logging database from file
 */
public class LoadDatabaseAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(LoadDatabaseAction.class);
    
    private JFrame mainWindow;
    private IDatabasePersister  dbPersister;
    
    public LoadDatabaseAction(JFrame mainWindow, IDatabasePersister dbPersister)
    {
        super("Load Database...");
        this.mainWindow = mainWindow;
        this.dbPersister = dbPersister;
    }
	
    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
        logger.info("Displaying the DatabaseFileChooser for LoadDatabaseAction...");
        
        DatabaseFileChooser fileSelector = DatabaseFileChooser.getInstance();
        int returnVal = fileSelector.showOpenDialog(mainWindow);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileSelector.getSelectedFile();
            
            logger.info("File "+ selectedFile.getName() + " was selected");
            
            if ( JOptionPane.showConfirmDialog(mainWindow, "Do you want to clear the view first ?", "Clear view ?", JOptionPane.YES_NO_OPTION) 
                  == JOptionPane.YES_OPTION )
            {
                (new ClearDatabaseAction()).actionPerformed(null);
            }
            
            try
            {
                dbPersister.loadDb(selectedFile);
            }
            catch (FileNotFoundException fnfe)
            {
                fnfe.printStackTrace();
                JOptionPane.showMessageDialog(mainWindow, fnfe.getMessage());
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(mainWindow, ioe.getMessage());
            }
        }
	}

}
