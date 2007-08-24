/**
 * @author wItspirit
 * 23-apr-2003
 * OpenAction.java
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
 * Opens a certain file
 */
public class OpenAction extends AbstractAction
{
    private JFrame                      parent;
    private JFileChooser                fileSelector;
    private FileOperationsInterface     fileOpsImplementor;

    public OpenAction(String identifier,
                       JFrame parent, 
                       JFileChooser fileSelector,
                       FileOperationsInterface fileOpsImplementor)
    {
        super("Open "+identifier+"...");
        this.parent = parent;
        this.fileSelector = fileSelector;
        this.fileOpsImplementor = fileOpsImplementor;
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        int returnVal = fileSelector.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            File selectedFile = fileSelector.getSelectedFile();
            
            try
            {
                fileOpsImplementor.open(selectedFile);
            }
            catch (FileNotFoundException e)
            {
                JOptionPane.showMessageDialog(parent, e.getMessage(), "File Not Found Exception", JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(parent, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
