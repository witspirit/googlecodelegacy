/**
 * @author vlerkenb
 * 3-feb-2003
 * LogMonitor.java
 */

package be.vanvlerken.bert.logmonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import be.vanvlerken.bert.logmonitor.actions.SaveConfigAction;
import be.vanvlerken.bert.logmonitor.configuration.Configuration;
import be.vanvlerken.bert.logmonitor.configuration.IConfigurationListener;
import be.vanvlerken.bert.logmonitor.gui.*;
import be.vanvlerken.bert.logmonitor.logging.CoderFactory;
import be.vanvlerken.bert.logmonitor.logging.DecoderProxy;
import be.vanvlerken.bert.logmonitor.logging.LogDatabase;
import be.vanvlerken.bert.logmonitor.logging.ILogDatabase;
import be.vanvlerken.bert.logmonitor.logging.LogServer;
import be.vanvlerken.bert.logmonitor.logging.ServerFactory;
import be.vanvlerken.bert.logmonitor.logging.TextDbPersister;

/**
 * The Main of the application
 * This will start and initialise everything that makes up the LogMonitor
 */
public class LogMonitor implements IConfigurationListener
{
    private static Logger logger = Logger.getLogger(LogMonitor.class);    
    private static LogMonitor instance;

    private LogDatabase logDb;
    private LogMonitorGUI gui;    
    private LogServer logServer;
    private Configuration config;
    private DecoderProxy decoderProxy;
    private CoderFactory coderFactory;
    private ServerFactory serverFactory;

    public static void main(String[] args)
    {
        instance = new LogMonitor(args);
        instance.start();
    }

    public static LogMonitor getInstance()
    {
        return instance;
    }

    public LogMonitor(String[] args)
    {        
        logDb = null;
        gui = null;
        decoderProxy = null;
        coderFactory = null;
    }

    public void start()
    {
        initLogger();
        logger.info("Logging system initialised");
        
        logger.info("Building the CoderFactory...");
        try
        {
            coderFactory = new CoderFactory();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            shutdown();
        }
        
        logger.info("Building the ServerFactory");
        serverFactory = new ServerFactory();

        logger.info("Attempting to load the default configuration...");
        initConfiguration();

        logger.info("Creating the Logging database...");
        initLogDatabase();
        
        logger.info("Creating the GUI...");
        gui = new LogMonitorGUI(logDb, new TextDbPersister(logDb));

        logger.info("Displaying the GUI...");
        gui.setVisible(true);

        logger.info("Creating the LogServer...");
        try
        {
            decoderProxy = new DecoderProxy(coderFactory.getDecoder(config.getLoggingConfig().getServerDecoder()));
        }
        catch (ClassNotFoundException e)
        {
            logger.error("Could not instantiate configured decoder (" + config.getLoggingConfig().getServerDecoder() + ")");
            logger.error("Continuing with the default decoder: " + coderFactory.getDefaultDecoder().getClass().getName());
            decoderProxy = new DecoderProxy(coderFactory.getDefaultDecoder());
        }
        
        logServer = serverFactory.getLogServer(config.getLoggingConfig().getServerType(),config.getLoggingConfig().getLogServer(), logDb, decoderProxy);

        logger.info("Starting the LogServer...");
        logServer.activate();

        /* Normally triggered by ProxyGui */
        /* shutdown(); */
    }

    /**
     * Method initConfiguration.
     */
    private void initConfiguration()
    {
        config = Configuration.getInstance();
        File defaultConfigFile = new File(System.getProperty("user.dir") + "/logmonitor.xml");
        try
        {
            config.readConfiguration(defaultConfigFile);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        config.addConfigurationListener(this);
        config.activate();
    }

    /**
     * Method initLogDatabase.
     */
    private void initLogDatabase()
    {
        logDb = new LogDatabase(config.getLoggingConfig().respectsNewlines());
        logDb.activate();
    }

    /**
     * Method initLogger.
     */
    private void initLogger()
    {
        // Log4J configuration
        PropertyConfigurator.configure("log4j.properties");
    }

    /**
     * Method shutdown.
     * Cleans up the LogMonitor application in a clean way
     */
    public void shutdown()
    {
        logger.info("Shutting down...");

        logger.info("Deactivating configuration changes");        
        config.deactivate();
        if (config.needsSave())
        {
            /* Should show dialog asking for saving or not
             * If they want to save, try saveConfigAction
             */
            logger.info("Should check wether the user wants to save his configuration or not");

            int yesno =
                JOptionPane.showConfirmDialog(
                    gui.getMainWindow(),
                    "Configuration has changed since last save.\nDo you want to save your configuration ?",
                    "Do you want to save configuration ?",
                    JOptionPane.YES_NO_OPTION);
            if (yesno == JOptionPane.YES_OPTION)
            {
                /* Create saveConfigAction */
                SaveConfigAction saveAction = new SaveConfigAction(gui.getMainWindow());
                saveAction.actionPerformed(null);
            }
        }

        if (logServer != null)
        {
            logger.info("Stopping the LogServer...");
            logServer.deactivate();
        }

        if (gui != null)
        {
            logger.info("Deactivating the GUI...");
            gui.setVisible(false);
        }

        if (logDb != null)
        {
            logger.info("Deactivating the LogDb...");
            logDb.deactivate();
        }

        logger.info("Clean shutdown.");

        /* For an unknown reason the application won't stop here... All my own
         * threads seem to have stopped cleanly and I'm not doing anything anymore.
         * Nevertheless, something is keeping the application awake...
         * Thus killing it the hard way...
         */
        try
        {
            /* Giving the other threads the time to shutdown cleanly */
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
        }
        /* Whichever thread still lingering around, is not going to die automatically...
         * Thus, it's time to forcebly destroy them.
         */
        System.exit(0);
    }

    /**
     * Returns the LogDatabase
     * @return ILogDatabase
     */
    public ILogDatabase getLogDatabase()
    {
        return logDb;
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.configuration.IConfigurationListener#configUpdated()
     */
    public void configUpdated()
    {
        /* Check if we must reinstantiate the logServer */        
        if (logServer != null && isServerUpdated() )
        {
            /* Requires update */
            logger.info("Reinstatiating the logServer with new settings");
            logServer.deactivate();
            logServer = serverFactory.getLogServer(config.getLoggingConfig().getServerType(),config.getLoggingConfig().getLogServer(), logDb, decoderProxy);
            logServer.activate();            
        }

        /* Check if we have to reconfigure the decoderProxy */
        if (decoderProxy != null)
        {
            String currentDecoder = decoderProxy.getDecoder().getClass().getName();
            String configDecoder = config.getLoggingConfig().getServerDecoder();
            if (!currentDecoder.equals(configDecoder))
            {
                try
                {
                    decoderProxy.setDecoder(coderFactory.getDecoder(configDecoder));
                }
                catch (ClassNotFoundException e)
                {
                    logger.error("Could not set decoder to new value !");
                    logger.error("Keeping the original setting");
                }
            }
        }
    }

    /**
     * @return
     */
    private boolean isServerUpdated()
    {
        return !logServer.getListenAddress().equals(config.getLoggingConfig().getLogServer()) ||
               !logServer.getClass().getName().equals(config.getLoggingConfig().getServerType());
    }
    
    /**
     * @return Returns the coderFactory.
     */
    public CoderFactory getCoderFactory()
    {
        return coderFactory;
    }
    
    /**
     * @return Returns the serverFactory.
     */
    public ServerFactory getServerFactory()
    {
        return serverFactory;
    }
}
