/**
 * @author wItspirit
 * 22-feb-2003
 * LoggingConfig.java
 */
package be.vanvlerken.bert.logmonitor.configuration;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import be.vanvlerken.bert.xmlparser2.BooleanXmlHandler;
import be.vanvlerken.bert.xmlparser2.ColorXmlHandler;
import be.vanvlerken.bert.xmlparser2.IntXmlHandler;
import be.vanvlerken.bert.xmlparser2.IpXmlHandler;
import be.vanvlerken.bert.xmlparser2.TextXmlHandler;
import be.vanvlerken.bert.xmlparser2.XmlSequenceHandler;
import be.vanvlerken.bert.xmlparser2.IXmlTagHandler;

/**
 * Hosts configuration information related to logging
 */
public class LoggingConfig implements IConfigPersistency
{
    private XmlSequenceHandler loggingHandler;
    private XmlSequenceHandler logServerHandler;
    private XmlSequenceHandler logLevelsHandler;
    private XmlSequenceHandler displayColorHandler;
    private IpXmlHandler loggingIpHandler;
    private IntXmlHandler loggingPortHandler;
    private TextXmlHandler loggingDecoderHandler;
    private TextXmlHandler loggingServerTypeHandler;
    private BooleanXmlHandler errorLevelHandler;
    private BooleanXmlHandler warningLevelHandler;
    private BooleanXmlHandler infoLevelHandler;
    private BooleanXmlHandler verboseLevelHandler;
    private ColorXmlHandler errorColorHandler;
    private ColorXmlHandler warningColorHandler;
    private ColorXmlHandler infoColorHandler;
    private ColorXmlHandler verboseColorHandler;
    private BooleanXmlHandler newestAtTheTopHandler;
    
    private List    listeners;
    private BooleanXmlHandler respectNewlinesHandler;
    private XmlSequenceHandler logDatabaseHandler;

    public LoggingConfig()
    {
        listeners = new ArrayList();
        
        respectNewlinesHandler = new BooleanXmlHandler("RespectNewlines");
        
        logDatabaseHandler = new XmlSequenceHandler("Database");
        logDatabaseHandler.addTagHandler(respectNewlinesHandler);
        
        loggingServerTypeHandler = new TextXmlHandler("Type");
        loggingIpHandler = new IpXmlHandler("IP");
        loggingPortHandler = new IntXmlHandler("Port");
        loggingDecoderHandler = new TextXmlHandler("Decoder");

        logServerHandler = new XmlSequenceHandler("Server");
        logServerHandler.addTagHandler(loggingServerTypeHandler);
        logServerHandler.addTagHandler(loggingIpHandler);
        logServerHandler.addTagHandler(loggingPortHandler);
        logServerHandler.addTagHandler(loggingDecoderHandler);

        errorLevelHandler = new BooleanXmlHandler("Error");
        warningLevelHandler = new BooleanXmlHandler("Warning");
        infoLevelHandler = new BooleanXmlHandler("Info");
        verboseLevelHandler = new BooleanXmlHandler("Verbose");

        logLevelsHandler = new XmlSequenceHandler("Levels");
        logLevelsHandler.addTagHandler(errorLevelHandler);
        logLevelsHandler.addTagHandler(warningLevelHandler);
        logLevelsHandler.addTagHandler(infoLevelHandler);
        logLevelsHandler.addTagHandler(verboseLevelHandler);

        errorColorHandler = new ColorXmlHandler("Error");
        warningColorHandler = new ColorXmlHandler("Warning");
        verboseColorHandler = new ColorXmlHandler("Verbose");
        infoColorHandler = new ColorXmlHandler("Info");

        displayColorHandler = new XmlSequenceHandler("DisplayColors");
        displayColorHandler.addTagHandler(errorColorHandler);
        displayColorHandler.addTagHandler(warningColorHandler);
        displayColorHandler.addTagHandler(infoColorHandler);
        displayColorHandler.addTagHandler(verboseColorHandler);

        newestAtTheTopHandler = new BooleanXmlHandler("NewestAtTheTop");

        loggingHandler = new XmlSequenceHandler("Logging");
        loggingHandler.addTagHandler(logDatabaseHandler);
        loggingHandler.addTagHandler(logServerHandler);
        loggingHandler.addTagHandler(logLevelsHandler);
        loggingHandler.addTagHandler(displayColorHandler);
        loggingHandler.addTagHandler(newestAtTheTopHandler);

        respectNewlinesHandler.setBoolean(false);
        loggingServerTypeHandler.setText("be.vanvlerken.bert.logmonitor.logging.StringServer");
        try
        {
            loggingIpHandler.setIpAddress(InetAddress.getByName("0.0.0.0"));
        }
        catch (UnknownHostException e)
        {
            loggingIpHandler.setIpAddress(null);
        }
        loggingPortHandler.setInt(10000);
        loggingDecoderHandler.setText("be.vanvlerken.bert.logmonitor.logging.LogMessageDecoder");

        errorLevelHandler.setBoolean(true);
        warningLevelHandler.setBoolean(true);
        infoLevelHandler.setBoolean(true);
        verboseLevelHandler.setBoolean(true);

        errorColorHandler.setColor(Color.red);
        warningColorHandler.setColor(Color.orange);
        infoColorHandler.setColor(Color.white);
        verboseColorHandler.setColor(Color.white);

        newestAtTheTopHandler.setBoolean(true);
    }

