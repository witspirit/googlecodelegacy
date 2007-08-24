/**
 * @author vlerkenb
 * 13-feb-2003
 * ClearDatabaseAction.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.LogMonitor;

/**
 * Clears the database
 */
public class ClearDatabaseAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(ClearDatabaseAction.class);
    
    public ClearDatabaseAction()
    {
        super("Clear database");
    }
	
    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
        logger.info("Clearing the database...");
        LogMonitor.getInstance().getLogDatabase().clear();
	}

}
