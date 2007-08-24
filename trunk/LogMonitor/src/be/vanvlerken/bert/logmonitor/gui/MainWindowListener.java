/**
 * @author vlerkenb
 * 3-feb-2003
 * MainWindowListener.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import be.vanvlerken.bert.logmonitor.LogMonitor;

/**
 * Takes care of the main Window events
 */
public class MainWindowListener extends WindowAdapter
{
	/**
	 * Constructor MainWindowListener.
	 * @param pm
	 */
	public MainWindowListener()
	{
	}

    
	/**
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e)
	{
		LogMonitor.getInstance().shutdown();
	}

}