    /**
     * Trigger an update of the configuration
     */
    protected void fireConfigUpdated()
    {
        List executeListeners = new ArrayList(listeners);
        
        for (int i=0; i < executeListeners.size(); i++)
        {
            IConfigurationListener listener = (IConfigurationListener) executeListeners.get(i);
            listener.configUpdated();
        }
    }

    public void addConfigurationListener(IConfigurationListener listener)
    {
        listeners.add(listener);
    }

    public IXmlTagHandler getXmlTagHandler()
    {
        return loggingHandler;
    }

    public void save(Writer writer, String prefix) throws IOException
    {
        loggingHandler.save(writer, prefix);
    }

    /**
     * Returns the logServer.
     * @return InetSocketAddress
     */
    public InetSocketAddress getLogServer()
    {
        return new InetSocketAddress(loggingIpHandler.getIpAddress(), loggingPortHandler.getInt());
    }

    /**
     * Sets the logServer.
     * @param logServer The logServer to set
     */
    public void setLogServer(InetSocketAddress logServer)
    {
        if (!(logServer.getAddress().equals(loggingIpHandler.getIpAddress()) && logServer.getPort() == loggingPortHandler.getInt()))
        {
            loggingIpHandler.setIpAddress(logServer.getAddress());
            loggingPortHandler.setInt(logServer.getPort());
            fireConfigUpdated();
        }
    }

    public String getServerType()
    {
        return loggingServerTypeHandler.getText();
    }
    
    public void setServerType(String serverType)
    {
        if (!serverType.equals(loggingServerTypeHandler.getText()))
        {
            loggingServerTypeHandler.setText(serverType);
            fireConfigUpdated();
        }
    }
    
    public String getServerDecoder()
    {
        return loggingDecoderHandler.getText();
    }

    public void setServerDecoder(String decoder)
    {
        if (!decoder.equals(loggingDecoderHandler.getText()))
        {
            loggingDecoderHandler.setText(decoder);
            fireConfigUpdated();
        }
    }

    /**
     * Indicates if errors are logged
     * @return Boolean
     */
    public boolean logErrors()
    {
        return errorLevelHandler.getBoolean();
    }

    /**
     * Indicates if warnings are logged
     * @return Boolean
     */
    public boolean logWarnings()
    {
        return warningLevelHandler.getBoolean();
    }

    /**
     * Indicates if informational messages are logged
     * @return Boolean
     */
    public boolean logInfo()
    {
        return infoLevelHandler.getBoolean();
    }

