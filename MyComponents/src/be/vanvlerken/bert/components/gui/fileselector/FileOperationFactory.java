/**
 * @author wItspirit
 * 23-apr-2003
 * FileOperationFactory.java
 */
package be.vanvlerken.bert.components.gui.fileselector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 * Offers a set op preconfigured actions that logically belong together
 */
public class FileOperationFactory
{
    public static final int NO_SEPERATOR = 0;
    public static final int TOP_SEPERATOR = 1;
    public static final int BOTTOM_SEPERATOR = 2;
    public static final int TOP_AND_BOTTOM_SEPERATOR = 3;
    
    private OpenAction      openAction;
    private SaveAction      saveAction;
    private SaveAsAction    saveAsAction;
    
    public FileOperationFactory(String operationsIdentifier,
                                 JFileChooser fileSelector,
                                 FileOperationsInterface fileOpsImplementor,
                                 JFrame parent)
    {
        openAction = new OpenAction(operationsIdentifier, parent, fileSelector, fileOpsImplementor);   
        saveAction = new SaveAction(operationsIdentifier, parent, fileSelector, fileOpsImplementor);
        saveAsAction = new SaveAsAction(operationsIdentifier, parent, fileSelector, fileOpsImplementor);
    }
    
    public int insertIntoMenu(JMenu menu, int offset)
    {
        int seperatorLocation;
        
        if ( offset == 0 && menu.getItemCount() == 0 )
        {
            seperatorLocation = NO_SEPERATOR;
        }
        else if ( offset == menu.getItemCount() )
        {
            seperatorLocation = TOP_SEPERATOR;
        }
        else if ( offset == 0 && menu.getItemCount() != 0 )
        {
            seperatorLocation = BOTTOM_SEPERATOR;
        }
        else
        {
            seperatorLocation = TOP_AND_BOTTOM_SEPERATOR;
        }
        
        return insertIntoMenu(menu, offset, seperatorLocation);
    }
    
    public int insertIntoMenu(JMenu menu, int offset, int seperatorLocation)
    {
        JMenuItem openMI = new JMenuItem(getOpenAction());
        JMenuItem saveMI = new JMenuItem(getSaveAction());
        JMenuItem saveAsMI = new JMenuItem(getSaveAsAction());
        
        int internalOffset = offset;
        if ( seperatorLocation == TOP_SEPERATOR || seperatorLocation == TOP_AND_BOTTOM_SEPERATOR )
        {
            menu.add(new JSeparator(), internalOffset++);
        }
        menu.add(openMI, internalOffset++);
        menu.add(saveMI, internalOffset++);
        menu.add(saveAsMI, internalOffset++);
        if ( seperatorLocation == BOTTOM_SEPERATOR || seperatorLocation == TOP_AND_BOTTOM_SEPERATOR )
        {
            menu.add(new JSeparator(), internalOffset++);
        }

        return internalOffset;
    }
    
    /**
     * Returns the openAction.
     * @return OpenAction
     */
    public OpenAction getOpenAction()
    {
        return openAction;
    }

    /**
     * Returns the saveAction.
     * @return SaveAction
     */
    public SaveAction getSaveAction()
    {
        return saveAction;
    }

    /**
     * Returns the saveAsAction.
     * @return SaveAsAction
     */
    public SaveAsAction getSaveAsAction()
    {
        return saveAsAction;
    }
}
