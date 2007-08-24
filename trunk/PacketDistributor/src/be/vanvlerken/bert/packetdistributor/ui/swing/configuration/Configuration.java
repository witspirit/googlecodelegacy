/**
 * @author wItspirit
 * 20-apr-2003
 * Configuration.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import be.vanvlerken.bert.components.gui.fileselector.FileOperationsInterface;
import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.configuration.PacketDistributorFactory;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.ui.swing.backend.IPacketDistributorFacade;
import be.vanvlerken.bert.packetdistributor.ui.swing.backend.impl.LocalPacketDistributorFacade;
import be.vanvlerken.bert.packetdistributor.ui.swing.backend.impl.RemotePacketDistributorFacade;

import com.thoughtworks.xstream.XStream;

/**
 * Specific central configuration for the PacketDistributorGui application
 */
public class Configuration extends Observable implements FileOperationsInterface
{
    private static final Log         log      = LogFactory.getLog(Configuration.class);
    private static Configuration     instance = null;
    private XStream                  xstream;

    private IPacketDistributorFacade packetDistributor;
    private GuiConfig                guiConfig;

    private File                     currentFile;

    public static Configuration getInstance()
    {
        if (instance == null)
        {
            instance = new Configuration();
        }
        return instance;
    }

    protected Configuration()
    {
        log.debug("Creating and configuring XStream");
        xstream = new XStream();
        xstream.alias("PacketDistributorGuiConfig", GuiConfig.class);
        log.debug("Initialising GuiConfig with defaults");
        guiConfig = new GuiConfig(); // Init with defaults
        packetDistributor = null;
        currentFile = null;
    }

    /**
     * @see be.vanvlerken.bert.components.gui.fileselector.FileOperationsInterface#open(File)
     */
    public void open(File file) throws FileNotFoundException, IOException
    {
        log.debug("Opening " + file + " for reading the GUI configuration");
        guiConfig = readGuiConfig(file);
        currentFile = file;
    }

    /**
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    private GuiConfig readGuiConfig(File file) throws FileNotFoundException
    {
        return (GuiConfig) xstream.fromXML(new InputStreamReader(new FileInputStream(file)));
    }

    /**
     * @see be.vanvlerken.bert.components.gui.fileselector.FileOperationsInterface#save()
     */
    public void save() throws FileNotFoundException, IOException
    {
        saveAs(currentFile);
    }

    /**
     * @see be.vanvlerken.bert.components.gui.fileselector.FileOperationsInterface#saveAs(File)
     */
    public void saveAs(File file) throws FileNotFoundException, IOException
    {
        if (file != null)
        {
            log.debug("Saving GUI configuration in " + file);
            writeGuiConfig(guiConfig, file);
            currentFile = file;
        }
        else
        {
            throw new FileNotFoundException("No proper file selected");
        }
    }

    /**
     * @param file
     * @throws FileNotFoundException
     */
    private void writeGuiConfig(GuiConfig config, File file) throws FileNotFoundException
    {
        xstream.toXML(config, new OutputStreamWriter(new FileOutputStream(file)));
    }

    public IPacketDistributorFacade getPacketDistributorAPI()
    {
        if (packetDistributor == null)
        {
            // Multiple possibilities here
            switch (guiConfig.getPacketDistributorInterface())
            {
            case GuiConfig.RMI_INTERFACE:
                log.debug("RMI Interface configured");
                RmiProxyFactoryBean rmiFactory = new RmiProxyFactoryBean();
                rmiFactory.setServiceInterface(IRemotePacketDistributorAPI.class);
                rmiFactory.setServiceUrl(guiConfig.getRmiUrl());
                try
                {
                    rmiFactory.afterPropertiesSet();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                IRemotePacketDistributorAPI remoteApi = (IRemotePacketDistributorAPI) rmiFactory.getObject();
                packetDistributor = new RemotePacketDistributorFacade(remoteApi);
                break;
            default:
                // Should log some warning maybe...
                log.warn("Unknown interface configured. Defaulting to local interface");
            case GuiConfig.LOCAL_INTERFACE:
                log.debug("Local interface configured");
                PacketDistributorFactory factory = new PacketDistributorFactory(guiConfig.getConfigFile());
                IPacketDistributorAPI localApi = factory.getPacketDistributorAPI();
                packetDistributor = new LocalPacketDistributorFacade(localApi);
                break;
            }
        }
        return packetDistributor;
    }

    /**
     * @return Returns the guiConfig.
     */
    public GuiConfig getGuiConfig()
    {
        return guiConfig;
    }

    public boolean needsSave()
    {
        try
        {
            if ( currentFile == null )
            {
                throw new FileNotFoundException("No current configuration file");
            }
            GuiConfig originalConfig = readGuiConfig(currentFile);
            return !originalConfig.equals(guiConfig);
        }
        catch (FileNotFoundException e)
        {
            return true;
        }
    }

    public void configUpdated()
    {
        packetDistributor = null; // Is proper cleanup handled by the
                                    // observers ?
        setChanged();
        notifyObservers(null);
    }
}
