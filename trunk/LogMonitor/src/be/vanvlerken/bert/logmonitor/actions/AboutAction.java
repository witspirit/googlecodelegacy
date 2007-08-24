/**
 * @author vlerkenb
 * 25-oct-2003
 * AboutAction.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * Displays an about box
 */
public class AboutAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(AboutAction.class);
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
        logger.info("Displaying the About dialog");
        
        String aboutText = 
            "LogMonitor\n" +
            "Version 1.2\n" +
            "\n" +
            "Author: Bert Van Vlerken\n" +
            "\n"+
            "This application is the sole property of Bert Van Vlerken\n"+
            "Copyright 2004";
             
        
        JOptionPane.showMessageDialog(mainWindow, aboutText);
	}
}
