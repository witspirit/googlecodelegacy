/**
 * @author wItspirit
 * 9-feb-2003
 * ShowConfigAction.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.configuration.Configuration;

/**
 * description
 */
public class ShowConfigAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(ShowConfigAction.class);
    private JFrame mainWindow;
    private Configuration config;
    
    public ShowConfigAction(JFrame mainWindow)
    {
        super("Show configuration");
        this.mainWindow = mainWindow;
        config = Configuration.getInstance();
    }

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
        logger.info("Show dialog with configuration settings");
        
        JPanel displayPanel = new JPanel(new FlowLayout());
        
        JPanel namesPanel = new JPanel(new GridLayout(4,1));
        JPanel valuesPanel = new JPanel(new GridLayout(4,1));
        
        displayPanel.add(namesPanel);
        displayPanel.add(valuesPanel);
        
        JLabel logIp = new JLabel("LogServer IP");
        JLabel logPort = new JLabel("LogServer Port");
        JLabel coder = new JLabel("LogServer coder");
        JLabel configFile = new JLabel("Current config file");
        
        JLabel currentLogIp = new JLabel(config.getLoggingConfig().getLogServer().getAddress().getHostAddress());
        JLabel currentLogPort = new JLabel((new Integer(config.getLoggingConfig().getLogServer().getPort()).toString()));
        JLabel currentCoder = new JLabel(config.getLoggingConfig().getServerDecoder());
        JLabel currentConfigFile;
        if ( config.getConfigFile() == null )
        {
            currentConfigFile = new JLabel("No Config File used yet");
        }
        else
        {
            currentConfigFile = new JLabel(config.getConfigFile().toString());
        }
        
        namesPanel.add(logIp);
        namesPanel.add(logPort);
        namesPanel.add(coder);
        namesPanel.add(configFile);
        
        valuesPanel.add(currentLogIp);
        valuesPanel.add(currentLogPort);
        valuesPanel.add(currentCoder);
        valuesPanel.add(currentConfigFile);
        
        JOptionPane.showMessageDialog(mainWindow, displayPanel);
	}

}
