/**
 * @author vlerkenb
 * 4-feb-2003
 * LogServerAction.java
 */
package be.vanvlerken.bert.logmonitor.actions;

import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.LogMonitor;
import be.vanvlerken.bert.logmonitor.configuration.Configuration;

/**
 * Provides the Logging configuration dialog
 */
public class LogServerAction extends AbstractAction
{
    private static Logger logger = Logger.getLogger(LogServerAction.class);

    private JFrame mainWindow;
    private Configuration config;

    public LogServerAction(JFrame mainWindow)
    {
        super("LogServer...");
        config = Configuration.getInstance();
        this.mainWindow = mainWindow;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        logger.info("Preparing the Logging Configuration dialog...");

        String[] decoders =LogMonitor.getInstance().getCoderFactory().getAvailableDecoders();
        /* Search for selected decoder */
        String currentDecoder = config.getLoggingConfig().getServerDecoder();
        int currentDecoderIndex = 0;
        for (int i = 0; i < decoders.length; i++)
        {
            if (decoders[i].equals(currentDecoder))
            {
                currentDecoderIndex = i;
                break;
            }
        }
        
        String[] servers = LogMonitor.getInstance().getServerFactory().getAvailableServers();
        /* Search for the selected server */
        String currentServer = config.getLoggingConfig().getServerType();
        int currentServerIndex = 0;
        for (int i= 0; i< servers.length; i++)
        {
            if ( servers[i].equals(currentServer))
            {
                currentServerIndex = i;
                break;
            }
        }

        LogServerDialog logServerDialog =
            new LogServerDialog(
                mainWindow,
                currentServerIndex,
                servers,
                config.getLoggingConfig().getLogServer().getAddress().getHostAddress(),
                config.getLoggingConfig().getLogServer().getPort(),
                currentDecoderIndex,
                decoders);

        int operation = logServerDialog.showDialog();
        if (operation == LogServerDialog.VALUES_SET)
        {
            logger.info("Values were set by the user");

            String ipStr = logServerDialog.getEnteredIp();
            int port = logServerDialog.getEnteredPort();
            int coder = logServerDialog.getEnteredCoder();
            int server = logServerDialog.getEnteredType();

            logger.info("Server: "+servers[server]);
            logger.info("IP: " + ipStr);
            logger.info("Port : " + port);
            logger.info("Coder : "+decoders[coder]);

            try
            {
                InetAddress ipAddress = InetAddress.getByName(ipStr);
                InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, port);

                config.getLoggingConfig().setLogServer(socketAddress);
                config.getLoggingConfig().setServerDecoder(decoders[coder]);
                config.getLoggingConfig().setServerType(servers[server]);
            }
            catch (UnknownHostException uhe)
            {
                logger.error("The value " + ipStr + " does not appear to be a valid name...");
            }
        }

    }
}
