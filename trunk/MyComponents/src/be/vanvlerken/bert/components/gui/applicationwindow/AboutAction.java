/**
 * @author vlerkenb
 * 4-feb-2003
 * AboutAction.java
 */
package be.vanvlerken.bert.components.gui.applicationwindow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Displays a typical 'About' dialog
 * In order to change the contents of this dialog bog. An application should obtain
 * the AboutMessage object via AboutMessage.getInstance() and change it's properties.
 * The next time the dialog box is openend, the new contents will be displayed.
 */
public class AboutAction extends AbstractAction
{
    private JFrame mainWindow;

    public AboutAction(JFrame mainWindow)
    {
        super("About...");
        this.mainWindow = mainWindow;
    }
      
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
        JOptionPane.showMessageDialog(mainWindow, AboutMessage.getInstance().getFormattedMessage());
	}
}
