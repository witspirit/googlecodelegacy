/**
 * @author vlerkenb
 * 4-feb-2003
 * LogMonitorGUI.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import javax.swing.*;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.logging.IDatabasePersister;
import be.vanvlerken.bert.logmonitor.logging.ILogView;


/**
 * Builds up the GUI
 */
public class LogMonitorGUI
{
    private static Logger logger = Logger.getLogger(LogMonitorGUI.class);
    
    private BasicGUI gui;
    private GeneralLoggingPanel generalLoggingPanel;
    private ModuleLoggingPanel moduleLoggingPanel;
    
    public LogMonitorGUI(ILogView logDb, IDatabasePersister dbPersister)
    {
        gui = new BasicGUI();
        
        logger.info("Creating the menubar...");
        MainMenuBar mainMenuBar = new MainMenuBar(gui.getMainWindow(), dbPersister);
        gui.setMenuBar(mainMenuBar);
        
        logger.info("Adding Tabs...");
        
        generalLoggingPanel = new GeneralLoggingPanel(logDb,gui);
        gui.addTab("Logging", generalLoggingPanel, "Shows RAW logging");
        
        moduleLoggingPanel = new ModuleLoggingPanel(logDb, gui);
        gui.addTab("Module Logging", moduleLoggingPanel, "Shows only logging for specific modules");
        
        gui.setStatus("LogMonitor started succesfully");
    }
    
    public void setVisible(boolean visible)
    {
        gui.setVisible(visible);
        if ( visible == true )
        {
            generalLoggingPanel.activate();  
            moduleLoggingPanel.activate();      
        }        
        else
        {
            generalLoggingPanel.deactivate();
            moduleLoggingPanel.deactivate();
        }
    }
    
    public JFrame getMainWindow()
    {
        return gui.getMainWindow();
    }
}
