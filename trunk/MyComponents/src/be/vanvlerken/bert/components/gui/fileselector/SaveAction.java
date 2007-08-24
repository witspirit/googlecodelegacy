/**
 * @author wItspirit
 * 23-apr-2003
 * SaveAction.java
 */
package be.vanvlerken.bert.components.gui.fileselector;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Saves a certain file
 */
public class SaveAction extends AbstractAction
{
    private JFrame                      parent;
    private JFileChooser                fileSelector;
    private FileOperationsInterface     fileOpsImplementor;

    public SaveAction(String identifier,
                       JFrame parent, 
                       JFileChooser fileSelector,
                       FileOperationsInterface fileOpsImplementor)
    {
        super("Save "+identifier+"...");
        this.parent = parent;
        this.fileSelector = fileSelector;
        this.fileOpsImplementor = fileOpsImplementor;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            fileOpsImplementor.save();
        }
        catch (FileNotFoundException fe)
        {
            /* Probably there was no correct save file, should display save dialog */
            int returnVal = fileSelector.showSaveDialog(parent);
            if(returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File selectedFile = fileSelector.getSelectedFile();
            
                try
                {
                    fileOpsImplementor.saveAs(selectedFile);
                }
                catch (FileNotFoundException fnfe)
                {
                    JOptionPane.showMessageDialog(parent, fnfe.getMessage(), "File Not Found Exception", JOptionPane.ERROR_MESSAGE);
                }
                catch (IOException ioe)
                {
                    JOptionPane.showMessageDialog(parent, ioe.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

}