    /**
     * Indicates if verbose messages are logged
     * @return Boolean
     */
    public boolean logVerbose()
    {
        return verboseLevelHandler.getBoolean();
    }

    /**
     * Activates or deactivates the logging of errors
     * @param logErrors
     */
    public void logErrors(boolean logErrors)
    {
        if (logErrors != errorLevelHandler.getBoolean())
        {
            errorLevelHandler.setBoolean(logErrors);
            fireConfigUpdated();
        }
    }

    /**
     * Activates or deactivates the logging of warnings
     * @param logWarnings
     */
    public void logWarnings(boolean logWarnings)
    {
        if (logWarnings != warningLevelHandler.getBoolean())
        {
            warningLevelHandler.setBoolean(logWarnings);
            fireConfigUpdated();
        }
    }

    /**
     * Activates or deactivates the logging of informational messages
     * @param logInfo
     */
    public void logInfo(boolean logInfo)
    {
        if (logInfo != infoLevelHandler.getBoolean())
        {
            infoLevelHandler.setBoolean(logInfo);
            fireConfigUpdated();
        }
    }

    /**
     * Activates or deactivates the logging of verbose messages
     * @param logVerbose
     */
    public void logVerbose(boolean logVerbose)
    {
        if (logVerbose != verboseLevelHandler.getBoolean())
        {
            verboseLevelHandler.setBoolean(logVerbose);
            fireConfigUpdated();
        }
    }

    /**
     * Returns the errorColor.
     * @return Color
     */
    public Color getErrorColor()
    {
        return errorColorHandler.getColor();
    }

    /**
     * Returns the infoColor.
     * @return Color
     */
    public Color getInfoColor()
    {
        return infoColorHandler.getColor();
    }

    /**
     * Returns the verboseColor.
     * @return Color
     */
    public Color getVerboseColor()
    {
        return verboseColorHandler.getColor();
    }

    /**
     * Returns the warningColor.
     * @return Color
     */
    public Color getWarningColor()
    {
        return warningColorHandler.getColor();
    }

    /**
     * Sets the errorColor.
     * @param errorColor The errorColor to set
     */
    public void setErrorColor(Color errorColor)
    {
        if (!errorColor.equals(errorColorHandler.getColor()))
        {
            errorColorHandler.setColor(errorColor);
            fireConfigUpdated();
        }
    }

    /**
     * Sets the infoColor.
     * @param infoColor The infoColor to set
     */
    public void setInfoColor(Color infoColor)
    {
        if (!infoColor.equals(infoColorHandler.getColor()))
        {
            infoColorHandler.setColor(infoColor);
            fireConfigUpdated();
        }
    }

    /**
     * Sets the verboseColor.
     * @param verboseColor The verboseColor to set
     */
    public void setVerboseColor(Color verboseColor)
    {
        if (!verboseColor.equals(verboseColorHandler.getColor()))
        {
            verboseColorHandler.setColor(verboseColor);
            fireConfigUpdated();
        }
    }

    /**
     * Sets the warningColor.
     * @param warningColor The warningColor to set
     */
    public void setWarningColor(Color warningColor)
    {
        if (!warningColor.equals(warningColorHandler.getColor()))
        {
            warningColorHandler.setColor(warningColor);
            fireConfigUpdated();
        }
    }

    public boolean isNewestAtTheTop()
    {
        return newestAtTheTopHandler.getBoolean();
    }

    public void setNewestAtTheTop(boolean newestAtTheTop)
    {
        if (newestAtTheTop != newestAtTheTopHandler.getBoolean())
        {
            newestAtTheTopHandler.setBoolean(newestAtTheTop);
            fireConfigUpdated();
        }
    }
    
    public boolean respectsNewlines()
    {
        return respectNewlinesHandler.getBoolean();
    }

    public void setRespectNewlines(boolean respect)
    {
        if (respect != respectNewlinesHandler.getBoolean())
        {
            respectNewlinesHandler.setBoolean(respect);
            fireConfigUpdated();
        }
    }
}
