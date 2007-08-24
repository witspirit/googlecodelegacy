/**
 * @author vlerkenb
 * 3-feb-2003
 * BasicGUI.java
 */
package be.vanvlerken.bert.logmonitor.gui;



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Builds up the complete GUI view
 */
public class BasicGUI implements IStatusBar
{
    private static Logger logger = Logger.getLogger("be.vanvlerken.bert.logmonitor");
    private JTabbedPane tabs = new JTabbedPane();
    private JTextField statusBar = new JTextField();
    private JFrame mainWindow = new JFrame("LogMonitor"); 
	
    /**
	 * Constructor for BasicGUI.
	 */
	public BasicGUI()
	{
        logger.fine("Creating the mainWindow");
        
        logger.finer("Setting the size of the mainWindow");
        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        mainWindow.setBounds(gE.getMaximumWindowBounds());
        
        logger.finer("Setting the proper windowevents on the mainWindow");
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainWindow.addWindowListener(new MainWindowListener());
        
        logger.fine("Initialising the status bar...");
        statusBar.setEditable(false);
        statusBar.setText("LogMonitor loading...");
        Border marginBorder = BorderFactory.createEmptyBorder(1,3,1,3);
        Border surroundBorder = BorderFactory.createEtchedBorder();
        Border compoundBorder = BorderFactory.createCompoundBorder(surroundBorder, marginBorder);
        statusBar.setBorder(compoundBorder);
              
        logger.fine("Creating the mainPanel...");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        logger.finer("Adding the JTabbedPane to the mainPanel");        
        mainPanel.add(tabs, BorderLayout.CENTER);
        
        logger.finer("Adding the StatusBar to the mainPanel");
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        mainWindow.getContentPane().add(mainPanel);
	}
    
    public void setVisible(boolean visible)
    {
        mainWindow.setVisible(visible);
    }
    
    public void setMenuBar(JMenuBar menuBar)
    {
        mainWindow.setJMenuBar(menuBar);
    }
    
    public void addTab(String title, Component component, String toolTip)
    {
        tabs.addTab(title, null, component, toolTip);
    }
    
    public void setStatus(String statusMessage)
    {
        statusBar.setText(statusMessage);
    }
    
    public JFrame getMainWindow()
    {
        return mainWindow;
    }
}
