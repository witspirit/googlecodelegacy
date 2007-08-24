/**
 * @author wItspirit
 * 23-apr-2003
 * SaveAsAction.java
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
 * Saves a certain file under a new name
 */
public class SaveAsAction extends AbstractAction
{
    private JFrame                      parent;
    private JFileChooser                fileSelector;
    private FileOperationsInterface     fileOpsImplementor;

    public SaveAsAction(String identifier,
                         JFrame parent, 
                         JFileChooser fileSelector,
                         FileOperationsInterface fileOpsImplementor)
    {
        super("Save "+identifier+" As...");
        this.parent = parent;
        this.fileSelector = fileSelector;
        this.fileOpsImplementor = fileOpsImplementor;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
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
}
