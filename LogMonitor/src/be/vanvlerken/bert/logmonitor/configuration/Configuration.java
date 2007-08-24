/**
 * @author vlerkenb
 * 6-feb-2003
 * Configuration.java
 */
package be.vanvlerken.bert.logmonitor.configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import be.vanvlerken.bert.xmlparser2.XmlInterpreter;
import be.vanvlerken.bert.xmlparser2.XmlSequenceHandler;

/**
 * Provides the interface to all configuration information
 * Implemented as a Singleton
 */
public class Configuration implements Runnable, IConfigurationListener
{
    private static Logger logger = Logger.getLogger(Configuration.class);
    private static Configuration instance = null;
    
    private XmlSequenceHandler  xmlHandler;
    private static final String configTag = "LogMonitor";
    
    /* Configuration elements */
    private LoggingConfig           loggingConfig;

    
    private File saveFile;
    private boolean needsSave;
    
    private List listeners; 
	private boolean active;
    private Thread notificationThread;
    
    public static Configuration getInstance()
    {
        if ( instance == null )
        {
            instance = new Configuration();
        }
        return instance;
    }
    
    protected Configuration()
    {
        loggingConfig = new LoggingConfig();
        loggingConfig.addConfigurationListener(this);
        buildXmlHandler();
        saveFile = null;
        needsSave = false;
        listeners = new ArrayList();
        active = true;
        notificationThread = null;
    }
    
    
    public void readConfiguration(File file) throws SAXException, FileNotFoundException, IOException
    {
        logger.info("Reading configuration from "+file);
        
        try
		{
			FileInputStream fileStream = new FileInputStream(file);
            xmlHandler.reset(); 
            
            XmlInterpreter xmlInterpreter = new XmlInterpreter(fileStream, xmlHandler);
            try
			{
				xmlInterpreter.analyze();
                
                /* Notify all components of change in configuration ! */
                fireConfigUpdated();
            
                saveFile = file;
                needsSave = false;
                logger.info("Configuration loaded succesfully");
			}
			catch (SAXException e)
			{
                logger.error("An error occured while parsing the configuration file:\n"+e.getMessage());
                throw e;
			}
			catch (IOException e)
			{
                logger.error("An error occured while reading from the configuration file:\n"+e.getMessage());
                throw e;
			}
		}
		catch (FileNotFoundException e)
		{
            logger.error("File "+file+" was not found !");
            throw e;
		}
    }
	
    /**
	 * Method buildXmlHandler.
	 * @return IXmlEventHandler
	 */
	private void buildXmlHandler()
	{
		xmlHandler = new XmlSequenceHandler(configTag);
        
        xmlHandler.addTagHandler(loggingConfig.getXmlTagHandler());
 	}
    
    public void saveConfiguration() throws FileNotFoundException, IOException
    {
        saveConfigurationAs(saveFile);
    }
    
    public void saveConfigurationAs(File file) throws FileNotFoundException, IOException
    {
        if ( file == null )
        {
            throw new FileNotFoundException("No save file provided");
        }
        
        saveFile = file;
        logger.info("Saving configuration to "+saveFile);
        
        try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<"+configTag+">\n");
            
            loggingConfig.save(writer, "    ");
            
            writer.write("</"+configTag+">\n");
            
            writer.flush();
            writer.close();
            
            logger.info("Configuration saved");
            needsSave = false;
		}
		catch (IOException e)
		{
            logger.error("An error occured while writing the configuration file:\n"+e.getMessage());;
            throw e;
		}
    }
	
    public void configUpdated()
    {
        fireConfigUpdated();
    }

    /**
     * Trigger an update of the configuration
     */
    protected synchronized void fireConfigUpdated()
    {
        /* Notify all components of a change in configuration */
        notifyAll();
        needsSave = true;
    }
      
    public synchronized void addConfigurationListener(IConfigurationListener listener)
    {
        listeners.add(listener);
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public synchronized void run()
    {
        while ( active )
        {
            if ( !listeners.isEmpty() )
            {
                Iterator listener = listeners.iterator();
                while ( listener.hasNext() )
                {
                    ((IConfigurationListener) listener.next()).configUpdated();
                }
            }
            
            try
			{
				wait();
			}
			catch (InterruptedException e)
			{
                logger.info("Got waken up...");
			}
        }
    }    
    
    public void activate()
    {
        if ( notificationThread == null )
        {
            active = true;
            notificationThread = new Thread(this);
            notificationThread.start();
        }
    }
    
    public synchronized void deactivate()
    {
        active = false;
        notificationThread = null;
        notifyAll();
    }
	
    /**
	 * Returns the needsSave.
	 * @return boolean
	 */
	public boolean needsSave()
	{
		return needsSave;
	}

	/**
	 * Returns the saveFile.
	 * @return File
	 */
	public File getConfigFile()
	{
		return saveFile;
	}
	
    /**
     * Returns the loggingConfig.
     * @return LoggingConfig
     */
    public LoggingConfig getLoggingConfig()
    {
        return loggingConfig;
    }     
}
