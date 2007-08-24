/**
 * @author wItspirit
 * 1-jan-2005
 * GuiConfig.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.configuration;

/**
 * Container for the GUI configuration
 */
public class GuiConfig
{
    public static final int LOCAL_INTERFACE = 1;
    public static final int RMI_INTERFACE   = 2;

    private int             PacketDistributorInterface;
    private String          ConfigFile;
    private String          RmiUrl;

    public GuiConfig()
    {
        PacketDistributorInterface = LOCAL_INTERFACE;
        ConfigFile = "packetdistributor.xml";
        RmiUrl = "rmi://localhost:1099/PacketDistributor";
    }

    /**
     * @return Returns the packetDistributorInterface.
     */
    public int getPacketDistributorInterface()
    {
        return PacketDistributorInterface;
    }

    /**
     * @return Returns the rmiUrl.
     */
    public String getRmiUrl()
    {
        return RmiUrl;
    }

    /**
     * @param packetDistributorInterface
     *            The packetDistributorInterface to set.
     */
    public void setPacketDistributorInterface(int packetDistributorInterface)
    {
        PacketDistributorInterface = packetDistributorInterface;
    }

    /**
     * @param rmiUrl
     *            The rmiUrl to set.
     */
    public void setRmiUrl(String rmiUrl)
    {
        RmiUrl = rmiUrl;
    }
   
    /**
     * @return Returns the configFile.
     */
    public String getConfigFile()
    {
        return ConfigFile;
    }
    
    /**
     * @param configFile The configFile to set.
     */
    public void setConfigFile(String configFile)
    {
        ConfigFile = configFile;
    }
    
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if ( obj instanceof GuiConfig )
        {
            GuiConfig other = (GuiConfig) obj;
            return (PacketDistributorInterface == other.PacketDistributorInterface)
                    &&
                    (ConfigFile.equals(other.ConfigFile))
                    &&
                    (RmiUrl.equals(other.RmiUrl));
        }
        else return false;
    }
}
