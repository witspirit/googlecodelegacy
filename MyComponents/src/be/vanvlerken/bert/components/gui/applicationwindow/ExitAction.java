/**
 * @author vlerkenb
 * 4-feb-2003
 * ExitAction.java
 */

package be.vanvlerken.bert.components.gui.applicationwindow;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


/**
 * Calls the shutdown of the ApplicationWindow it resides on
 */
public class ExitAction extends AbstractAction
{
    private ApplicationWindow  appWindow;
    
    public ExitAction(ApplicationWindow appWindow)
    {
        super("Exit");
        this.appWindow = appWindow;
    }

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
        appWindow.dispose();
	}

}
