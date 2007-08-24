/**
 * @author vlerkenb
 * 4-feb-2003
 * ExitAction.java
 */

package be.vanvlerken.bert.logmonitor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.LogMonitor;

/**
 * description
 */
public class ExitAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(ExitAction.class);
    private JFrame  mainWindow;
    
    public ExitAction(JFrame mainWindow)
    {
        super("Exit");
        this.mainWindow = mainWindow;
    }

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
        logger.info("Exiting LogMonitor");
        mainWindow.dispose();
        LogMonitor.getInstance().shutdown();
	}

}
